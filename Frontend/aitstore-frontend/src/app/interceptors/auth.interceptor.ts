import { inject } from '@angular/core';
import {
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpErrorResponse,
  HttpClient,
  HttpHeaders
} from '@angular/common/http';
import { catchError, switchMap, throwError, of } from 'rxjs';

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
      // Si no hay refreshToken guardado o no es un 401, devolvemos el error
      if (error.status !== 401 || !localStorage.getItem('refreshToken')) {
        return throwError(() => error);
      }

      const http = inject(HttpClient);
      const refreshToken = localStorage.getItem('refreshToken');

      // Intentamos renovar el token con el refreshToken
      return http.post<any>('http://localhost:8081/api/auth/refresh', { refreshToken }).pipe(
        switchMap((res) => {
          const newToken = res.token;
          const newRefreshToken = res.refreshToken;

          // Guardamos los nuevos tokens
          localStorage.setItem('token', newToken);
          localStorage.setItem('refreshToken', newRefreshToken);

          // Clonamos la petición original con el nuevo token
          const retryReq = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${newToken}`)
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
