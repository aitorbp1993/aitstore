// src/app/pages/order-detail.component.ts
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);

  pedido: any = null;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.http.get(`${environment.apiUrl}/pedidos/${id}`).subscribe({
        next: (data) => this.pedido = data,
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo cargar el pedido',
            confirmButtonColor: '#ef4444'
          });
        }
      });
    }
  }

  get total(): number {
    return this.pedido?.items?.reduce(
      (acc: number, item: any) => acc + item.precioUnitario * item.cantidad, 0
    ) || 0;
  }
}
