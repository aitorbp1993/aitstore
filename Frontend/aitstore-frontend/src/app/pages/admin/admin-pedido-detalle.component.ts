import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-admin-pedido-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-pedido-detalle.component.html',
  styleUrls: ['./admin-pedido-detalle.component.scss']
})
export class AdminPedidoDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);

  pedido: any = null;
  cargando = true;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.http.get<any>(`${environment.apiUrl}${id}`).subscribe({
        next: res => this.pedido = res,
        error: err => console.error('Error al cargar pedido:', err),
        complete: () => this.cargando = false
      });
    }
  }
}
