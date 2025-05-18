import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../shared/services/carrito.service';
import { environment } from '../../environments/environment';

interface ProductoDTO {
  id: number;
  nombre: string;
  precio: number;
  descripcion?: string;
  imagenUrl?: string;
  categoriaNombre?: string;
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
    this.cargarDatosCategoria();
  }

  private cargarDatosCategoria(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (!id) return;

      this.cargando = true;
      this.http.get<{nombreCategoria: string; productos: ProductoDTO[]}>(
        `${environment.apiUrl}/categorias/${id}/productos`
      ).subscribe({
        next: (res) => {
          this.categoriaNombre = res.nombreCategoria;
          this.productos = res.productos.map(p => ({
            ...p,
            categoriaNombre: res.nombreCategoria
          }));
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error:', err);
          this.categoriaNombre = 'Categoría no encontrada';
          this.productos = [];
          this.cargando = false;
        }
      });
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

  verDetalles(productoId: number): void {
    this.router.navigate(['/producto', productoId]);
  }

  volverInicio(): void {
    this.router.navigate(['/']);
  }

  obtenerImagen(producto: ProductoDTO): string {
    const url = producto.imagenUrl?.trim();
    const categoria = producto.categoriaNombre || this.categoriaNombre;

    if (!url || url.includes('placeholder')) {
      return this.obtenerImagenPorCategoria(categoria);
    }
    return url;
  }

  private obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase();
    const mapeo: {[key: string]: string} = {
      'sobremesa': 'desktop',
      'portátil': 'laptop',
      'monitor': 'monitor',
      'teclado': 'keyboard',
      'ratón': 'mouse',
      'ratones': 'mouse',
      'placa base': 'motherboard',
      'procesador': 'cpu',
      'cpu': 'cpu',
      'gráfica': 'gpu',
      'gpu': 'gpu',
      'ram': 'ram',
      'disco': 'ssd',
      'ssd': 'ssd',
      'fuente': 'psu',
      'caja': 'case',
      'torre': 'case',
      'refrigeración': 'cooling',
      'ventilador': 'cooling',
      'periférico': 'accessory',
      'accesorio': 'accessory',
      'silla': 'chair',
      'escritorio': 'chair'
    };

    for (const [keyword, image] of Object.entries(mapeo)) {
      if (nombre.includes(keyword)) {
        return `assets/img/${image}-default.png`;
      }
    }
    return 'assets/img/default.png';
  }
}
