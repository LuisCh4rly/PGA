package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.SemanaRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SemanaResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISeguimientoSemanalService {

    SemanaResponseDto crearSeguimientoSemanal (SemanaRequestDto dto);

    SeguimientoDashboardResponseDto obtenerSeguimientoSemanalActual(Long idInscripción);
   List<SeguimientoDashboardResponseDto> obtenerSeguimientoAlumno(Long idInscripcion, Pageable pageable);
}
