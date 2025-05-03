import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../shared/services/carrito.service';

interface ProductoDTO {
  id: number;
  nombre: string;
  precio: number;
  imagenUrl?: string;
}

@Component({
  selector: 'app-categoria-productos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './categoria-productos.component.html',
  styleUrls: ['./categoria-productos.component.scss']
})
export class CategoriaProductosComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);

  categoriaNombre = '';
  productos: ProductoDTO[] = [];
  cargando = true;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.cargarCategoriaConProductos(Number(id));
      }
    });
  }

  cargarCategoriaConProductos(id: number): void {
    this.cargando = true;
    this.http.get<any>(`http://localhost:8081/api/categorias/${id}/productos`).subscribe({
      next: (res) => {
        this.categoriaNombre = res.nombreCategoria;
        this.productos = res.productos || [];
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando productos de la categoría:', err);
        this.categoriaNombre = 'Categoría no encontrada';
        this.productos = [];
        this.cargando = false;
      }
    });
  }

  agregarAlCarrito(producto: ProductoDTO): void {
    this.carritoService.agregarItem({
      id: producto.id,
      nombre: producto.nombre,
      precio: producto.precio,
      cantidad: 1
    });
  }

  volverInicio(): void {
    this.router.navigate(['/']);
  }
}
