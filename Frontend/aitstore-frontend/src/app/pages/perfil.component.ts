import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  perfilForm: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    direccion: ['', [Validators.maxLength(255)]],
    telefono: ['', [Validators.pattern(/^\d{9}$/)]]
  });

  pedidos: any[] = [];
  cargando = true;
  mensajeExito: string | null = null;
  mensajeError: string | null = null;

  ngOnInit(): void {
    this.http.get<any>(`${environment.apiUrl}/usuarios/me`).subscribe({
      next: res => {
        this.perfilForm.patchValue({
          nombre: res.nombre,
          direccion: res.direccion,
          telefono: res.telefono
        });

        this.http.get<any[]>(`${environment.apiUrl}/pedidos/usuario/${res.id}`).subscribe({
          next: pedidos => this.pedidos = pedidos,
          error: err => console.error('Error cargando pedidos:', err),
          complete: () => this.cargando = false
        });
      },
      error: err => {
        console.error('Error obteniendo usuario:', err);
        this.cargando = false;
      }
    });
  }

  guardarCambios(): void {
    this.mensajeExito = null;
    this.mensajeError = null;

    if (this.perfilForm.invalid) return;

    this.http.patch(`${environment.apiUrl}/usuarios/me`, this.perfilForm.value).subscribe({
      next: () => this.mensajeExito = 'Perfil actualizado correctamente.',
      error: err => {
        console.error(err);
        this.mensajeError = 'Error al actualizar el perfil.';
      }
    });
  }

  verDetalles(id: number): void {
    this.router.navigate(['/pedidos', id]);
  }
}
