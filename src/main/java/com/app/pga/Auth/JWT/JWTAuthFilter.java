package com.app.pga.Auth.JWT;

import com.app.pga.Auth.Security.UserDetailsServiceImp;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsServiceImp userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        //verificar si hay un token tipo Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);
            email = jwtService.getUsernameFromToken(token);
        }
        /**siguiente verificación corresponde a validar que el usuario exista y corroborrar que el
         * token sea correcto y nadie lo haya usado antes.*/

        if(email!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            //validar que el token no esté expirado
            if(jwtService.isTokenValid(token,userDetails)) {
                // Extraer roles desde el token
                Claims claims = jwtService.extractAllClaims(token);

                List<String> roles = claims.get("ROLES", List.class);

                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());

                //crear un nuevo objeto de autenticación con el usuario y sus roles
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, //datos del usuario
                        null, //la contraseña ya viene en el token
                        authorities //roles y permisos
                        );
                //almacena datos de la sesion
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //guarda en el contexto de springSecurity.ahora Spring Security sabe que el usuario está logueado
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authorities del UserDetails: " + userDetails.getAuthorities());
                System.out.println("Authorities del JWT: " + authorities);

            }
        }
        //hacer que la petición siga a otros filtros hasta que llegue al controlador
        filterChain.doFilter(request, response);
    }
}


