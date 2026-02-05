package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CambiarEstadoTareaDto;
import com.app.pga.App.Models.Enum.EstadoTarea;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IActividadAlumnoService {

    void entregarActividad( Long idActividadAlumno, MultipartFile archivo);
    List<ActividadAlumnoDto> obtenerPorInscripcion(Long idInscripcion);
    void agregarObservacion(Long idActividadAlumno, CambiarEstadoTareaDto cambiarEstadoTareaDto);
    void exentarActividad( Long idActividadAlumno, CambiarEstadoTareaDto cambiarEstadoTareaDto);
    void cambiarEstado ( Long idActividadAlumno,CambiarEstadoTareaDto cambiarEstadoTareaDto);
    void asignarActividadesGrupalesPorInscripcion(Long idInscripcion);
    Resource visualizarEntrega(Long idActividadAlumno);

}
