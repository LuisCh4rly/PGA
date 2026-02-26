--Tabla de roles
INSERT INTO roles (id_role, name) VALUES
                                 (1, 'ADMIN'),
                                 (2, 'DOCENTE'),
                                 (3, 'ALUMNO');


--Tabla de usuarios
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, created_At, telefono, direccion, activo) VALUES ('MARISOL', 'HERRERA', 'SANDOVAL', current_timestamp, 5548769826, 'AV. LOMAS ESTRELLA 59', true),
                                                                                                                  ('JOSE ARMANDO', 'GUTIERRES', 'PERALTA', current_timestamp, 5512457836, 'OLIMPICA SUR 62', true),
                                                                                                                  ('KAREN', 'ATENOGENES', 'MATINEZ',current_timestamp, 5578945818, 'CALLE NIÑOS HEROES S/N', true),
                                                                                                                  ('NOEL', 'SANCHEZ', 'SALCEDO', current_timestamp,5585967425, 'FRANCISCO SARABIA', true),
                                                                                                                  ('MAURICIO SEBASTIAN', 'JURADO', 'PEREZ', current_timestamp, 5578549017, 'AV. DE LOS MAESTROS', true),
                                                                                                                  ('JULIO', 'MARTINEZ', 'LOPEZ', current_timestamp, 5545258679, 'MIRAMONTES 89', true),
                                                                                                                  ('JUAN', 'PEREZ', 'LOPEZ', current_timestamp, 5512345678, 'CALLE REFORMA 120', true),
                                                                                                                  ('ANA', 'GOMEZ', 'MARTINEZ', current_timestamp, 5523456789, 'AV. INSURGENTES 450', true),
                                                                                                                  ('CARLOS', 'RAMIREZ', 'TORRES', current_timestamp, 5534567890, 'CALLE JUAREZ 89', true),
                                                                                                                  ('LUISA', 'MENDOZA', 'CRUZ', current_timestamp, 5545678901, 'AV. UNIVERSIDAD 300', true),
                                                                                                                  ('MIGUEL', 'HERNANDEZ', 'FLORES', current_timestamp, 5556789012, 'CALLE ALLENDE 45', true),
                                                                                                                  ('SOFIA', 'CASTILLO', 'RIVERA', current_timestamp, 5567890123, 'AV. TLAHUAC 210', true),
                                                                                                                  ('DANIEL', 'ORTIZ', 'NAVARRO', current_timestamp, 5578901234, 'CALLE HIDALGO 67', true),
                                                                                                                  ('FERNANDA', 'ROJAS', 'SALINAS', current_timestamp, 5589012345, 'AV. AZTECAS 500', true),
                                                                                                                  ('JORGE', 'VARGAS', 'MEJIA', current_timestamp, 5590123456, 'CALLE PINO SUAREZ 150', true),
                                                                                                                  ('PAULA', 'SANTOS', 'LUNA', current_timestamp, 5511122233, 'AV. COYOACAN 88', true),
                                                                                                                  ('RICARDO', 'MORALES', 'AGUILAR', current_timestamp, 5522233344, 'CALLE MORELOS 34', true),
                                                                                                                  ('KAREN', 'DELGADO', 'PACHECO', current_timestamp, 5533344455, 'AV. CHURUBUSCO 410', true),
                                                                                                                  ('ALBERTO', 'NUNEZ', 'REYES', current_timestamp, 5544455566, 'CALLE ZARAGOZA 76', true),
                                                                                                                  ('VALERIA', 'IBARRA', 'MOLINA', current_timestamp, 5555566677, 'AV. PATRIOTISMO 260', true),
                                                                                                                  ('OSCAR', 'FUENTES', 'ROMERO', current_timestamp, 5566677788, 'CALLE TLAXCALA 19', true),
                                                                                                                  ('CARMEN', 'CRUZ', 'ROSAS', current_timestamp,5577788899, 'AV. DIVISION DEL NORTE 102', true),
                                                                                                                  ('IVAN', 'SILVA', 'CORONA', current_timestamp,5588899900, 'CALLE DURANGO 55', true),
                                                                                                                  ('PATRICIA', 'VEGA', 'CARRILLO', current_timestamp, 5599900011, 'AV. OBSERVATORIO 380', true),
                                                                                                                  ('EDUARDO', 'CORTES', 'MIRANDA', current_timestamp, 5510101010, 'CALLE TOLUCA 90', true);

