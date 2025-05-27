import {
  Component,
  OnInit,
  inject,
  signal,
  ViewChildren,
  QueryList,
  ElementRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { trigger, transition, animate, style, query, stagger } from '@angular/animations';
import Swal from 'sweetalert2';
// Services
import { CarritoService } from '../shared/services/carrito.service';

// Environments
import { environment } from '../../environments/environment';

// Interfaces
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

// Image mapping configuration
const IMAGENES_POR_CATEGORIA: { [key: string]: string } = {
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

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('600ms cubic-bezier(0.4, 0, 0.2, 1)',
          style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ]),
    trigger('staggerFade', [
      transition(':enter', [
        query(':enter', [
          style({ opacity: 0, transform: 'translateY(20px)' }),
          stagger(100, [
            animate('400ms cubic-bezier(0.4, 0, 0.2, 1)',
              style({ opacity: 1, transform: 'translateY(0)' }))
          ])
        ])
      ])
    ]),
    trigger('scaleIn', [
      transition(':enter', [
        style({ transform: 'scale(0.95)', opacity: 0 }),
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)',
          style({ transform: 'scale(1)', opacity: 1 }))
      ])
    ]),
    trigger('modalAnimation', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('200ms ease-out', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('150ms ease-in', style({ opacity: 0 }))
      ])
    ])
  ]
})
export class HomeComponent implements OnInit {
  // Dependencies
  private readonly http = inject(HttpClient);
  private readonly carritoService = inject(CarritoService);
  private readonly route = inject(ActivatedRoute);

  // Template References
  @ViewChildren('scrollContainer') scrollContainers!: QueryList<ElementRef>;

  // State signals
  public categorias = signal<CategoriaConProductosDTO[]>([]);
  public cargando = signal(true);
  public productoSeleccionado: ProductoDTO | null = null;

  ngOnInit(): void {
    this.initDataLoading();
    this.carritoService.recargarCarrito();
  }

  // Public Methods
  public scrollRight(categoria: CategoriaConProductosDTO): void {
    this.handleScroll(categoria, 400);
  }

  public scrollLeft(categoria: CategoriaConProductosDTO): void {
    this.handleScroll(categoria, -400);
  }

  public onImageLoad(): void {
    // Placeholder para futuras implementaciones
  }

  public trackByCategory(index: number, categoria: CategoriaConProductosDTO): string {
    return categoria.nombreCategoria;
  }

  public trackByProduct(index: number, producto: ProductoDTO): number {
    return producto.id;
  }

  public agregarAlCarrito(producto: ProductoDTO): void {
    this.carritoService.agregarItem({
      id: producto.id,
      nombre: producto.nombre,
      precio: producto.precio,
      cantidad: 1
    });

    this.mostrarNotificacionCarrito(producto.nombre);
  }

  public obtenerImagen(producto: ProductoDTO, categoriaPadre: string): string {
    const defaultImage = this.obtenerImagenPorCategoria(categoriaPadre);
    const hasCustomImage = producto.imagenUrl?.trim() &&
                         !producto.imagenUrl.includes('placeholder') &&
                         !producto.imagenUrl.includes('via.placeholder.com');

    return hasCustomImage ? producto.imagenUrl : defaultImage;
  }

  // Private Methods
  private initDataLoading(): void {
    this.route.queryParams.subscribe(params => {
      const searchTerm = params['search']?.trim();
      searchTerm ? this.cargarProductosFiltrados(searchTerm) : this.cargarTodosLosProductos();
    });
  }

  private handleScroll(categoria: CategoriaConProductosDTO, scrollAmount: number): void {
    const categoryIndex = this.categorias().indexOf(categoria);
    const scrollElement = this.scrollContainers.toArray()[categoryIndex]?.nativeElement;

    if (scrollElement) {
      scrollElement.scrollBy({
        left: scrollAmount,
        behavior: 'smooth'
      });
    }
  }

  private mostrarNotificacionCarrito(productName: string): void {
    Swal.fire({
      toast: true,
      position: 'top-end',
      icon: 'success',
      title: `${productName} añadido al carrito`,
      showConfirmButton: false,
      timer: 1600,
      timerProgressBar: true,
      background: '#1f2937',
      color: '#fff'
    });
  }

  private obtenerImagenPorCategoria(nombreCategoria: string): string {
    const normalizedCategory = nombreCategoria.toLowerCase().trim();

    for (const [key, value] of Object.entries(IMAGENES_POR_CATEGORIA)) {
      if (normalizedCategory.includes(key)) return `assets/img/${value}`;
    }

    return 'assets/img/default.png';
  }

  private cargarProductosFiltrados(termino: string): void {
    this.handleDataLoading(
      this.http.get<CategoriaConProductosDTO[]>(
        `${environment.apiUrl}/home/categorias-productos?search=${encodeURIComponent(termino)}`
      )
    );
  }

  private cargarTodosLosProductos(): void {
    this.handleDataLoading(
      this.http.get<CategoriaConProductosDTO[]>(
        `${environment.apiUrl}/home/categorias-productos`
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
      error: (err: any) => {
        console.error('Error loading data:', err);
        this.cargando.set(false);
      }
    });
  }
}
