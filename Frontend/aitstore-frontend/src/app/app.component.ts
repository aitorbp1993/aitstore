import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FloatingCartButtonComponent } from './shared/floating-cart-button.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FloatingCartButtonComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'aitstore-frontend';
}
