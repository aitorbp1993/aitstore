import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

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
    this.http.get(`http://localhost:8081/api/pedidos/${id}`).subscribe({
      next: (data) => this.pedido = data,
      error: () => alert('No se pudo cargar el pedido')
    });
  }

  get total(): number {
    return this.pedido?.items?.reduce(
      (acc: number, item: any) => acc + item.precioUnitario * item.cantidad, 0
    ) || 0;
  }
}