--tabla de cuentas
INSERT INTO cuentas (password, role_id, usuario_id, email) VALUES
-- ADMIN (solo uno)
('$2a$12$kBtOBRVsElpkT1yoO9//XuFgA9evlQiu/KF0mG2dgXC6O0n9S89gW', 1, 1, 'admin@pga.com'),

-- DOCENTES (usuarios 2,3,4)
('$2a$10$X8n8KxZP8r7MZ1QF9z9xUu0x0YvR7Kp8Q0JzM5kQnYQk2sF9D2L7m', 2, 2, 'docente1@pga.com'),
('$2a$10$X8n8KxZP8r7MZ1QF9z9xUu0x0YvR7Kp8Q0JzM5kQnYQk2sF9D2L7m', 2, 3, 'docente2@pga.com'),
('$2a$10$X8n8KxZP8r7MZ1QF9z9xUu0x0YvR7Kp8Q0JzM5kQnYQk2sF9D2L7m', 2, 4, 'docente3@pga.com'),

-- ALUMNOS (usuarios 5 en adelante)
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 5, 'alumno5@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 6, 'alumno6@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 7, 'alumno7@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 8, 'alumno8@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 9, 'alumno9@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 10,'alumno10@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 11,'alumno11@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 12, 'alumno12@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 13, 'alumno13@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 14, 'alumno14@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 15, 'alumno15@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 16, 'alumno16@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 17, 'alumno17@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 18, 'alumno18@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 19, 'alumno19@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 20, 'alumno20@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 21, 'alumno21@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 22, 'alumno22@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 23, 'alumno23@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 24, 'alumno24@pga.com'),
('$2a$10$M4F5vZPZ8s9y6WQKJk7H2NQF9K0P3mZrD8vLQ6Hf7Ue1x5A9R0pK', 3, 25, 'alumno25@pga.com');



-- Tabla de Campo formativo
INSERT INTO campos_formativos (nombre, descripcion, activo) VALUES ('Arquitectura de APIs', 'Diseño de servicios REST y lógica de servidor en Java.', true),
                                                                   ('Gestion de Datos', 'Modelado SQL y optimización de bases de datos masivas.', true),
                                                                   ('Frontend Dinamico', 'Creación de interfaces reactivas con frameworks modernos.', true),
                                                                   ('Seguridad Web', 'Protección de datos, cifrado y control de acceso seguro.', true),
                                                                   ('DevOps y Despliegue', 'Automatización de nubes y gestión de contenedores Docker.', true),
                                                                   ('Pruebas de Software', 'Estrategias de testing, pruebas unitarias y automatizadas para garantizar calidad.', true),
                                                                   ('Arquitectura de Software', 'Diseño de sistemas escalables, patrones y buenas prácticas de arquitectura.', true),
                                                                   ('Metodologías Ágiles', 'Gestión de proyectos con Scrum, Kanban y mejora continua del trabajo en equipo.', true);


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
                                                                                      ('Variables de Entorno', 'Gestionar credenciales sensibles de forma segura en la nube.', true, 5),
                                                                                      ('Versionado de API', 'Implementar versiones v1 y v2 para mantener compatibilidad.', true, 1),
                                                                                      ('Mapeo con MapStruct', 'Convertir entidades a DTOs usando mapeo automático.', true, 1),
                                                                                      ('Normalización de Datos', 'Aplicar reglas de normalización para evitar redundancia.', true, 2),
                                                                                      ('Lazy Loading UI', 'Cargar componentes bajo demanda para mejorar rendimiento.', true, 3),
                                                                                      ('Rate Limiting', 'Limitar solicitudes por usuario para prevenir abuso del sistema.', true, 4);

