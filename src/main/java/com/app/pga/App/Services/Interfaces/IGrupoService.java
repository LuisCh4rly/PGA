package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.GrupoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;
import com.app.pga.App.Models.Filtros.GrupoFiltro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGrupoService {
    GrupoResponseDto crear(GrupoRequestDto dto);
    GrupoResponseDto obtenerPorId(Long idGrupo);
    List<GrupoResponseDto> obtenerGrupoGeneral();
    List<GrupoResponseDto> obtenerGruposActivos();
    List<GrupoResponseDto> obtenerPorCurso(Long idCurso);
    List<GrupoResponseDashboardDto> obtenerPorDocente(Long idDocente);
    GrupoResponseDto cambiarEstado(Long idGrupo);
    GrupoResponseDto cambiarDocente(Long idGrupo, Long idDocente);
    Page<GrupoResponseDto> findAll(GrupoFiltro filtro , Pageable pageable);
}