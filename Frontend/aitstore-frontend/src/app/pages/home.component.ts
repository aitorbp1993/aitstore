// src/app/pages/home/home.component.ts
import {
  Component,
  OnInit,
  inject,
  signal
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { trigger, transition, animate, style } from '@angular/animations';
import Swal from 'sweetalert2';
import { CarritoService } from '../shared/services/carrito.service';
import { environment } from '../../environments/environment';

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

const IMAGENES_POR_CATEGORIA: { [key: string]: string } = {
  'portátil': 'laptop-default.png',
  'sobremesa': 'desktop-default.png',
  'monitor': 'monitor-default.png',
  'ratón': 'mouse-default.png',
  'teclado': 'keyboard-default.png',
  'procesador': 'cpu-default.png',
  'gráfica': 'gpu-default.png',
  'placa base': 'motherboard-default.png',
  'ssd': 'ssd-default.png',
  'ram': 'ram-default.png',
  'fuente': 'psu-default.png',
  'caja': 'case-default.png',
  'refrigeración': 'cooling-default.png',
  'silla': 'chair-default.png',
  'accesorio': 'accessory-default.png'
};

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('500ms ease-in', style({ opacity: 1 }))
      ])
    ]),
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('600ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ]),
    trigger('scaleIn', [
      transition(':enter', [
        style({ transform: 'scale(0.95)', opacity: 0 }),
        animate('400ms ease-out', style({ transform: 'scale(1)', opacity: 1 }))
      ])
    ])
  ]
})
export class HomeComponent implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly carritoService = inject(CarritoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  public categorias = signal<CategoriaConProductosDTO[]>([]);
  public cargando = signal(true);
  public productoSeleccionado: ProductoDTO | null = null;

  ngOnInit(): void {
    this.initDataLoading();
    this.carritoService.recargarCarrito();
  }

  public agregarAlCarrito(producto: ProductoDTO): void {
    this.carritoService.agregarItem({
      id: producto.id,
      nombre: producto.nombre,
      precio: producto.precio,
      cantidad: 1
    });

    Swal.fire({
      toast: true,
      position: 'top-end',
      icon: 'success',
      title: `${producto.nombre} añadido al carrito`,
      showConfirmButton: false,
      timer: 1600,
      timerProgressBar: true,
      background: '#1f2937',
      color: '#fff'
    });
  }

  public obtenerImagen(producto: ProductoDTO, categoriaPadre: string): string {
    const defaultImage = this.obtenerImagenPorCategoria(categoriaPadre);
    const tieneImagen = producto.imagenUrl?.trim() &&
      !producto.imagenUrl.includes('placeholder');
    return tieneImagen ? producto.imagenUrl : defaultImage;
  }

  public trackByCategory(index: number, categoria: CategoriaConProductosDTO): string {
    return categoria.nombreCategoria;
  }

  public trackByProduct(index: number, producto: ProductoDTO): number {
    return producto.id;
  }

  public onImageLoad(): void {}

  private obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase();
    for (const [key, value] of Object.entries(IMAGENES_POR_CATEGORIA)) {
      if (nombre.includes(key)) return `assets/img/${value}`;
    }
    return 'assets/img/default.png';
  }

  private initDataLoading(): void {
    this.route.queryParams.subscribe(params => {
      const search = params['search']?.trim();
      search ? this.cargarProductosFiltrados(search) : this.cargarTodosLosProductos();
    });
  }

  private cargarTodosLosProductos(): void {
    this.handleDataLoading(
      this.http.get<CategoriaConProductosDTO[]>(`${environment.apiUrl}/home/categorias-productos`)
    );
  }

  private cargarProductosFiltrados(termino: string): void {
    this.handleDataLoading(
      this.http.get<CategoriaConProductosDTO[]>(
        `${environment.apiUrl}/home/categorias-productos?search=${encodeURIComponent(termino)}`
      )
    );
  }

  private handleDataLoading(observable$: any): void {
    this.cargando.set(true);
    observable$.subscribe({
      next: (res: CategoriaConProductosDTO[]) => {
        this.categorias.set(res);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
      }
    });
  }
}
