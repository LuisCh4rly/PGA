package com.app.pga.Auth.Security;

import com.app.pga.Auth.Models.Entities.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImp implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private Boolean debeCambiarPassword;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImp build(Cuenta cuenta) {
        Collection<? extends GrantedAuthority> authorities = Set.of( new SimpleGrantedAuthority("ROLE_".concat(cuenta.getRole().getName().name())));
        return new UserDetailsImp(
                cuenta.getIdCuenta(),
                cuenta.getEmail(),
                cuenta.getPassword(),
                cuenta.getDebeCambiarPassword(),
                authorities
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }

    public boolean seDebeCambiarContraseña() { return debeCambiarPassword;}
}
