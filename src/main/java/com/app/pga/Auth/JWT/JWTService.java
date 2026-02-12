package com.app.pga.Auth.JWT;

import com.app.pga.Auth.Models.Dtos.Response.AuthResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long timeExpiration;

    //GENERAR CONTRASEÑA
    public AuthResponseDto generateToken(UserDetails userDetails) {
        Instant now = Instant.now() ; //fecha actual
        Instant expiration = now.plusMillis(timeExpiration);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .toList();

        String token =  Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("ROLES", roles)
                .signWith(getSignatureKey())
                .compact();
        return new AuthResponseDto(token, "Bearer", expiration);
    }

    public String getUsernameFromToken (String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Obtener username desde el Token
    private Date getExpiration (String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public <T> T getClaim (String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractAllClaims (String token) {
        return Jwts.parser() //crear un objeto capaz de leer y decodificar un jwt
                .verifyWith(getSignatureKey())//pasarle la firma para que el parser valide autentiidad
                .build() //construye el parser
                .parseSignedClaims(token) //lee el token frirmado
                .getPayload(); //obtine los claims
    }

    public SecretKey getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //decodificar
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid (String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
