import { ApplicationConfig } from '@angular/core';
import { provideRouter, Route } from '@angular/router';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withEnabledBlockingInitialNavigation),
    { provide: 'onSameUrlNavigation', useValue: 'reload' }
  ]
};
