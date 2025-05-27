// src/app/pages/pedido-detalle.component.ts
import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-pedido-detalle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedido-detalle.component.html',
  styleUrls: ['./pedido-detalle.component.scss']
})
export class PedidoDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private router = inject(Router);

  pedido: any = null;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.http.get(`${environment.apiUrl}/pedidos/${id}`).subscribe({
        next: (data) => this.pedido = data,
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Pedido no encontrado',
            text: 'No se pudo cargar el pedido. Redirigiendo...',
            timer: 2000,
            showConfirmButton: false
          });
          this.router.navigate(['/pedidos']);
        }
      });
    }
  }

  volver(): void {
    this.router.navigate(['/pedidos']);
  }
}
