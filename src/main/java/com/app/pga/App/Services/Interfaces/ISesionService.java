package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.*;

import java.util.List;

public interface ISesionService {
    SesionDto createSesion (SesionRequestDto dto, Long idGrupo);
    SesionDetalletDto actualizarInscripcionesSesion(Long idSesion, List<Long> nuevosIdsInscripcion);
    SesionDetalletDto tomarAsistencia(Long idSesion, List<AsistenciaDto> listaAsistencia);
    SesionDetalletDto obtenerDetalleSesion (Long idSesion);
    List<SesionDto>obtenerSesionesPorGrupo(Long idGrupo);
    List<SesiondocenteDto>obtenerSesionPorDocente(Long idDocente);
    SesionDetalletDto actualizarSesion (Long idSesion, SesionUpdateDto dto );
    List<SesiondocenteDto>obtenerSesionPorAlumno(Long idInscripcion);
}
