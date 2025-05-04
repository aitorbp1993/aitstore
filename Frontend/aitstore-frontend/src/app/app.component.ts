import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'aitstore-frontend';
  mensaje = '';

  constructor(private http: HttpClient) {}

  probarCors(): void {
    this.http.get('https://aitstore-backend.onrender.com/api/test', { responseType: 'text' })
      .subscribe({
        next: (respuesta) => {
          this.mensaje = `✅ Respuesta: ${respuesta}`;
        },
        error: (err) => {
          this.mensaje = `❌ Error al llamar al backend: ${err.message || err.statusText}`;
          console.error('Error:', err);
        }
      });
  }
}