--Tabla de Curso
INSERT INTO cursos (nombre, descripcion, fecha_alta, activo) VALUES ( 'Diseño de Interfaces','Curso de diseño de interfaces y experiencia de usuario',CURRENT_DATE,true),
                                                                    ( 'Programación Orientada a Objetos','Fundamentos de programación orientada a objetos con Java',CURRENT_DATE,true),
                                                                    ( 'Bases de Datos','Modelado, consultas SQL y diseño de bases de datos relacionales',CURRENT_DATE,true),
                                                                    ('Desarrollo Backend con Spring Boot', 'Creación de APIs REST, validaciones y seguridad con Spring Boot', CURRENT_DATE, true),
                                                                    ('Desarrollo Frontend con React', 'Construcción de interfaces dinámicas y manejo de estado en React', CURRENT_DATE, true),
                                                                    ('DevOps Básico', 'Introducción a Docker, CI/CD y despliegue de aplicaciones en la nube', CURRENT_DATE, true);
--Insertar actividades a un curso
INSERT INTO cursos_actividades (curso_id, actividad_base_id) VALUES (1, 9),
                                                                    (1, 10),
                                                                    (1, 11),
                                                                    (1, 12),
                                                                    (1, 23),
                                                                    (2, 1),
                                                                    (2, 2),
                                                                    (2, 3),
                                                                    (2, 4),
                                                                    (2, 22),
                                                                    (3, 5),
                                                                    (3, 6),
                                                                    (3, 7),
                                                                    (3, 8),
                                                                    (3, 25),
                                                                    (4, 1),
                                                                    (4, 2),
                                                                    (4, 3),
                                                                    (4, 13),
                                                                    (4, 14),
                                                                    (4, 15),
                                                                    (4, 21),
                                                                    (5, 9),
                                                                    (5, 10),
                                                                    (5, 11),
                                                                    (5, 12),
                                                                    (5, 24),
                                                                    (6, 17),
                                                                    (6, 18),
                                                                    (6, 19),
                                                                    (6, 20);


--Tabla Grupo
INSERT INTO grupos (nombre, estado,created_at, periodo, id_curso, id_usuario) VALUES ('Grupo1', 'HABILITADO',current_timestamp, 'Marzo-Septiembre', 1, 3),
                                                                                     ('Grupo2', 'HABILITADO',current_timestamp, 'Marzo-Septiembre', 2, 3),
                                                                                     ('Grupo3', 'HABILITADO', current_timestamp, 'Marzo-Septiembre', 3, 2),
                                                                                     ('Grupo4', 'HABILITADO', current_timestamp, 'Marzo-Septiembre', 4, 4),
                                                                                     ('Grupo5', 'HABILITADO', current_timestamp, 'Marzo-Septiembre', 5, 3);

