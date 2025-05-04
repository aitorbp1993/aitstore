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
  obtenerImagen(producto: ProductoDTO): string {
    const url = producto.imagenUrl?.trim();
    if (!url || url.includes('placeholder') || url.startsWith('http') && url.includes('via.placeholder.com')) {
      return this.obtenerImagenPorCategoria(this.categoriaNombre);
    }
    return url;
  }


  obtenerImagenPorCategoria(nombreCategoria: string): string {
    const nombre = nombreCategoria.toLowerCase();

    if (nombre.includes('sobremesa')) return 'assets/img/desktop-default.png';
    if (nombre.includes('portátil')) return 'assets/img/laptop-default.png';
    if (nombre.includes('monitor')) return 'assets/img/monitor-default.png';
    if (nombre.includes('teclado')) return 'assets/img/keyboard-default.png';
    if (nombre.includes('ratón') || nombre.includes('ratones')) return 'assets/img/mouse-default.png';
    if (nombre.includes('placa base')) return 'assets/img/motherboard-default.png';
    if (nombre.includes('procesador') || nombre.includes('cpu')) return 'assets/img/cpu-default.png';
    if (nombre.includes('gráfica') || nombre.includes('gpu')) return 'assets/img/gpu-default.png';
    if (nombre.includes('ram')) return 'assets/img/ram-default.png';
    if (nombre.includes('disco') || nombre.includes('ssd')) return 'assets/img/ssd-default.png';
    if (nombre.includes('fuente')) return 'assets/img/psu-default.png';
    if (nombre.includes('caja') || nombre.includes('torre')) return 'assets/img/case-default.png';
    if (nombre.includes('refrigeración') || nombre.includes('ventilador')) return 'assets/img/cooling-default.png';
    if (nombre.includes('periférico') || nombre.includes('accesorio')) return 'assets/img/accessory-default.png';
    if (nombre.includes('silla') || nombre.includes('escritorio')) return 'assets/img/chair-default.png';

    return 'assets/img/default.png';
  }
}
