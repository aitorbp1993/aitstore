import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProductResponseDTO } from '../../shared/interfaces/product-response.dto';
import { ProductService } from '../../shared/services/product.service';
import Swal from 'sweetalert2';

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
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron obtener los productos',
          confirmButtonColor: '#ef4444'
        });
      }
    });
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc2626',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.productService.deleteById(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Producto eliminado',
              showConfirmButton: false,
              timer: 1500
            });
            this.cargarProductos();
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'No se pudo eliminar',
              text: 'Puede que el producto esté asociado a un pedido.',
              confirmButtonColor: '#ef4444'
            });
          }
        });
      }
    });
  }

  editar(id: number): void {
    this.router.navigate(['/admin/productos/editar', id]);
  }

  crear(): void {
    this.router.navigate(['/admin/productos/crear']);
  }
}
