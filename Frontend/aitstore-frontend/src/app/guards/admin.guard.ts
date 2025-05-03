import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (!token) {
    router.navigate(['/auth/login']);
    return false;
  }

  const payload = JSON.parse(atob(token.split('.')[1]));
  const rol = payload?.rol;

  if (rol === 'ADMIN') {
    return true;
  }

  router.navigate(['/']);
  return false;
};
