package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.UsuarioDto;

import java.util.List;

public interface IUsuarioService {

    UsuarioDto createUsuario (UsuarioDto usuarioDto);
    UsuarioDto desactivarUsuario (Long idUsuario);
    UsuarioDto findById (Long idUsuario);
    UsuarioDto actualizarUsuario (Long idUsuario, UsuarioDto usuarioDto);
    List<UsuarioDto> findAll();
    List<UsuarioDto>findAllActivos();
}
