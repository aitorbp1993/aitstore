import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ProductResponseDTO } from '../../shared/interfaces/product-response.dto';
import { ProductService } from '../../shared/services/product.service';

@Component({
  selector: 'app-admin-productos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-productos.component.html',
  styleUrls: ['./admin-productos.component.scss']
})
export class AdminProductosComponent implements OnInit {
  private productService = inject(ProductService);
  private router = inject(Router);

  productos: ProductResponseDTO[] = [];

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.productService.getAll().subscribe({
      next: (data) => this.productos = data,
      error: () => alert('Error al obtener productos')
    });
  }

  eliminar(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este producto?')) {
      this.productService.deleteById(id).subscribe({
        next: () => {
          alert('✅ Producto eliminado');
          this.cargarProductos();
        },
        error: (err) => {
          console.error('❌ Error al eliminar producto:', err);
          alert('No se pudo eliminar el producto. Puede que esté asociado a un pedido.');
        }
      });
    }
  }

  editar(id: number): void {
    this.router.navigate(['/admin/productos/editar', id]);
  }

  crear(): void {
    this.router.navigate(['/admin/productos/crear']);
  }
}
