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
  templateUrl: './floating-cart-button.component.html',
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
