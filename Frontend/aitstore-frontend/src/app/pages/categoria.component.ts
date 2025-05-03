import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../shared/services/carrito.service';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.scss']
})
export class CategoriaComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private router = inject(Router); // 👈 Para navegar
  private carritoService = inject(CarritoService); // 👈 Para añadir productos al carrito

  categoriaNombre: string = '';
  productos: any[] = [];
  cargando = true;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.cargarCategoria(Number(id));
      }
    });
  }

  cargarCategoria(id: number): void {
    this.cargando = true;
    this.http.get<any>(`http://localhost:8081/api/categorias/${id}`).subscribe({
      next: (res) => {
        this.categoriaNombre = res.nombre;
        this.productos = res.productos || [];
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando categoría:', err);
        this.cargando = false;
      }
    });
  }

  agregarAlCarrito(producto: any): void {
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
