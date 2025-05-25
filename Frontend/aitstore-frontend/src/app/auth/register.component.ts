import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  registerForm: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(6),
        Validators.pattern(/^\S*$/)
      ]
    ],
    direccion: ['', Validators.maxLength(255)],
    telefono: ['', [Validators.pattern(/^\d{9}$/)]]
  });

  errorMessage: string | null = null;

  onSubmit(): void {
    if (this.registerForm.invalid) return;

    this.http.post(`${environment.apiUrl}/auth/register`, this.registerForm.value)
      .subscribe({
        next: () => this.router.navigate(['/auth/login']),
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error?.message || 'Error al registrarse';
        }
      });
  }
}
