import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../shared/header.component';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent],
  styleUrls: ['./admin-layout.component.scss'],
  template: `
    <app-header></app-header>
    <div class="p-4 bg-gray-100 dark:bg-gray-800 min-h-screen">
      <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold">Panel de Administración</h1>
        <button (click)="salirDelPanelAdmin()" class="text-blue-600 hover:underline">
          Volver a la tienda
        </button>
      </div>
      <router-outlet></router-outlet>
    </div>
  `
})
export class AdminLayoutComponent {
  constructor(private router: Router) {}

  salirDelPanelAdmin(): void {
    this.router.navigateByUrl('/');
  }
}
