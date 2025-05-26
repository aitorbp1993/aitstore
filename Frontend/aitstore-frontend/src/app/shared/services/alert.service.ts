import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  // Alerta simple (tipo alert)
  showAlert(title: string, text: string, icon: 'success' | 'error' | 'warning' | 'info' = 'info') {
    return Swal.fire({
      title,
      text,
      icon,
      confirmButtonText: 'OK'
    });
  }

  // Confirmación (tipo confirm)
  showConfirm(title: string, text: string, confirmButtonText = 'Sí', cancelButtonText = 'Cancelar') {
    return Swal.fire({
      title,
      text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText,
      cancelButtonText
    });
  }

  // Notificación tipo toast
  showToast(message: string, icon: 'success' | 'error' | 'info' = 'info') {
    return Swal.fire({
      toast: true,
      position: 'top-end',
      icon,
      title: message,
      showConfirmButton: false,
      timer: 2000,
      timerProgressBar: true
    });
  }
}
