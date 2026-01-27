package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Models.Enum.EstadoEnum;

import java.util.List;

public interface IGrupoService {
    GrupoDto crear(GrupoDto dto);
    GrupoDto obtenerPorId(Long idGrupo);
    List<GrupoDto> obtenerGrupoGeneral();
    List<GrupoDto> obtenerPorCurso(Long idCurso);
    List<GrupoDto> obtenerPorDocente(Long idDocente);
    GrupoDto cambiarEstado(Long idGrupo);
    GrupoDto cambiarDocente(GrupoDto dto);
}