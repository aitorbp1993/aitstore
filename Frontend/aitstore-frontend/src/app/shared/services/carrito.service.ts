import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface CarritoItem {
  id: number;
  nombre: string;
  precio: number;
  cantidad: number;
}

@Injectable({ providedIn: 'root' })
export class CarritoService {
  private carritoSubject = new BehaviorSubject<CarritoItem[]>(this.cargarCarrito());
  carrito$ = this.carritoSubject.asObservable();

  // Clave dinámica basada en el usuario
  private get clave(): string {
    const id = this.obtenerUsuarioId();
    return `carrito_usuario_${id || 'anonimo'}`;
  }

  private cargarCarrito(): CarritoItem[] {
    const datos = localStorage.getItem(this.clave);
    return datos ? JSON.parse(datos) : [];
  }

  private guardarCarrito(carrito: CarritoItem[]): void {
    localStorage.setItem(this.clave, JSON.stringify(carrito));
    this.carritoSubject.next(carrito);
  }

  obtenerCarrito(): CarritoItem[] {
    return this.carritoSubject.value;
  }

  agregarItem(item: CarritoItem): void {
    const carrito = this.obtenerCarrito();
    const index = carrito.findIndex(i => i.id === item.id);

    if (index !== -1) {
      carrito[index].cantidad += item.cantidad;
    } else {
      carrito.push(item);
    }

    this.guardarCarrito(carrito);
  }

  eliminarItem(productoId: number): void {
    const actualizado = this.obtenerCarrito().filter(item => item.id !== productoId);
    this.guardarCarrito(actualizado);
  }

  vaciarCarrito(): void {
    this.guardarCarrito([]);
  }

  obtenerUsuarioId(): number {
    const id = localStorage.getItem('usuarioId');
    return id ? parseInt(id, 10) : 0;
  }

  incrementarCantidad(productoId: number): void {
    const carrito = this.obtenerCarrito();
    const item = carrito.find(p => p.id === productoId);
    if (item) {
      item.cantidad += 1;
      this.guardarCarrito(carrito);
    }
  }

  disminuirCantidad(productoId: number): void {
    const carrito = this.obtenerCarrito();
    const index = carrito.findIndex(p => p.id === productoId);
    if (index !== -1) {
      if (carrito[index].cantidad > 1) {
        carrito[index].cantidad -= 1;
      } else {
        carrito.splice(index, 1);
      }
      this.guardarCarrito(carrito);
    }
  }

  recargarCarrito(): void {
    const nuevo = this.cargarCarrito();
    this.carritoSubject.next(nuevo);
  }
}
