package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Filtros.UsuarioFiltro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {

    Usuario createUsuario (UsuarioRequestDto usuarioRequestDto);
    UsuarioResponseDto desactivarUsuario (Long idUsuario);
    UsuarioResponseDto  findById (Long idUsuario);
    UsuarioResponseDto  actualizarUsuario (Long idUsuario, UsuarioRequestDto usuarioRequestDto);

    Page< UsuarioResponseDto > findAll(UsuarioFiltro filtro , Pageable pageable);
    List< UsuarioResponseDto >findAllActivos();
    List<UsuarioResponseDto>findAllActivosDocentes();
    Page<UsuarioResponseDto> findAllDocentes(UsuarioFiltro filtro , Pageable pageable);
    List<UsuarioResponseDto>findAllActivosAlumnos();
    Page<UsuarioResponseDto>findAllAlumnos(UsuarioFiltro filtro , Pageable pageable);
}
