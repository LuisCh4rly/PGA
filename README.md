#  Plataforma de Gestión Web - Backend (Spring Boot)
Este repositorio contiene la API REST y la lógica de negocio principal de la plataforma, 
desarrollada bajo un entorno de arquitectura empresarial utilizando **Java** y **Spring Boot**. 
El sistema está diseñado para ser escalable, seguro y fácil de mantener, aplicando las mejores prácticas.

## Herramientas :
*  **Lenguaje:** Java 21.0.7
* **Gestor de Dependencias:** Maven 3.9.12
* **Base de Datos:** PostgreSQL 17
* **Framework Principal:** Spring Boot 4.0.1
* **Persistencia de Datos:** Hibernate / JPA
* **Seguridad:** Spring Security & JWT
* **Pruebas de API:** Postman

## Arquitectura
El backend está estructurado bajo una **arquitectura limpia en capas**, garantizando la separación de responsabilidades:

1.  **Controller Layer (`@RestController`):** Se encarga de exponer los endpoints de la API REST, manejar las peticiones HTTP y las respuestas.
2.  **Service Layer (`@Service`):** Contiene el núcleo de la aplicación y toda la lógica de negocio.
3.  **Repository Layer (`@Repository`):** Capa de abstracción de datos utilizando Spring Data JPA para la comunicación fluida con la base de datos.
4.  **DTOs (Data Transfer Objects):** Implementados para transferir información entre el cliente y el servidor de forma segura, evitando exponer directamente las entidades de la base de datos.

## Variables de Entorno
Para poder ejecutar el sistema con éxito es necesario realizar la configuración de la base de datos, las variables de entorno y los parámetros de conexión importantes de la aplicación.
Las variables de entorno son las siguientes:
1. **JWT_SECRET** Contraseña secreta para generar y validar tokens JWT.
2. **JWT_EXPIRATION** Tiempo de expiración del token JWT en milisegundos.
3. **PASSWORD_MAIL** Contraseña de aplicación utilizada para el envío de correos electrónicos.

## Instalación
Es necesario clonar el repositorio correspondiente y configurar previamente la base de datos PostgreSQL en conjunto con las variables de entorno requeridas. 
Los pasos son los siguientes:
**Clonar el repositorio del backend.**
**Acceder a la carpeta del proyecto.**
**Configurar archivo application.properties.**
**Definir las variables de entorno requeridas.**
**Compilar y levantar la aplicación.**
**Una vez levantado, el backend estará disponible en : http://localhost:8080** 
