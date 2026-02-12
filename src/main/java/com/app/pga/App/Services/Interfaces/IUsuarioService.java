package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Entities.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario createUsuario (UsuarioRequestDto usuarioRequestDto);
    UsuarioResponseDto desactivarUsuario (Long idUsuario);
    UsuarioResponseDto  findById (Long idUsuario);
    UsuarioResponseDto  actualizarUsuario (Long idUsuario, UsuarioRequestDto usuarioRequestDto);
    List< UsuarioResponseDto > findAll();
    List< UsuarioResponseDto >findAllActivos();
}
