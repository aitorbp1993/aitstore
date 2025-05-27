import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CarritoService } from '../shared/services/carrito.service';
import { AuthService } from '../auth/auth.service';
import { environment } from '../../environments/environment';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

interface CategoriaDTO {
  id: number;
  nombre: string;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  private router = inject(Router);
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);

  categorias: CategoriaDTO[] = [];
  categoriasAgrupadas: { grupo: string, items: CategoriaDTO[] }[] = [];
  submenusAbiertos: { [grupo: string]: boolean } = {};

  searchTerm = '';
  cantidadTotal = 0;
  mensajePopup: string | null = null;

  menuAbierto = false;
  categoriasDesplegadas = false;
  perfilMenuAbierto = false;

  ngOnInit(): void {
    this.carritoService.recargarCarrito();

    this.carritoService.carrito$.subscribe(carrito => {
      this.cantidadTotal = carrito.reduce((acc, item) => acc + item.cantidad, 0);
    });

    this.carritoService.notificacion$.subscribe(msg => {
      this.mensajePopup = msg;
      setTimeout(() => this.mensajePopup = null, 2500);
    });

    this.cargarCategorias();
  }

  get autenticado(): boolean {
    return this.authService.isAuthenticated();
  }

  get nombreUsuario(): string {
    return this.authService.getNombre();
  }

  get inicial(): string {
    return this.nombreUsuario.charAt(0).toUpperCase();
  }

  toggleMenu(): void {
    this.menuAbierto = !this.menuAbierto;
    this.perfilMenuAbierto = false;
  }

  toggleCategorias(): void {
    this.categoriasDesplegadas = !this.categoriasDesplegadas;
  }

  togglePerfilMenu(): void {
    this.perfilMenuAbierto = !this.perfilMenuAbierto;
    this.menuAbierto = false;
  }

  toggleSubmenu(grupo: string): void {
    this.submenusAbiertos[grupo] = !this.submenusAbiertos[grupo];
  }

  cerrarMenus(): void {
    this.menuAbierto = false;
    this.perfilMenuAbierto = false;
    this.categoriasDesplegadas = false;
    this.submenusAbiertos = {};
  }

  buscar(): void {
    if (this.searchTerm.trim().length > 0) {
      this.router.navigate(['/'], { queryParams: { search: this.searchTerm.trim() } });
      this.cerrarMenus();
    }
  }

  irACategoria(id: number): void {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/categoria', id]);
      this.cerrarMenus();
    });
  }

  irAInicio(): void {
    this.router.navigate(['/']);
    this.cerrarMenus();
  }

  irAPerfil(): void {
    this.router.navigate(['/perfil']);
    this.cerrarMenus();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
    this.cerrarMenus();
  }

  private cargarCategorias(): void {
    this.http.get<CategoriaDTO[]>(`${environment.apiUrl}/categorias`).subscribe({
      next: res => {
        this.categorias = res;
        this.agruparCategorias(res);
      },
      error: err => console.error('Error cargando categorías:', err)
    });
  }

  private agruparCategorias(categorias: CategoriaDTO[]): void {
    const grupos: { [key: string]: string[] } = {
      'Ordenadores': ['portátiles', 'sobremesa'],
      'Pantallas': ['monitores'],
      'Periféricos': ['teclados', 'ratones'],
      'Componentes': ['cpu', 'procesadores', 'gpu', 'gráficas', 'placas base', 'ram', 'ssd', 'discos duros', 'fuentes', 'cajas'],
      'Refrigeración': ['refrigeración', 'ventiladores'],
      'Otros': ['accesorios', 'periféricos', 'sillas', 'escritorios']
    };

    const agrupadas: { [grupo: string]: CategoriaDTO[] } = {};

    categorias.forEach(cat => {
      const nombre = cat.nombre.toLowerCase();
      for (const grupo in grupos) {
        if (grupos[grupo].some(palabra => nombre.includes(palabra))) {
          if (!agrupadas[grupo]) agrupadas[grupo] = [];
          agrupadas[grupo].push(cat);
          return;
        }
      }
      if (!agrupadas['Otros']) agrupadas['Otros'] = [];
      agrupadas['Otros'].push(cat);
    });

    this.categoriasAgrupadas = Object.keys(agrupadas).map(grupo => ({
      grupo,
      items: agrupadas[grupo]
    }));
  }
}
