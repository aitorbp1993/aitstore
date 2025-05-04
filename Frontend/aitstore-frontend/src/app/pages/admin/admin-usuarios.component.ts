import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

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

  ngOnInit(): void {
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
        alert('Error al obtener usuarios');
      }
    });
  }

  eliminarUsuario(id: number): void {
    if (!confirm('¿Estás seguro de eliminar este usuario?')) return;

    const token = localStorage.getItem('token');
    if (!token) return;

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.delete(`${environment.apiUrl}/usuarios/${id}`, { headers }).subscribe({
      next: () => {
        this.usuarios = this.usuarios.filter(u => u.id !== id);
        alert('✅ Usuario eliminado correctamente');
      },
      error: (err) => {
        console.error('❌ Error al eliminar usuario:', err);
        alert('Error al eliminar usuario');
      }
    });
  }
}
