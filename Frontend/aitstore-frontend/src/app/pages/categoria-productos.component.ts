import { Component, OnInit, inject, signal } from '@angular/core';
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
  productos = signal<ProductoDTO[]>([]);
  cargando = signal(true);
  productoSeleccionado: ProductoDTO | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) this.cargarDatosCategoria(id);
    });

    this.carritoService.recargarCarrito();
  }

  private cargarDatosCategoria(id: string): void {
    this.cargando.set(true);
    this.http.get<{ nombreCategoria: string; productos: ProductoDTO[] }>(
      `${environment.apiUrl}/categorias/${id}/productos`
    ).subscribe({
      next: res => {
        this.categoriaNombre = res.nombreCategoria;
        const productos = res.productos.map(p => ({
          ...p,
          categoriaNombre: res.nombreCategoria
        }));
        this.productos.set(productos);
        this.cargando.set(false);
      },
      error: err => {
        console.error('Error:', err);
        this.categoriaNombre = 'Categoría no encontrada';
        this.productos.set([]);
        this.cargando.set(false);
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

  obtenerImagen(producto: ProductoDTO): string {
    const url = producto.imagenUrl?.trim();
    const categoria = producto.categoriaNombre || this.categoriaNombre;
    if (!url || url.includes('placeholder') || url.includes('via.placeholder.com')) {
      return this.obtenerImagenPorCategoria(categoria);
    }
    return url;
  }

  private obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase().trim();
    const mapeo: { [key: string]: string } = {
      'sobremesa': 'desktop-default.png',
      'portátil': 'laptop-default.png',
      'monitor': 'monitor-default.png',
      'teclado': 'keyboard-default.png',
      'ratón': 'mouse-default.png',
      'ratones': 'mouse-default.png',
      'placa base': 'motherboard-default.png',
      'procesador': 'cpu-default.png',
      'cpu': 'cpu-default.png',
      'gráfica': 'gpu-default.png',
      'gpu': 'gpu-default.png',
      'ram': 'ram-default.png',
      'disco': 'ssd-default.png',
      'ssd': 'ssd-default.png',
      'fuente': 'psu-default.png',
      'caja': 'case-default.png',
      'torre': 'case-default.png',
      'refrigeración': 'cooling-default.png',
      'ventilador': 'cooling-default.png',
      'periférico': 'accessory-default.png',
      'accesorio': 'accessory-default.png',
      'silla': 'chair-default.png',
      'escritorio': 'chair-default.png'
    };

    for (const [key, value] of Object.entries(mapeo)) {
      if (nombre.includes(key)) return `assets/img/${value}`;
    }
    return 'assets/img/default.png';
  }
}
