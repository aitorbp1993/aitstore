import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductResponseDTO } from '../interfaces/product-response.dto';
import { ProductDTO } from '../interfaces/product.dto';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly apiUrl = `${environment.apiUrl}/productos`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProductResponseDTO[]> {
    return this.http.get<ProductResponseDTO[]>(this.apiUrl);
  }

  getById(id: number): Observable<ProductResponseDTO> {
    return this.http.get<ProductResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(product: ProductDTO): Observable<ProductResponseDTO> {
    return this.http.post<ProductResponseDTO>(this.apiUrl, product);
  }

  update(id: number, product: ProductDTO): Observable<ProductResponseDTO> {
    return this.http.put<ProductResponseDTO>(`${this.apiUrl}/${id}`, product);
  }

  deleteById(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  searchByNombre(nombre: string): Observable<ProductDTO[]> {
  return this.http.get<ProductDTO[]>(`${this.apiUrl}?search=${encodeURIComponent(nombre)}`);
}

}
