import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { CarritoService } from '../shared/services/carrito.service';
import { environment } from '../../environments/environment';
import { ActivatedRoute } from '@angular/router';


interface ProductoDTO {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  imagenUrl: string;
}

interface CategoriaConProductosDTO {
  nombreCategoria: string;
  productos: ProductoDTO[];
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],

})
export class HomeComponent implements OnInit {
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);
  private route = inject(ActivatedRoute);


  categorias = signal<CategoriaConProductosDTO[]>([]);
  cargando = signal(true);
  productoSeleccionado: ProductoDTO | null = null;
  categoriaActiva: CategoriaConProductosDTO | null = null;
ngOnInit(): void {
  this.route.queryParams.subscribe(params => {
    const search = params['search'];
    if (search && search.trim().length > 0) {
      this.cargarFiltrados(search.trim());
    } else {
      this.cargarDatos();
    }
  });

  this.carritoService.recargarCarrito();
}
private cargarFiltrados(termino: string): void {
  this.cargando.set(true);
  this.http.get<CategoriaConProductosDTO[]>(`${environment.apiUrl}/home/categorias-productos?search=${encodeURIComponent(termino)}`)
    .subscribe({
      next: (res) => {
        this.categorias.set(res);
        this.cargando.set(false);
      },
      error: (err) => {
        console.error('Error buscando productos:', err);
        this.cargando.set(false);
      }
    });
}



  private cargarDatos(): void {
    this.http.get<CategoriaConProductosDTO[]>(`${environment.apiUrl}/home/categorias-productos`)
      .subscribe({
        next: (res) => {
          this.categorias.set(res);
          this.cargando.set(false);
        },
        error: (err) => {
          console.error('Error:', err);
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

  obtenerImagen(producto: ProductoDTO, categoriaPadre: string): string {
    this.categoriaActiva = this.categorias().find(c => c.nombreCategoria === categoriaPadre) || null;
    const url = producto.imagenUrl?.trim();
    const usaImagenDefault = !url || url.includes('placeholder') || url.includes('via.placeholder.com');
    return usaImagenDefault ? this.obtenerImagenPorCategoria(categoriaPadre) : url;
  }

  private obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase().trim();

    const mapeoImagenes: { [key: string]: string } = {
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

    for (const [key, value] of Object.entries(mapeoImagenes)) {
      if (nombre.includes(key)) return `assets/img/${value}`;
    }
    return 'assets/img/default.png';
  }
}
