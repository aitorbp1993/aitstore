import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './shared/header.component';
import { FooterComponent } from './shared/footer.component';
import { FloatingCartButtonComponent } from "./shared/floating-cart-button.component";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent, FloatingCartButtonComponent],
  styleUrls: ['./layout.component.scss'],
  templateUrl: './layout.component.html'
})
export class LayoutComponent {
  toggleDarkMode() {
    document.documentElement.classList.toggle('dark');
  }
}
