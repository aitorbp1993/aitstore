import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  usuario: any = null;
  pedidos: any[] = [];
  cargando = true;

  ngOnInit(): void {
    // ✅ Obtener usuario autenticado usando /me
    this.http.get<any>('http://localhost:8081/api/usuarios/me').subscribe({
      next: res => {
        this.usuario = res;

        // 🟢 Una vez obtenemos el usuario, cargamos sus pedidos
        this.http.get<any[]>(`http://localhost:8081/api/pedidos/usuario/${res.id}`).subscribe({
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

  verDetalles(id: number): void {
    this.router.navigate(['/pedidos', id]);
  }
}
