import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

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

  private notificacionSubject = new Subject<string>();
  notificacion$ = this.notificacionSubject.asObservable();

  private get clave(): string {
    const id = this.obtenerUsuarioId();
    return `carrito_usuario_${id > 0 ? id : 'anonimo'}`;
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
    const idx = carrito.findIndex(i => i.id === item.id);

    if (idx !== -1) {
      carrito[idx].cantidad += item.cantidad;
    } else {
      carrito.push(item);
    }

    this.guardarCarrito(carrito);
    this.notificacionSubject.next(`✅ ${item.nombre} añadido al carrito`);
  }

  eliminarItem(productoId: number): void {
    const actualizado = this.obtenerCarrito().filter(i => i.id !== productoId);
    this.guardarCarrito(actualizado);
  }

  vaciarCarrito(): void {
    this.guardarCarrito([]);
  }

  incrementarCantidad(productoId: number): void {
    const carrito = this.obtenerCarrito();
    const item = carrito.find(p => p.id === productoId);
    if (item) {
      item.cantidad++;
      this.guardarCarrito(carrito);
    }
  }

  disminuirCantidad(productoId: number): void {
    const carrito = this.obtenerCarrito();
    const idx = carrito.findIndex(p => p.id === productoId);
    if (idx !== -1) {
      if (carrito[idx].cantidad > 1) {
        carrito[idx].cantidad--;
      } else {
        carrito.splice(idx, 1);
      }
      this.guardarCarrito(carrito);
    }
  }

  recargarCarrito(): void {
    this.carritoSubject.next(this.cargarCarrito());
  }

  obtenerUsuarioId(): number {
    const idStr = localStorage.getItem('usuarioId');
    const id = Number(idStr);
    return isNaN(id) ? 0 : id;
  }
}
