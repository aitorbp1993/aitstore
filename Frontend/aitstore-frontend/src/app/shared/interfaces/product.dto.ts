export interface ProductDTO {
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  imagenUrl?: string;
  categoriaId: number;
}
