import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CarritoService } from '../shared/services/carrito.service';
import { environment } from '../../environments/environment';
import Swal from 'sweetalert2';
import { ProductCardComponent } from "../shared/components/product-card.component";

interface ProductoDTO {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  imagenUrl: string;
  categoria: string;
}

interface CategoriaConProductosDTO {
  id: number;
  nombre: string;
  productos: ProductoDTO[];
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, ProductCardComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);
  private route = inject(ActivatedRoute);

  categoriasConProductos = signal<CategoriaConProductosDTO[]>([]);
  destacados: ProductoDTO[] = []; // ← agregado para evitar el error
  cargando = signal(true);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const search = params['search']?.trim();
      if (search?.length > 0) {
        this.buscarProductos(search);
      } else {
        this.cargarCategoriasConProductos();
      }
    });

    this.carritoService.recargarCarrito();
  }

  private buscarProductos(termino: string): void {
    this.cargando.set(true);
    this.http.get<CategoriaConProductosDTO[]>(
      `${environment.apiUrl}/home/categorias-productos?search=${encodeURIComponent(termino)}`
    ).subscribe({
      next: res => {
        this.categoriasConProductos.set(res);
        this.cargando.set(false);
      },
      error: err => {
        console.error('Error buscando productos:', err);
        this.cargando.set(false);
      }
    });
  }

  private cargarCategoriasConProductos(): void {
    this.cargando.set(true);
    this.http.get<CategoriaConProductosDTO[]>(
      `${environment.apiUrl}/home/categorias-productos`
    ).subscribe({
      next: res => {
        this.categoriasConProductos.set(res);
        this.cargando.set(false);
      },
      error: err => {
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

  obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase().trim();

    const mapeo: { [clave: string]: string } = {
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

    for (const clave in mapeo) {
      if (nombre.includes(clave)) {
        return `assets/img/${mapeo[clave]}`;
      }
    }

    return 'assets/img/default.png';
  }
}
