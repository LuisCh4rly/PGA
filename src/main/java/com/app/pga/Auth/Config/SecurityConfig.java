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
                        .requestMatchers("/api/auth/login",
                                        "/api/auth/register").permitAll()
                        .requestMatchers("/api/usuario/**").permitAll()
                        .requestMatchers("/api/cuentas/**").permitAll()
                        .requestMatchers("/api/actividades-alumnos/**").permitAll()
                        .requestMatchers("/api/actividadBase/**").permitAll()
                        .requestMatchers("/api/grupo/**").permitAll()
                        .requestMatchers("/api/campo/**").permitAll()
                        .requestMatchers("/api/curso/**").permitAll()
                        .requestMatchers("/api/documentos/**").permitAll()
                        .requestMatchers("/api/expedientes/**").permitAll()
                        .requestMatchers("/api/expedientes/**").permitAll()
                        .requestMatchers("/api/grupo/**").permitAll()
                        .requestMatchers("/api/inscripcion/**").permitAll()
                        .requestMatchers("/api/sesiones/**").permitAll()
                        .requestMatchers("/api/reporte/alumnos/{id}/reporte").permitAll()
                        .requestMatchers("/api/seguimiento-semanal/**").permitAll()
                        .requestMatchers("/api/detalles/**").permitAll()
                        // RUTAS DE ADMIN
                        //RUTAS ALUMNO
                        //RUTAS DOCENTE

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
