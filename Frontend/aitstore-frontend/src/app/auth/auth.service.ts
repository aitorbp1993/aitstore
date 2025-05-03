import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API_URL = 'http://localhost:8081/api/auth';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(email: string, password: string) {
    return this.http.post<{
      token: string;
      refreshToken: string;
      usuarioId: number;
      nombre: string;
    }>(`${this.API_URL}/login`, { email, password });
  }

  logout(): void {
    const id = localStorage.getItem('usuarioId');
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('usuarioId');
    localStorage.removeItem('nombre');
    localStorage.removeItem('rol');
    localStorage.removeItem(`carrito_usuario_${id}`);
    this.router.navigate(['/auth/login']);
  }

  refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post(`${this.API_URL}/refresh`, { refreshToken });
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsuarioId(): number {
    const id = localStorage.getItem('usuarioId');
    return id ? parseInt(id, 10) : 0;
  }

  getNombre(): string {
    return localStorage.getItem('nombre') || '';
  }

  getRol(): string {
    return localStorage.getItem('rol') || '';
  }
}
