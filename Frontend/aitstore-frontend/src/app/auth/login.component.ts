// src/app/auth/login.component.ts
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';
import { CarritoService, CarritoItem } from '../shared/services/carrito.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  private carritoService = inject(CarritoService);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  errorMessage: string | null = null;

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    const { email, password } = this.loginForm.value;
    this.authService.login(email, password).subscribe({
      next: ({ token, refreshToken, usuarioId, nombre }) => {
        // Guardar credenciales y datos de usuario
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('usuarioId', usuarioId.toString());
        localStorage.setItem('nombre', nombre);

        // Extraer y guardar rol desde el JWT
        const payload: any = JSON.parse(atob(token.split('.')[1]));
        const rol: string = payload.rol;
        localStorage.setItem('rol', rol);

        // Fusionar carrito anónimo con el del usuario
        const anonKey = 'carrito_usuario_anonimo';
        const anonCart: CarritoItem[] = JSON.parse(localStorage.getItem(anonKey) || '[]');
        const userKey = `carrito_usuario_${usuarioId}`;
        const userCart: CarritoItem[] = JSON.parse(localStorage.getItem(userKey) || '[]');
        const mergedCart: CarritoItem[] = [...anonCart, ...userCart];
        localStorage.setItem(userKey, JSON.stringify(mergedCart));
        localStorage.removeItem(anonKey);

        // Recargar carrito con el contenido fusionado
        this.carritoService.recargarCarrito();

        // Navegar según rol
        this.router.navigate([rol === 'ADMIN' ? '/admin/productos' : '/']);
      },
      error: (error: HttpErrorResponse) => {
        const detalles = error.error?.detalles as string[] | undefined;
        this.errorMessage =
          error.status === 401 || (error.status === 500 && detalles?.includes('Credenciales erróneas'))
            ? 'Credenciales incorrectas'
            : 'Error al iniciar sesión';
      }
    });
  }
}
