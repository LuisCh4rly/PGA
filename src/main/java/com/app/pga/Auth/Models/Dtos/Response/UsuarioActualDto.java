package com.app.pga.Auth.Models.Dtos.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
@Builder
@Setter
@Getter
public class UsuarioActualDto{
    private Long id;
    private String email;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean debeCambiarPassword;
 }
