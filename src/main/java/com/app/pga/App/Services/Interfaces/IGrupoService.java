package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.GrupoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;

import java.util.List;

public interface IGrupoService {
    GrupoResponseDto crear(GrupoRequestDto dto);
    GrupoResponseDto obtenerPorId(Long idGrupo);
    List<GrupoResponseDto> obtenerGrupoGeneral();
    List<GrupoResponseDto> obtenerGruposActivos();
    List<GrupoResponseDto> obtenerPorCurso(Long idCurso);
    List<GrupoResponseDto> obtenerPorDocente(Long idDocente);
    GrupoResponseDto cambiarEstado(Long idGrupo);
    GrupoResponseDto cambiarDocente(Long idGrupo, Long idDocente);
}