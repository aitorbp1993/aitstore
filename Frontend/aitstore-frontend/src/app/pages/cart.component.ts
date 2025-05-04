import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { CarritoService } from '../shared/services/carrito.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  private carritoService = inject(CarritoService);
  private http = inject(HttpClient);
  private router = inject(Router);

  carrito = this.carritoService.obtenerCarrito();
  mensajeError: string | null = null;

  ngOnInit(): void {
    this.carrito = this.carritoService.obtenerCarrito();
  }

  get total(): number {
    return this.carrito.reduce((acc, item) => acc + item.precio * item.cantidad, 0);
  }

  aumentar(id: number): void {
    this.carritoService.incrementarCantidad(id);
    this.carrito = this.carritoService.obtenerCarrito();
  }

  disminuir(id: number): void {
    this.carritoService.disminuirCantidad(id);
    this.carrito = this.carritoService.obtenerCarrito();
  }

  eliminarItem(productoId: number): void {
    this.carritoService.eliminarItem(productoId);
    this.carrito = this.carritoService.obtenerCarrito();
  }

  vaciar(): void {
    this.carritoService.vaciarCarrito();
    this.carrito = [];
    this.mensajeError = null;
  }

  finalizarCompra(): void {
    this.mensajeError = null;
    const usuarioId = this.carritoService.obtenerUsuarioId();

    if (!usuarioId || usuarioId === 0) {
      this.mensajeError = '🔒 Necesitas iniciar sesión o registrarte para finalizar la compra.';
      return;
    }

    const items = this.carrito.map(item => ({
      productoId: item.id,
      nombreProducto: item.nombre,
      cantidad: item.cantidad,
      precioUnitario: item.precio
    }));

    const payload = {
      usuarioId,
      estado: 'PENDIENTE',
      items
    };

    this.http.post('http://localhost:8081/api/pedidos', payload).subscribe({
      next: () => {
        this.vaciar();
        this.router.navigate(['/pedidos']);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error backend:', error);

        if (error.status === 403) {
          this.mensajeError = '🔒 Necesitas iniciar sesión o registrarte para finalizar la compra.';
        } else if (
          error.status === 400 &&
          (typeof error.error === 'string' && error.error.toLowerCase().includes('stock') ||
           typeof error.error?.message === 'string' && error.error.message.toLowerCase().includes('stock'))
        ) {
          this.mensajeError = '❌ No hay stock suficiente para uno o más productos del carrito.';
        } else {
          this.mensajeError = '❗ Ocurrió un error inesperado al finalizar la compra.';
        }
      }
    });
  }
}
