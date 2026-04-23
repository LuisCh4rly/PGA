package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Dtos.RequestDto.SesionRequestDto;
import com.app.pga.App.Models.Dtos.RequestDto.SesionUpdateDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDetalletDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesiondocenteDto;
import com.app.pga.App.Models.Filtros.SesionFiltro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISesionService {
    SesionDto createSesion (Long idGrupo, SesionRequestDto dto);
    SesionDetalletDto actualizarInscripcionesSesion(Long idSesion, List<Long> nuevosIdsInscripcion);
    SesionDetalletDto tomarAsistencia(Long idSesion, List<AsistenciaDto> listaAsistencia);
    SesionDetalletDto obtenerDetalleSesion (Long idSesion);
    List<SesionDto>obtenerSesionesPorGrupo(Long idGrupo);
    List<SesiondocenteDto>obtenerSesionPorDocente(Long idDocente);
    SesionDetalletDto actualizarSesion (Long idSesion, SesionUpdateDto dto );
    List<SesiondocenteDto>obtenerSesionPorAlumno(Long idInscripcion);
    public Page<SesiondocenteDto> obtenerSesionesFiltradas(Long idDocente, SesionFiltro filtro, Pageable pageable);
    public List<ReporteAsistenciaGrupoDto>obtenerReporteAsistenciaPorGrupo(Long idGrupo);
}
