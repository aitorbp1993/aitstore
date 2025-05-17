import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';
import { CarritoService } from '../shared/services/carrito.service';

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
        const payload = JSON.parse(atob(token.split('.')[1]));
        const rol = payload.rol;
        localStorage.setItem('rol', rol);

        // Recargar carrito asociado al usuario
        this.carritoService.recargarCarrito();

        // Navegar según rol
        this.router.navigate([rol === 'ADMIN' ? '/admin/productos' : '/']);
      },
      error: (error: HttpErrorResponse) => {
        const detalles = error.error?.detalles as string[] | undefined;
        const isCredencialesErroneas =
          error.status === 401 ||
          (error.status === 500 && detalles?.includes('Credenciales erróneas'));

        this.errorMessage = isCredencialesErroneas
          ? 'Credenciales incorrectas'
          : 'Error al iniciar sesión';
      }
    });
  }
}
