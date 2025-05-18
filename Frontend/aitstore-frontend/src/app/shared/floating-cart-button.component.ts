import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CarritoService, CarritoItem } from './services/carrito.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-floating-cart-button',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="cantidadTotal$ | async as cantidad"
         class="fixed bottom-6 right-6 z-50">
      <button (click)="irAlCarrito()"
              class="w-14 h-14 rounded-full bg-blue-600 text-white shadow-lg flex items-center justify-center text-lg font-bold relative hover:bg-blue-700 transition-all">
        🛒
        <span *ngIf="cantidad > 0"
              class="absolute -top-2 -right-2 bg-red-600 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">
          {{ cantidad }}
        </span>
      </button>
    </div>
  `,
  styleUrls: ['./floating-cart-button.component.scss']
})
export class FloatingCartButtonComponent {
  private router = inject(Router);
  private carritoService = inject(CarritoService);

  cantidadTotal$: Observable<number> = this.carritoService.carrito$.pipe(
    map((items: CarritoItem[]) =>
      items.reduce((acc: number, item: CarritoItem) => acc + item.cantidad, 0)
    )
  );

  irAlCarrito(): void {
    this.router.navigate(['/carrito']);
  }
}
