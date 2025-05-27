import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  suscribirse(event: Event): void {
    event.preventDefault();
    Swal.fire({
      icon: 'success',
      title: '¡Suscripción confirmada!',
      text: 'Te has suscrito correctamente al boletín.',
      timer: 2000,
      showConfirmButton: false
    });
  }
}
