# 🛒 AITStore

**AITStore** es una aplicación web de comercio electrónico desarrollada como Proyecto de Fin de Ciclo del Grado Superior en Desarrollo de Aplicaciones Web. Esta plataforma permite a los usuarios comprar productos tecnológicos y a los administradores gestionar el catálogo y usuarios a través de una interfaz moderna, segura y totalmente responsive.

---

## 📑 Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [URL del Despliegue](#url-del-despliegue)
- [Cuentas de Prueba](#cuentas-de-prueba)
- [Características Principales](#características-principales)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Capturas de Pantalla](#capturas-de-pantalla)
- [Cómo Ejecutar el Proyecto Localmente](#cómo-ejecutar-el-proyecto-localmente)
- [Seguridad](#seguridad)
- [Testing](#testing)
- [Futuras Mejoras](#futuras-mejoras)
- [Licencia](#licencia)

---

## 🧾 Descripción del Proyecto

AITStore resuelve la necesidad de contar con una tienda online profesional centrada en productos tecnológicos. Ofrece una experiencia fluida tanto para usuarios finales como para administradores.

- 🔍 **Público objetivo**: usuarios comunes que desean comprar productos y administradores encargados de la gestión.
- 📱 **Accesible desde**: ordenadores, tablets y móviles.

---

## 🛠️ Tecnologías Utilizadas

### 🔹 Frontend
- Angular 19
- TypeScript
- Tailwind CSS
- SweetAlert2
- Angular Router & Forms
- Angular Animations
- JWT (autenticación)

### 🔹 Backend
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- MapStruct
- JWT
- Swagger/OpenAPI
- Lombok

### 🔹 Base de Datos y DevOps
- MySQL (con MySQL Workbench)
- Railway (hosting base de datos)
- Docker (configuración local y producción)
- Render (despliegue frontend y backend)
- Postman (pruebas de API)

---

## 🌐 URL del Despliegue

- 🔗 **Frontend**: [https://aitstore-frontend.onrender.com](https://aitstore-frontend.onrender.com)
- 🔗 **Backend**: [https://aitstore-backend.onrender.com](https://aitstore-backend.onrender.com)

---

## 👤 Cuentas de Prueba

Para probar el sistema como usuario registrado:

```txt
Usuario: demo@gmail.com
Contraseña: 123456
```

---

## 📚 Características Principales

### 👥 Usuarios
- Registro e inicio de sesión
- Modificación del perfil
- Visualización de pedidos
- Modo oscuro

### 🛍️ Cliente
- Ver productos por categorías
- Buscar y filtrar
- Añadir productos al carrito
- Finalizar pedido

### 🧑‍💼 Administrador
- Gestión de productos: CRUD
- Gestión de usuarios
- Gestión de pedidos
- Panel independiente con layout propio

---

## 🏗️ Estructura del Proyecto

```bash
Frontend/
├── src/app/
│   ├── components/
│   ├── pages/
│   ├── services/
│   └── layout/
└── tailwind.config.js

Backend/
├── src/main/java/com/aitorbp/aitstore/
│   ├── auth/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── mapper/
│   ├── repository/
│   └── service/
└── resources/
    └── application.yml
```


## 🚀 Cómo Ejecutar el Proyecto Localmente

### 🔹 Backend

```bash
cd Backend
./mvnw spring-boot:run
```

### 🔹 Frontend

```bash
cd Frontend
npm install
ng serve
```

> La aplicación estará disponible en `http://localhost:4200` y el backend en `http://localhost:8081`

---

## 🔐 Seguridad

- Uso de JWT para proteger rutas
- Roles: `CLIENTE`, `ADMIN`
- Rutas del panel de administración protegidas
- Interceptor de Angular para añadir tokens

---

## 🧪 Testing

- Pruebas de endpoints con Postman
- Validación de formularios Angular
- Pruebas manuales de navegación, protección de rutas y errores
- Entorno de pruebas montado para asegurar coherencia funcional

---

## 📈 Futuras Mejoras

- Subida de imágenes personalizadas por producto
- Panel de estadísticas para administración
- Valoraciones de productos por parte del cliente
- Integración de pasarela de pago
- Notificaciones por email

---

## 📄 Licencia

Proyecto desarrollado por **Aitor Bartolomé Puertas** como Proyecto Final del Ciclo Superior en Desarrollo de Aplicaciones Web. Uso exclusivamente académico.

---
