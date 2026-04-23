package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadCatalogoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadExtraDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Entities.ActividadGrupo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IActividadGrupoService {
    void precargarDesdeCurso(Long idGrupo);

    ActividadGrupoDto asignarDesdeCatalogo(Long idGrupo, AsignarActividadCatalogoDto dto, MultipartFile archivo);
    ActividadGrupoDto crearExtra( Long idGrupo, AsignarActividadExtraDto dto, MultipartFile archivo);

    List<ActividadGrupoDto> obtenerActividadesPorGrupo(Long idGrupo);
    List<ActividadGrupoDto> obtenerActividadesGrupales(Long idGrupo);
    List<ActividadGrupoDto> obtenerActividadesIndividuales(Long idGrupo);
    ActividadGrupoDashboardDto obtenerActividadPorId (Long idActividad);


    ActividadGrupoDto actualizarInstruccionesPorId (Long idActividadGrupo, MultipartFile archivo);



    void agregarInscripcionesActividad(Long idActividadGrupo, List<Long> nuevasInscripciones);

    Resource visualizarInstrucciones(Long idActividadGrupo);


}
