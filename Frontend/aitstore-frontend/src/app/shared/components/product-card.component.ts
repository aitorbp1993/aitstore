
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="w-[220px] bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow hover:shadow-lg transition-all duration-300 flex flex-col">
      <img [src]="producto.imagenUrl || obtenerImagenPorCategoria(producto.categoria)"
           alt="{{ producto.nombre }}"
           class="w-full h-40 object-cover rounded-t-xl" />

      <div class="p-4 flex flex-col flex-grow">
        <h3 class="text-base font-semibold text-gray-900 dark:text-white mb-1 truncate">
          {{ producto.nombre }}
        </h3>
        <p class="text-sm text-gray-600 dark:text-gray-400 truncate">
          {{ producto.descripcion || 'Producto sin descripción.' }}
        </p>
        <p class="text-green-600 dark:text-green-400 font-bold my-2">
          {{ producto.precio | currency:'EUR' }}
        </p>
        <button (click)="add.emit(producto)"
                class="mt-auto bg-blue-600 hover:bg-blue-700 text-white py-2 px-3 rounded-lg text-sm font-medium transition">
          Añadir al carrito
        </button>
      </div>
    </div>
  `
})
export class ProductCardComponent {
  @Input() producto: any;
  @Output() add = new EventEmitter<any>();

  obtenerImagenPorCategoria(nombreCategoria: string): string {
    // Lógica de imagen por categoría (puedes centralizarla)
    const base = 'assets/img/';
    const nombre = nombreCategoria?.toLowerCase() || 'generico';
    if (nombre.includes('portátil')) return base + 'portatil.jpg';
    if (nombre.includes('ratón')) return base + 'raton.jpg';
    if (nombre.includes('teclado')) return base + 'teclado.jpg';
    return base + 'generico.jpg';
  }
}
