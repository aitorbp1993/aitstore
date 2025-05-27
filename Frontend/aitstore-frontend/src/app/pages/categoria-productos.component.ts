// src/app/pages/categoria-productos.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../shared/services/carrito.service';
import { environment } from '../../environments/environment';
import Swal from 'sweetalert2';

interface ProductoDTO {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  imagenUrl: string;
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
  productoSeleccionado: ProductoDTO | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) this.cargarProductos(Number(id));
    });
  }

  private cargarProductos(id: number): void {
    this.cargando = true;
    this.http.get<any>(`${environment.apiUrl}/categorias/${id}/productos`).subscribe({
      next: (res) => {
        this.categoriaNombre = res.nombreCategoria;
        this.productos = res.productos.map((p: any) => ({
          id: p.id,
          nombre: p.nombre,
          descripcion: p.descripcion || 'Descripción no disponible',
          precio: p.precio,
          stock: p.stock || 0,
          imagenUrl: p.imagenUrl || ''
        }));
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los productos de esta categoría.',
          confirmButtonColor: '#ef4444'
        });
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
      icon: 'success',
      title: 'Producto añadido',
      text: `${producto.nombre} se ha añadido al carrito.`,
      timer: 1500,
      showConfirmButton: false
    });
  }

  obtenerImagen(producto: ProductoDTO): string {
    const url = producto.imagenUrl?.trim();
    if (!url || url.includes('placeholder')) {
      return this.obtenerImagenPorCategoria(this.categoriaNombre);
    }
    return url;
  }

  private obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase();
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

  volverInicio(): void {
    Swal.fire({
      title: '¿Volver a la página principal?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, volver',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#2563eb'
    }).then(result => {
      if (result.isConfirmed) {
        this.router.navigate(['/']);
      }
    });
  }
}
