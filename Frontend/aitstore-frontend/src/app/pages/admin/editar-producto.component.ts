import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-producto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './editar-producto.component.html',
  styleUrls: ['./editar-producto.component.scss']
})
export class EditarProductoComponent implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    descripcion: [''],
    precio: [0, [Validators.required, Validators.min(0.01)]],
    stock: [0, [Validators.required, Validators.min(0)]],
    imagenUrl: [''],
    categoria: ['', Validators.required]
  });

  productoId: number = 0;

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

  ngOnInit(): void {
    this.productoId = Number(this.route.snapshot.paramMap.get('id'));
    this.http.get<any>(`${environment.apiUrl}/productos/${this.productoId}`).subscribe({
      next: (producto) => {
        this.form.patchValue(producto);
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo cargar el producto.',
          confirmButtonColor: '#ef4444'
        });
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Revisa los campos obligatorios',
        confirmButtonColor: '#f59e0b'
      });
      return;
    }

    this.http.put(`${environment.apiUrl}/productos/${this.productoId}`, this.form.value).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Producto actualizado',
          showConfirmButton: false,
          timer: 1500
        });
        this.router.navigate(['/admin/productos']);
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo actualizar el producto.',
          confirmButtonColor: '#ef4444'
        });
      }
    });
  }
}
