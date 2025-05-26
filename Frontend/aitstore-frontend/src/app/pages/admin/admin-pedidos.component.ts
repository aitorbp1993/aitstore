import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { environment } from '../../../environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-pedidos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-pedidos.component.html',
  styleUrls: ['./admin-pedidos.component.scss']
})
export class AdminPedidosComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  pedidos: any[] = [];
  cargando = true;

  ngOnInit(): void {
    this.http.get<any[]>(`${environment.apiUrl}/pedidos`).subscribe({
      next: res => this.pedidos = res,
      error: err => {
        console.error('Error al cargar pedidos:', err);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los pedidos.',
          confirmButtonColor: '#ef4444'
        });
      },
      complete: () => this.cargando = false
    });
  }

  verDetalles(id: number): void {
    this.router.navigate(['/admin/pedidos', id]);
  }
}
