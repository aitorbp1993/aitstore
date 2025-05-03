import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

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

    this.http.get<Pedido[]>(`http://localhost:8081/api/pedidos/usuario/${usuarioId}`)
      .subscribe({
        next: (data) => this.pedidos = data,
        error: () => alert('Error al cargar pedidos')
      });
  }
}
