import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);
  private elementRef = inject(ElementRef);

  categorias: any[] = [];
  categoriasAgrupadas: { [key: string]: any[] } = {};
  submenusAbiertos: { [grupo: string]: boolean } = {};
  categoriasDesplegadas = false;
  menuAbierto = false;
  perfilMenuAbierto = false;

  searchTerm: string = '';
  autenticado: boolean = false;
  inicial: string = '';
  cantidadTotal: number = 0;
  mensajePopup: string = '';

  ngOnInit(): void {
    this.autenticado = !!localStorage.getItem('token');
    const nombre = localStorage.getItem('nombre');
    if (nombre) this.inicial = nombre.charAt(0).toUpperCase();

    this.http.get<any[]>(`${environment.apiUrl}/categorias`).subscribe({
      next: res => {
        this.categorias = res;
        this.categoriasAgrupadas = this.agruparCategorias(res);
      },
      error: err => console.error('Error al cargar categorías', err)
    });

    const carrito = JSON.parse(localStorage.getItem('carrito') || '[]');
    this.cantidadTotal = carrito.reduce((total: number, item: any) => total + item.cantidad, 0);
  }

  agruparCategorias(categorias: any[]): { [key: string]: any[] } {
    const grupos: { [key: string]: string[] } = {
      'Ordenadores': ['portátiles', 'sobremesa'],
      'Pantallas': ['monitores'],
      'Periféricos': ['teclados', 'ratones'],
      'Componentes': ['cpu', 'procesadores', 'gpu', 'gráficas', 'placas base', 'ram', 'ssd', 'discos duros', 'fuentes', 'cajas'],
      'Refrigeración': ['refrigeración', 'ventiladores'],
      'Otros': ['accesorios', 'periféricos', 'sillas', 'escritorios']
    };

    const resultado: { [key: string]: any[] } = {};
    categorias.forEach(cat => {
      const nombre = cat.nombre.toLowerCase();
      for (const grupo in grupos) {
        if (grupos[grupo].some(palabra => nombre.includes(palabra))) {
          if (!resultado[grupo]) resultado[grupo] = [];
          resultado[grupo].push(cat);
          return;
        }
      }
      if (!resultado['Otros']) resultado['Otros'] = [];
      resultado['Otros'].push(cat);
    });

    return resultado;
  }

  toggleSubmenu(grupo: string) {
    this.submenusAbiertos[grupo] = !this.submenusAbiertos[grupo];
  }

  toggleMenu() {
    this.menuAbierto = !this.menuAbierto;
  }

  togglePerfilMenu() {
    this.perfilMenuAbierto = !this.perfilMenuAbierto;
  }

  cerrarMenus() {
    this.menuAbierto = false;
    this.perfilMenuAbierto = false;
    this.categoriasDesplegadas = false;
    this.submenusAbiertos = {};
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('nombre');
    localStorage.removeItem('carrito');
    this.router.navigate(['/auth/login']);
  }

  irACategoria(id: number) {
    this.cerrarMenus();
    this.router.navigate(['/categoria', id]);
  }

  irAInicio() {
    this.router.navigate(['/']);
  }

  irAPerfil() {
    this.router.navigate(['/perfil']);
  }

  buscar() {
    if (this.searchTerm.trim()) {
      this.router.navigate(['/'], { queryParams: { search: this.searchTerm.trim() } });
      this.searchTerm = '';
      this.cerrarMenus();
    }
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.perfilMenuAbierto = false;
      this.categoriasDesplegadas = false;
    }
  }

  get gruposCategorias(): string[] {
  return Object.keys(this.categoriasAgrupadas);
}

}
