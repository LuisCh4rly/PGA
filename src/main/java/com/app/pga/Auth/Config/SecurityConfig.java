package com.app.pga.Auth.Config;

import com.app.pga.App.Exception.ApiError;
import com.app.pga.Auth.JWT.JWTAuthFilter;
import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Security.UserDetailsServiceImp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsService; //carga datos desde la base de datos
    private final JWTService jwtService; //generacion y validacion de tokens
    private final JWTAuthFilter jwtAuthenticationFilter; //fitro de validacion del token




    //codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Usa tu UserDetailsServiceImpl para autenticar usuarios cargando desde la base de datos.	Reemplaza el login clásico de Spring.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    //Authentication Manager
    //Spring lo utiliza  para autenticar usuarios manualmente.
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    //Filtro de seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desactivar CSRF (porque usamos JWT, se activa en caso de formularios)
                .csrf(csrf -> csrf.disable())

                // Definir qué rutas son públicas o privadas
                .authorizeHttpRequests(auth -> auth
                        //  RUTAS PÚBLICAS
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")
                        .requestMatchers("/api/auth/usuario-actual").hasAnyRole("DOCENTE", "ALUMNO", "ADMIN")
                        .requestMatchers("/api/auth/cambiar-password").hasAnyRole("DOCENTE", "ALUMNO", "ADMIN")
                        //CURSO
                        .requestMatchers(HttpMethod.GET, "/api/curso/activos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/curso/listPage").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/curso/habilitarDeshabilitar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/curso/*/asignarActividades").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/curso/*/quitarActividades/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/curso/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/curso/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/curso").hasRole("ADMIN")

                        //GRUPO
                        .requestMatchers(HttpMethod.GET, "/api/grupo/Activo/porDocente/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/grupo/porDocente/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/grupo/porCurso/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/grupo/listPage").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/grupo/activos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/grupo/habilitarDeshabilitar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/grupo/*/cambiarDocente/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/grupo/*").hasAnyRole("ADMIN", "DOCENTE")
                        .requestMatchers(HttpMethod.POST, "/api/grupo").hasRole("ADMIN")

                        //USUARIO
                        .requestMatchers(HttpMethod.GET, "/api/usuario/docentes-activos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuario/alumnos-activos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/desactivar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuario/*").hasAnyRole("ADMIN", "DOCENTE", "ALUMNO")
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/*").hasAnyRole("ADMIN", "DOCENTE", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/api/usuario").hasRole("ADMIN")

                        //INSCRIPCIONES

                        //.requestMatchers(HttpMethod.GET, "/api/inscripcion/activos" ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/inscripcion/grupo/*/alumnos/general").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/inscripcion/grupo/*/alumnos").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/inscripcion/grupoAlumno/*").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.GET,"/api/inscripcion/gruposAlumno/*").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.PUT,"/api/inscripcion/*/asignarGrupo/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/inscripcion/desactivar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/inscripcion/*").hasAnyRole("ADMIN", "DOCENTE", "ALUMNO")
                        .requestMatchers(HttpMethod.GET,"/api/inscripcion").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/inscripcion").hasRole("ADMIN")

                        //CAMPOS FORMATIVOS
                        .requestMatchers(HttpMethod.GET, "/api/campo/activos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/campo/habilitarDeshabilitar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/campo/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/campo/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/campo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/campo").hasRole("ADMIN")

                        //ACTIVIDAD BASE
                        .requestMatchers(HttpMethod.GET,"/api/actividadBase/activas").hasAnyRole("ADMIN", "DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/actividadBase/*/instrucciones").hasAnyRole("ADMIN", "DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/actividadBase/campo/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/actividadBase/habilitarDeshabilitar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/actividadBase/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/actividadBase/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/actividadBase").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/actividadBase").hasRole("ADMIN")

                        //DOCUMENTO
                        .requestMatchers(HttpMethod.GET,"/api/documentos/listPage").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/documentos/habilitarDeshabilitar/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/documentos/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/documentos/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/documentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/documentos").hasRole("ADMIN")

                        //EXPEDIENTES
                        .requestMatchers(HttpMethod.GET,"/api/expedientes/alumnos/*").hasAnyRole("ADMIN", "ALUMNO")
                        .requestMatchers(HttpMethod.PUT,"/api/expedientes/*/observaciones").hasRole("ADMIN")

                        //DOCUMENTO-EXPEDIENTE
                        .requestMatchers(HttpMethod.GET,"/api/expedientes/documentos/*/ver").hasAnyRole("ADMIN", "ALUMNO")
                        .requestMatchers(HttpMethod.PUT,"/api/expedientes/documentos/*/revision").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/expedientes/*/documentos/*").hasRole("ALUMNO")

                        //ACTIVIDAD GRUPO
                        .requestMatchers(HttpMethod.POST,"/api/grupo/actividades/catalogo/multiples").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.POST,"/api/grupo/actividades/extra/multiples").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.PUT,"/api/grupo/actividades/instrucciones").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/grupo/docente/*/actividades").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.POST,"/api/grupo/actividad-grupo/*/inscripciones").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/grupo/actividad/*/instrucciones").hasAnyRole("DOCENTE", "ALUMNO")
                        .requestMatchers(HttpMethod.PUT,"/api/grupo/actividad/*/actualizar").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/grupo/actividad/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/grupo/*/actividades/grupos").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/grupo/*/actividades/individuales").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/grupo/*/actividades").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/grupo/*/actividad").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.POST,"/api/grupo/*/actividades/catalogo").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.POST,"/api/grupo/*/actividades/extra").hasRole("DOCENTE")

                        //SESIONES
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/grupos/asistencias/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/docente/*/sesiones").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/porDocente/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/porGrupo/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/asistencia/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/alumno/*/sesiones").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.GET,"/api/sesiones/porAlumno/*").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.POST,"/api/sesiones/grupo/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.PUT,"/api/sesiones/*/inscripciones").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.PUT,"/api/sesiones/actualizar/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.POST,"/api/sesiones/*/asistencia").hasRole("DOCENTE")



                        //ACTIVIDAD ALUMNO
                        .requestMatchers(HttpMethod.GET,"/api/actividades-alumnos/grupos/seguimiento/*").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/actividades-alumnos/inscripcion/*/disponibles").hasAnyRole("ALUMNO", "DOCENTE")
                        .requestMatchers(HttpMethod.POST,"/api/actividades-alumnos/*/entrega").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.GET,"/api/actividades-alumnos/*/entrega").hasAnyRole("ALUMNO", "DOCENTE")
                        .requestMatchers(HttpMethod.PUT,"/api/actividades-alumnos/*/observacion").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.PUT,"/api/actividades-alumnos/*/exentar").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.PUT,"/api/actividades-alumnos/*/estado").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/actividades-alumnos/inscripcion/*").hasAnyRole("ALUMNO", "DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/actividades-alumnos/actividad/*").hasRole("DOCENTE")


                        //DETALLE
                        .requestMatchers(HttpMethod.PUT,"/api/detalles/*").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.POST,"/api/detalles").hasRole("ALUMNO")

                        //SEGUIMIENTO
                        .requestMatchers(HttpMethod.GET,"/api/seguimiento-semanal/*/actual").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.POST,"/api/seguimiento-semanal/*").hasRole("ALUMNO")
                        .requestMatchers(HttpMethod.GET,"/api/seguimiento-semanal/*").hasRole("ALUMNO")

                        //REPORTES
                        .requestMatchers(HttpMethod.GET,"/api/reporte/alumnos/*/reporte").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reporte/asistencias/*/reporte").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/reporte/grupos/*/reporte").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET,"/api/reporte/inscripciones/reporte").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reporte/expediente/reporte").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reporte/seguimientoSemanal/*/reporte").hasRole("DOCENTE")
                        //
                        // CUALQUIER OTRA RUTA
                        .anyRequest().authenticated()
                )

                // Política sin sesión (cada request lleva su token)
                ////es para configurar que http no almacene un usario logueado. En cada peticion se debe validar desde cero el token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Usuario no autenticado (sin token o token inválido)
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");

                            ApiError err = new ApiError(
                                    null,
                                    HttpStatus.UNAUTHORIZED.value(),
                                    "No autorizado",
                                    "Debes iniciar sesión o tu token no es válido",
                                    request.getRequestURI(),
                                    null
                            );

                            response.getWriter().write(new ObjectMapper().writeValueAsString(err));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Usuario autenticado, pero sin permisos (rol incorrecto)
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");

                            ApiError err = new ApiError(
                                    null,
                                    HttpStatus.FORBIDDEN.value(),
                                    "Acceso denegado",
                                    "No tienes permisos para realizar esta acción",
                                    request.getRequestURI(),
                                    null
                            );

                            response.getWriter().write(new ObjectMapper().writeValueAsString(err));
                        })
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Configurar el proveedor de autenticación
                .authenticationProvider(authenticationProvider())
                // Agregar el filtro JWT antes del filtro de autenticación por usuario/contraseña
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
