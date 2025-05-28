// src/app/pages/cart.component.ts
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
    Swal.fire({
      title: '¿Vaciar carrito?',
      text: 'Se eliminarán todos los productos del carrito.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc2626',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, vaciar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.carritoService.vaciarCarrito();
        this.carrito = [];
        Swal.fire({
          icon: 'success',
          title: 'Carrito vacío',
          timer: 1500,
          showConfirmButton: false
        });
      }
    });
  }

  finalizarCompra(): void {
    const usuarioId = this.carritoService.obtenerUsuarioId();

   if (!usuarioId || usuarioId === 0) {
  Swal.fire({
    icon: 'warning',
    title: '🔐 Inicia sesión o regístrate',
    text: 'Necesitas una cuenta para finalizar tu compra.',
    showCancelButton: true,
    showDenyButton: true,
    confirmButtonText: 'Iniciar sesión',
    denyButtonText: 'Registrarse',
    cancelButtonText: 'Cancelar',
    confirmButtonColor: '#2563eb',
    denyButtonColor: '#10b981',
    cancelButtonColor: '#6b7280'
  }).then(result => {
    if (result.isConfirmed) {
      this.router.navigate(['/auth/login']);
    } else if (result.isDenied) {
      this.router.navigate(['/auth/register']);
    }
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

    Swal.fire({
      title: 'Procesando pedido...',
      didOpen: () => {
        Swal.showLoading();
      },
      allowOutsideClick: false,
      allowEscapeKey: false
    });

    this.http.post(`${environment.apiUrl}/pedidos`, payload).subscribe({
      next: () => {
        this.carritoService.vaciarCarrito();
        this.carrito = [];
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
        Swal.close();

        const msg = typeof error.error === 'string'
          ? error.error.toLowerCase()
          : error.error?.message?.toLowerCase();

        if (error.status === 403) {
          Swal.fire({
            icon: 'warning',
            title: 'Acceso denegado',
            text: '🔒 Debes iniciar sesión para continuar.',
            confirmButtonColor: '#f59e0b'
          });
        } else if (error.status === 400 && msg?.includes('stock')) {
          Swal.fire({
            icon: 'error',
            title: 'Sin stock suficiente',
            text: '❌ Uno o más productos del carrito no tienen suficiente stock.',
            confirmButtonColor: '#dc2626'
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error inesperado',
            text: '❗ Ocurrió un error al procesar la compra. Inténtalo de nuevo más tarde.',
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
