import { inject } from '@angular/core';
import {
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpErrorResponse,
  HttpClient
} from '@angular/common/http';
import { catchError, switchMap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  const token = localStorage.getItem('token');

  // Añadimos el token al request si existe
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const refreshToken = localStorage.getItem('refreshToken');

      // Si no es un 401 o no hay refreshToken, devolvemos el error original
      if (error.status !== 401 || !refreshToken) {
        return throwError(() => error);
      }

      const http = inject(HttpClient);

      // Intentamos renovar el token con el refreshToken
      return http.post<any>(`${environment.apiUrl}/auth/refresh`, { refreshToken }).pipe(
        switchMap((res) => {
          const newToken = res.token;
          const newRefreshToken = res.refreshToken;

          // Guardamos los nuevos tokens
          localStorage.setItem('token', newToken);
          localStorage.setItem('refreshToken', newRefreshToken);

          // Reintentamos la petición original con el nuevo token
          const retryReq = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newToken}`
            }
          });

          return next(retryReq);
        }),
        catchError(refreshError => {
          // Si falla la renovación, limpiamos sesión y redirigimos al login
          localStorage.clear();
          window.location.href = '/auth/login';
          return throwError(() => refreshError);
        })
      );
    })
  );
};
