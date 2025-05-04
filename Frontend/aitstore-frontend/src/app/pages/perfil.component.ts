import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment'; // ✅ Importamos apiUrl

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
    this.http.get<any>(`${environment.apiUrl}/usuarios/me`).subscribe({
      next: res => {
        this.usuario = res;
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

  verDetalles(id: number): void {
    this.router.navigate(['/pedidos', id]);
  }
}
