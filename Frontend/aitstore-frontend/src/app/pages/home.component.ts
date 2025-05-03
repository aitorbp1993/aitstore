import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CarritoService } from '../shared/services/carrito.service';

interface ProductoDTO {
  id: number;
  nombre: string;
  precio: number;
  imagenUrl?: string;
}

interface CategoriaConProductosDTO {
  id: number;
  nombre: string;
  productos: ProductoDTO[];
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  categoriasOriginal: CategoriaConProductosDTO[] = [];
  categoriasFiltradas = signal<CategoriaConProductosDTO[]>([]);

  ngOnInit(): void {
    this.http.get<CategoriaConProductosDTO[]>('http://localhost:8081/api/home/categorias-productos')
      .subscribe({
        next: (res) => {
          this.categoriasOriginal = res;
          this.aplicarFiltro(); // Aplicar filtro inicial si hay query param
        },
        error: (err) => console.error('Error cargando productos:', err)
      });

    this.route.queryParamMap.subscribe(() => this.aplicarFiltro());
  }

  aplicarFiltro(): void {
    const search = this.route.snapshot.queryParamMap.get('search')?.toLowerCase() || '';

    if (!search) {
      this.categoriasFiltradas.set(this.categoriasOriginal);
      return;
    }

    const resultado = this.categoriasOriginal
      .map(categoria => ({
        ...categoria,
        productos: categoria.productos.filter(producto =>
          producto.nombre.toLowerCase().includes(search)
        )
      }))
      .filter(categoria => categoria.productos.length > 0);

    this.categoriasFiltradas.set(resultado);
  }

  agregarAlCarrito(producto: ProductoDTO): void {
    this.carritoService.agregarItem({
      id: producto.id,
      nombre: producto.nombre,
      precio: producto.precio,
      cantidad: 1
    });
  }

  volverAlInicio(): void {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/']);
    });
  }
}
