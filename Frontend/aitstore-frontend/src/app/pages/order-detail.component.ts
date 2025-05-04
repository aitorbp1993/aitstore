import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment'; // ✅ Importación correcta

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.scss'
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
        error: () => alert('No se pudo cargar el pedido')
      });
    }
  }

  get total(): number {
    return this.pedido?.items?.reduce(
      (acc: number, item: any) => acc + item.precioUnitario * item.cantidad, 0
    ) || 0;
  }
}
