import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { CarritoService } from '../shared/services/carrito.service';
import { environment } from '../../environments/environment';
import Swal from 'sweetalert2';

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
  }

  finalizarCompra(): void {
    const usuarioId = this.carritoService.obtenerUsuarioId();

    if (!usuarioId || usuarioId === 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Inicia sesión',
        text: '🔒 Debes iniciar sesión o registrarte para finalizar la compra.',
        confirmButtonColor: '#2563eb'
      });
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

    this.http.post(`${environment.apiUrl}/pedidos`, payload).subscribe({
      next: () => {
        this.vaciar();
        Swal.fire({
          icon: 'success',
          title: '¡Compra realizada!',
          text: 'Tu pedido ha sido generado con éxito.',
          showConfirmButton: false,
          timer: 1800
        });
        this.router.navigate(['/pedidos']);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error backend:', error);

        const msg = typeof error.error === 'string' ? error.error.toLowerCase() : error.error?.message?.toLowerCase();

        if (error.status === 403) {
          Swal.fire({
            icon: 'warning',
            title: 'Inicia sesión',
            text: '🔒 Debes iniciar sesión o registrarte para continuar.',
            confirmButtonColor: '#f59e0b'
          });
        } else if (error.status === 400 && msg?.includes('stock')) {
          Swal.fire({
            icon: 'error',
            title: 'Sin stock',
            text: '❌ No hay stock suficiente para uno o más productos del carrito.',
            confirmButtonColor: '#dc2626'
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error inesperado',
            text: '❗ Ocurrió un error al finalizar la compra.',
            confirmButtonColor: '#ef4444'
          });
        }
      }
    });
  }

  esMovil(): boolean {
    return window.innerWidth < 768;
  }

  cerrarModal(): void {
    if (this.esMovil()) {
      this.router.navigate(['/']);
    }
  }
}
