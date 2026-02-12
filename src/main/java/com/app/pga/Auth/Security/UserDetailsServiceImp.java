package com.app.pga.Auth.Security;

import com.app.pga.Auth.Models.Entities.Cuenta;
import com.app.pga.Auth.Repositories.ICuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final ICuentaRepository cuentaRepository;

    @Override
    public UserDetails loadUserByUsername(String email ) throws UsernameNotFoundException {
        Cuenta cuenta = cuentaRepository.findByUsuario_Email(email)
                .orElseThrow(()-> new UsernameNotFoundException("Cuenta no encontrada con el email: "+ email));

        if (cuenta.getUsuario() == null || !cuenta.getUsuario().getActivo()) {
            throw new UsernameNotFoundException("Usuario deshabilitado o eliminado");
        }
        return UserDetailsImp.build(cuenta);
    }
}
