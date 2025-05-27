import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from "./shared/footer.component";
import { FloatingCartButtonComponent } from "./shared/floating-cart-button.component";
import { HeaderComponent } from "./shared/header.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FooterComponent, FloatingCartButtonComponent, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'aitstore-frontend';

  // Estado del modo oscuro
  isDarkMode: boolean = false;

  constructor() {
    // Cargar preferencia desde localStorage al iniciar
    const saved = localStorage.getItem('darkMode');
    this.isDarkMode = saved === 'true';
    this.aplicarModo();
  }

  // Alternar entre modo claro y oscuro
  toggleDarkMode(): void {
    this.isDarkMode = !this.isDarkMode;
    localStorage.setItem('darkMode', String(this.isDarkMode));
    this.aplicarModo();
  }

  // Aplicar la clase dark al <html>
  aplicarModo(): void {
    const root = document.documentElement;
    if (this.isDarkMode) {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
  }
}
