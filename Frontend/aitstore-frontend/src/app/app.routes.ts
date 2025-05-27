import { Routes } from '@angular/router';

import { LayoutComponent } from './layout.component';
import { HomeComponent } from './pages/home.component';
import { CartComponent } from './pages/cart.component';
import { LoginComponent } from './auth/login.component';
import { RegisterComponent } from './auth/register.component';
import { PedidosComponent } from './pages/pedidos/pedidos.component';
import { OrderDetailComponent } from './pages/order-detail.component';
import { AdminLayoutComponent } from './layouts/admin-layout.component';
import { CategoriaProductosComponent } from './pages/categoria-productos.component';

import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  // 🧍‍♂️ Cliente - Layout general
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: HomeComponent },
      { path: 'carrito', component: CartComponent, canActivate: [authGuard] },
      { path: 'auth/login', component: LoginComponent },
      { path: 'auth/register', component: RegisterComponent },
      { path: 'pedidos', component: PedidosComponent, canActivate: [authGuard] },
      { path: 'pedidos/:id', component: OrderDetailComponent, canActivate: [authGuard] },
      {
        path: 'categoria/:id',
        loadComponent: () =>
          import('./pages/categoria-productos.component').then(m => m.CategoriaProductosComponent)
      },
      {
        path: 'perfil',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./pages/perfil.component').then(m => m.PerfilComponent)
      }
    ]
  },

  // 🛠 Admin - Panel administrativo (protegido por rol)
  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate: [adminGuard],
    children: [
      {
        path: 'productos',
        loadComponent: () =>
          import('./pages/admin/admin-productos.component').then(m => m.AdminProductosComponent)
      },
      {
        path: 'productos/crear',
        loadComponent: () =>
          import('./pages/admin/crear-producto.component').then(m => m.CrearProductoComponent)
      },
      {
        path: 'productos/editar/:id',
        loadComponent: () =>
          import('./pages/admin/editar-producto.component').then(m => m.EditarProductoComponent)
      },
      {
        path: 'usuarios',
        loadComponent: () =>
          import('./pages/admin/admin-usuarios.component').then(m => m.AdminUsuariosComponent)
      },
      {
        path: 'pedidos',
        loadComponent: () =>
          import('./pages/admin/admin-pedidos.component').then(m => m.AdminPedidosComponent)
      },
      {
        path: 'pedidos/:id',
        loadComponent: () =>
          import('./pages/admin/admin-pedido-detalle.component').then(m => m.AdminPedidoDetalleComponent)
      },
      {
        path: 'categoria/:id',
        component: CategoriaProductosComponent
      }
    ]
  },

  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];
