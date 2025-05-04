import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment'; // ✅ Importar entorno

@Component({
  selector: 'app-crear-producto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './crear-producto.component.html',
  styleUrls: ['./crear-producto.component.scss']
})
export class CrearProductoComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    descripcion: [''],
    precio: [0, [Validators.required, Validators.min(0.01)]],
    stock: [0, [Validators.required, Validators.min(0)]],
    imagenUrl: [''],
    categoria: ['', Validators.required]
  });

  categorias = [
    { id: 1, nombre: "Ordenadores de sobremesa" },
    { id: 2, nombre: "Portátiles" },
    { id: 3, nombre: "Monitores" },
    { id: 4, nombre: "Teclados" },
    { id: 5, nombre: "Ratones" },
    { id: 6, nombre: "Placas base" },
    { id: 7, nombre: "Procesadores (CPU)" },
    { id: 8, nombre: "Tarjetas gráficas (GPU)" },
    { id: 9, nombre: "Memoria RAM" },
    { id: 10, nombre: "Discos duros y SSD" },
    { id: 11, nombre: "Fuentes de alimentación" },
    { id: 12, nombre: "Cajas/Torres" },
    { id: 13, nombre: "Refrigeración líquida y ventiladores" },
    { id: 14, nombre: "Periféricos y accesorios" },
    { id: 15, nombre: "Sillas y escritorios gaming" }
  ];

  onSubmit(): void {
    if (this.form.invalid) {
      console.warn('Formulario inválido:', this.form.value);
      return;
    }

    const producto = this.form.value;

    this.http.post(`${environment.apiUrl}/productos`, producto).subscribe({
      next: () => {
        alert('✅ Producto creado correctamente');
        this.router.navigate(['/admin/productos']);
      },
      error: (err) => {
        console.error('❌ Error al crear el producto:', err);
        alert('Error al crear el producto');
      }
    });
  }
}
