--Tabla de usuarios
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, created_At,  email, telefono, direccion, activo) VALUES ('MARISOL', 'HERRERA', 'SANDOVAL', current_timestamp, 'MARISOLHERRERA@EMAIL.COM', 5548769826, 'AV. LOMAS ESTRELLA 59', true),
                                                                                                   ('JOSE ARMANDO', 'GUTIERRES', 'PERALTA', current_timestamp,'JOSEPERALTA@EMAIL.COM', 5512457836, 'OLIMPICA SUR 62', true),
                                                                                                   ('KAREN', 'ATENOGENES', 'MATINEZ',current_timestamp,'KARENMARTINEZ@EMAIL.COM', 5578945818, 'CALLE NIÑOS HEROES S/N', true),
                                                                                                   ('NOEL', 'SANCHEZ', 'SALCEDO', current_timestamp,'NOELSANCHEZ@EMAIL.COM', 5585967425, 'FRANCISCO SARABIA', true),
                                                                                                   ('JULIO', 'MARTINEZ', 'LOPEZ', current_timestamp,'JULIOLOPEZ@EMAIL.COM', 5545258679, 'MIRAMONTES 89', true);
-- Tabla de Campo formativo
INSERT INTO campos_formativos (nombre, descripcion, activo) VALUES ('Arquitectura de APIs', 'Diseño de servicios REST y lógica de servidor en Java.', true),
                                                                   ('Gestion de Datos', 'Modelado SQL y optimización de bases de datos masivas.', true),
                                                                   ('Frontend Dinamico', 'Creación de interfaces reactivas con frameworks modernos.', true),
                                                                   ('Seguridad Web', 'Protección de datos, cifrado y control de acceso seguro.', true),
                                                                   ('DevOps y Despliegue', 'Automatización de nubes y gestión de contenedores Docker.', true);
--Tabla de Docentes
INSERT INTO docentes (fecha_alta, activo, id_user) VALUES (CURRENT_TIMESTAMP, TRUE, 1),
                                                          (CURRENT_TIMESTAMP, TRUE, 2);
-- Tabla de Activides
INSERT INTO actividades_base (titulo, descripcion, activo, id_campo_formativo) VALUES ('Crear Endpoint REST', 'Desarrollo de una ruta POST para recibir archivos JSON.', true, 1),
                                                                                      ('Validar DTOs', 'Implementación de validaciones de entrada en Java Spring.', true, 1),
                                                                                      ('Manejo de Excepciones', 'Crear un controlador global para errores HTTP en la API.', true, 1),
                                                                                      ('Documentar con Swagger', 'Generar la documentación técnica de los endpoints en Java.', true, 1),
                                                                                      ('Diseñar Modelo ER', 'Creación del diagrama entidad relación para el proyecto.', true, 2),
                                                                                      ('Script de Carga SQL', 'Escribir comandos INSERT masivos para poblar la base.', true, 2),
                                                                                      ('Optimizar Consultas', 'Uso de índices en Postgres para búsquedas más rápidas.', true, 2),
                                                                                      ('Backups Automatizados', 'Programar respaldos periódicos de la base de datos SQL.', true,  2),
                                                                                      ('Consumo de API', 'Uso de Fetch API para mostrar datos en tablas dinámicas.', true, 3),
                                                                                      ('Componente de Carga', 'Crear un botón que dispare el proceso de importación.', true, 3),
                                                                                      ('Estado Global Redux', 'Gestionar datos compartidos entre múltiples componentes.', true, 3),
                                                                                      ('Diseño Responsivo', 'Adaptar la interfaz para móviles usando CSS Grid o Flex.', true, 3),
                                                                                      ('Configurar JWT', 'Implementar tokens para proteger las rutas del backend.', true, 4),
                                                                                      ('Sanitizar Entradas', 'Evitar inyección SQL validando caracteres especiales.', true, 4),
                                                                                      ('Encriptar Passwords', 'Uso de BCrypt en Java para guardar claves de forma segura.', true, 4),
                                                                                      ('Politicas CORS', 'Configurar el acceso restringido a dominios conocidos.', true, 4),
                                                                                      ('Dockerizar App', 'Crear una imagen de Docker para la API de Java y Postgres.', true, 5),
                                                                                      ('Configurar Railway', 'Despliegue automático de la base de datos en la nube.', true, 5),
                                                                                      ('Monitorizar Logs', 'Implementar rastreo de errores en el servidor de producción.', true, 5),
                                                                                      ('Variables de Entorno', 'Gestionar credenciales sensibles de forma segura en la nube.', true, 5);
COMMIT;