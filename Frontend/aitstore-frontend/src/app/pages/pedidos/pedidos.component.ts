import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';
import Swal from 'sweetalert2';

interface PedidoItem {
  nombreProducto: string;
  cantidad: number;
  precioUnitario: number;
}

interface Pedido {
  id: number;
  estado: string;
  total: number;
  items: PedidoItem[];
}

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './pedidos.component.html',
  styleUrls: ['./pedidos.component.scss']
})
export class PedidosComponent implements OnInit {
  private http = inject(HttpClient);
  pedidos: Pedido[] = [];

  ngOnInit(): void {
    const usuarioId = localStorage.getItem('usuarioId');
    if (!usuarioId) return;

    this.http.get<Pedido[]>(`${environment.apiUrl}/pedidos/usuario/${usuarioId}`).subscribe({
      next: (data) => this.pedidos = data,
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar tus pedidos.',
          confirmButtonColor: '#ef4444'
        });
      }
    });
  }
}
