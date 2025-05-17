import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';

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

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  errorMessage: string | null = null;

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: ({ token, refreshToken }) => {
        // Decodificar JWT para extraer payload
        const payload = JSON.parse(atob(token.split('.')[1]));
        const rol = payload.rol;
        const nombre = payload.nombre;

        // Guardar solo token, refreshToken, rol y nombre
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('rol', rol);
        localStorage.setItem('nombre', nombre);

        // Redirigir según rol
        this.router.navigate([rol === 'ADMIN' ? '/admin/productos' : '/']);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.status === 401
          ? 'Credenciales incorrectas'
          : 'Error al iniciar sesión';
      }
    });
  }
}

