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

    this.http.post<{ token: string }>(`${environment.apiUrl}/auth/register`, datos)
      .subscribe({
        next: (res) => {
          const token = res.token;
          // Decodificar JWT para extraer payload
          const payload = JSON.parse(atob(token.split('.')[1]));
          const rol = payload.rol;
          const nombre = payload.nombre;
          // Extraer ID robustamente (usuarioId, id o sub)
          const usuarioId = payload.usuarioId ?? payload.id ?? payload.sub ?? 0;

          // Guardar token y datos de usuario
          localStorage.setItem('token', token);
          localStorage.setItem('rol', rol);
          localStorage.setItem('nombre', nombre);
          localStorage.setItem('usuarioId', usuarioId.toString());

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

          // Redirigir al home
          this.router.navigateByUrl('/');
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error?.message || 'Error al registrarse';
        }
      });
  }
}
