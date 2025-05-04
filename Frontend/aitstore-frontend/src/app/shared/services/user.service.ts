import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PedidoItemDTO {
  productoNombre: string;
  cantidad: number;
  precioUnitario: number;
}

export interface PedidoDTO {
  id: number;
  fechaCreacion: string;
  total: number;
  estado: string;
  items: PedidoItemDTO[];
}

export interface UsuarioDTO {
  id: number;
  nombre: string;
  email: string;
  rol: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  getUsuario(id: number): Observable<UsuarioDTO> {
    return this.http.get<UsuarioDTO>(`${this.baseUrl}/usuarios/${id}`);
  }

  getPedidosDeUsuario(id: number): Observable<PedidoDTO[]> {
    return this.http.get<PedidoDTO[]>(`${this.baseUrl}/pedidos/usuario/${id}`);
  }
}
