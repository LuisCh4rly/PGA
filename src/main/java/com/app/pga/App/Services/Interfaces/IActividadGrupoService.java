package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadCatalogoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadExtraDto;

import java.util.List;

public interface IActividadGrupoService {
    ActividadGrupoDto asignarDesdeCatalogo(Long idGrupo, AsignarActividadCatalogoDto dto);
    ActividadGrupoDto crearExtra( Long idGrupo, AsignarActividadExtraDto dto);
    List<ActividadGrupoDto> obtenerActividadesPorGrupo(Long idGrupo);
    List<ActividadGrupoDto> obtenerActividadesGrupales(Long idGrupo);
    List<ActividadGrupoDto> obtenerActividadesIndividuales(Long idGrupo);
    void agregarInscripcionesActividad(Long idActividadGrupo, List<Long> nuevasInscripciones);
}
