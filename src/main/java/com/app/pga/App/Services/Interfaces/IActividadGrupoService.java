package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadCatalogoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadCatalogoMultipleDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadExtraDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadExtraMultipleDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoAgrupadaDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResumenDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IActividadGrupoService {
    void precargarDesdeCurso(Long idGrupo);

    ActividadGrupoDto asignarDesdeCatalogo(Long idGrupo, AsignarActividadCatalogoDto dto, MultipartFile archivo);
    ActividadGrupoDto crearExtra( Long idGrupo, AsignarActividadExtraDto dto, MultipartFile archivo);

    void asignarDesdeCatalogoMultiple (AsignarActividadCatalogoMultipleDto dto, MultipartFile archivo);
    void crearExtraMultiple(AsignarActividadExtraMultipleDto dto, MultipartFile archivo);

    List<ActividadGrupoDto> obtenerActividadesPorGrupo(Long idGrupo);
    List<ActividadGrupoDto> obtenerActividadesGrupales(Long idGrupo);
    List<ActividadGrupoDto> obtenerActividadesIndividuales(Long idGrupo);
    ActividadGrupoDashboardDto obtenerActividadPorId (Long idActividad);


    ActividadGrupoDto actualizarInstruccionesPorId (Long idActividadGrupo, MultipartFile archivo);



    void agregarInscripcionesActividad(Long idActividadGrupo, List<Long> nuevasInscripciones);

    Resource visualizarInstrucciones(Long idActividadGrupo);

    List <GrupoResumenDto> obtenerGruposSinActividad (String titulo, Long idDocente);

    Set<ActividadGrupoAgrupadaDto>obtenerActividadesPorGrupos(Long idDocente);

    void actualizarInstruccionesMultiple (List<Long>idActividades, MultipartFile archivo);
}