-- Tabla inscripciones
INSERT INTO inscripciones
(fecha_inscripcion, fecha_inicio, fecha_fin, estado, escuela, nivel_estudio, carrera, tipo, id_usuario, id_grupo) VALUES (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UNAM', 'LICENCIATURA', 'Ingenieria en Sistemas', 'Servicio_Social', 5, 1),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'IPN', 'LICENCIATURA', 'Contaduria', 'Practicas_Profesionales', 6, 1),
                                                        (current_timestamp, '01/02/2026','01/02/2027', TRUE, 'CONALEP', 'TECNICO', 'Informatica', 'Jovenes_Construyendo_El_Futuro', 7, 1),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UAM', 'LICENCIATURA', 'Administracion', 'Servicio_Social', 8, 1),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'TESCI', 'LICENCIATURA', 'Industrial', 'Practicas_Profesionales', 9, 2),
                                                        (current_timestamp, '01/02/2026','01/02/2027', TRUE, 'CBTIS', 'TECNICO', 'Programacion', 'Jovenes_Construyendo_El_Futuro', 10, 2),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UAEM', 'LICENCIATURA', 'Derecho', 'Servicio_Social', 11, 2),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'IPN', 'LICENCIATURA', 'Mecatronica', 'Practicas_Profesionales', 12, 2),
                                                        (current_timestamp, '01/02/2026','01/02/2027', TRUE, 'UNAM', 'LICENCIATURA', 'Psicologia', 'Jovenes_Construyendo_El_Futuro', 13, 3),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UVM', 'LICENCIATURA', 'Mercadotecnia', 'Servicio_Social', 14, 3),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'UAEM', 'LICENCIATURA', 'Arquitectura', 'Practicas_Profesionales', 15, 3),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'IPN', 'LICENCIATURA', 'Electronica', 'Servicio_Social', 16, 3),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'UNAM', 'LICENCIATURA', 'Economia', 'Practicas_Profesionales', 17, 4),
                                                        (current_timestamp, '01/02/2026','01/02/2027', TRUE, 'CONALEP', 'TECNICO', 'Soporte Tecnico', 'Jovenes_Construyendo_El_Futuro', 18, 4),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UAM', 'LICENCIATURA', 'Biologia', 'Servicio_Social', 19, 4),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'UAEM', 'LICENCIATURA', 'Contaduria', 'Practicas_Profesionales', 20, 4),
                                                        (current_timestamp, '01/02/2026','01/02/2027', TRUE, 'CBTIS', 'TECNICO', 'Electronica', 'Jovenes_Construyendo_El_Futuro', 21, 5),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UNAM', 'LICENCIATURA', 'Ingenieria Civil', 'Servicio_Social', 22, 5),
                                                        (current_timestamp, '01/02/2026','01/07/2026', TRUE, 'IPN', 'LICENCIATURA', 'Logistica', 'Practicas_Profesionales', 23, 5),
                                                        (current_timestamp, '01/02/2026','01/02/2027', TRUE, 'UVM', 'LICENCIATURA', 'Negocios Internacionales', 'Jovenes_Construyendo_El_Futuro', 24, 5),
                                                        (current_timestamp, '01/02/2026','01/08/2026', TRUE, 'UAEM', 'LICENCIATURA', 'Ingenieria en Sistemas', 'Servicio_Social', 25, 5);


-- Precargar actividades por grupo (desde el curso)
INSERT INTO actividades_grupos (titulo,descripcion,fecha_asignacion,req_entrega,alcance,origen,id_grupo)
SELECT
    ab.titulo,
    ab.descripcion,
    CURRENT_TIMESTAMP,
    TRUE,
    'GRUPAL',
    'CURSO',
    g.id_grupo
FROM grupos g
         JOIN cursos_actividades ca
              ON ca.curso_id = g.id_curso
         JOIN actividades_base ab
              ON  ab.id_actividad = ca.actividad_base_id
WHERE g.estado = 'HABILITADO' AND ab.activo = true;



---Precargar actividades de alumno
INSERT INTO actividades_alumnos (estado_tarea, excento,motivo_exencion,Url_entrega, fecha_entrega, observaciones,  id_actividad_grupo, id_inscripcion)
SELECT
    'Sin_Iniciar',
    FALSE,
    null,
    null,
    null,
    null,
    ag.id_actividad_grupo,
    i.id_inscripcion

FROM inscripciones i
         JOIN actividades_grupos ag
              ON ag.id_grupo = i.id_grupo

WHERE i.estado = TRUE;

--Tabla documentos
INSERT INTO catalogo_documentos(tipo, nombre, activo, obligatorio)
VALUES ('CURP', 'CURP', TRUE,TRUE),
       ('CV', 'Curriculum Viate', TRUE, TRUE),
       ('CD', 'Comprobante de Domicilio', TRUE, TRUE),
       ('INE', 'Identificación Oficial', TRUE, TRUE),
       ('FOTO', 'Fotografía', TRUE,TRUE);

-- Crear Expedientes para usuarios con rol ALUMNO

WITH alumnos_sin_expediente AS (
    SELECT u.id_usuario
    FROM usuarios u
             JOIN cuentas c ON c.usuario_id = u.id_usuario
             LEFT JOIN expedientes e ON e.id_usuario = u.id_usuario
    WHERE c.role_id = 3
      AND u.activo = true
      AND e.id_expediente IS NULL
),

     expedientes_creados AS (
INSERT INTO expedientes (id_usuario, estado)
SELECT
    ase.id_usuario,
    'NO_APROBADO'
FROM alumnos_sin_expediente ase
    RETURNING id_expediente, id_usuario
)

INSERT INTO documentos_expedientes (
    id_expediente,
    id_documento,
    estado_documento
)
SELECT
    ec.id_expediente,
    d.id_documento,
    'PENDIENTE'
FROM expedientes_creados ec
         CROSS JOIN catalogo_documentos d
WHERE d.activo = true;



COMMIT ;

