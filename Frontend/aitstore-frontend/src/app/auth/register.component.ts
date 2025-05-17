import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { CarritoService } from '../shared/services/carrito.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private carritoService = inject(CarritoService);

  registerForm: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  errorMessage: string | null = null;

  onSubmit(): void {
    if (this.registerForm.invalid) return;

    const datos = this.registerForm.value;

    this.http.post<{ token: string; refreshToken: string; usuarioId: number; nombre: string }>(
      `${environment.apiUrl}/auth/register`,
      datos
    )
    .subscribe({
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
        const anon = JSON.parse(localStorage.getItem(anonKey) || '[]');
        const userKey = `carrito_usuario_${usuarioId}`;
        const userCart = JSON.parse(localStorage.getItem(userKey) || '[]');
        const fusion = [...anon, ...userCart];
        localStorage.setItem(userKey, JSON.stringify(fusion));
        localStorage.removeItem(anonKey);

        // Recargar carrito
        this.carritoService.recargarCarrito();

        // Navegar al home
        this.router.navigateByUrl('/');
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = err.error?.message || 'Error al registrarse';
      }
    });
  }
}
