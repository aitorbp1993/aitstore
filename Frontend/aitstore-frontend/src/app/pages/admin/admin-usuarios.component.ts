import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-usuarios.component.html',
  styleUrls: ['./admin-usuarios.component.scss']
})
export class AdminUsuariosComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  usuarios: any[] = [];
  idAdminActual: number = 0;

  ngOnInit(): void {
    const id = localStorage.getItem('usuarioId');
    this.idAdminActual = id ? parseInt(id, 10) : 0;
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    const token = localStorage.getItem('token');
    if (!token) return;

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>(`${environment.apiUrl}/usuarios`, { headers }).subscribe({
      next: data => this.usuarios = data,
      error: (err) => {
        console.error('❌ Error al obtener usuarios:', err);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron obtener los usuarios.',
          confirmButtonColor: '#ef4444'
        });
      }
    });
  }

  puedeEliminar(usuario: any): boolean {
    return usuario.id !== this.idAdminActual;
  }

  eliminarUsuario(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Eliminar un usuario es irreversible.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc2626',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        const token = localStorage.getItem('token');
        if (!token) return;

        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

        this.http.delete(`${environment.apiUrl}/usuarios/${id}`, { headers }).subscribe({
          next: () => {
            this.usuarios = this.usuarios.filter(u => u.id !== id);
            Swal.fire({
              icon: 'success',
              title: 'Usuario eliminado',
              showConfirmButton: false,
              timer: 1500
            });
          },
          error: (err) => {
            console.error('❌ Error al eliminar usuario:', err);
            Swal.fire({
              icon: 'error',
              title: 'No se pudo eliminar',
              text: 'Este usuario podría estar vinculado a otros datos.',
              confirmButtonColor: '#ef4444'
            });
          }
        });
      }
    });
  }
}
