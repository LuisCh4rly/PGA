package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoAgrupadoDto;
import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;

import java.util.List;

public interface IDetalleSeguimientoService {

    void actualizarEstado (Long idDetalleSeguimeinto, DetalleSeguimientoRequestDto dto);
    List<DetalleDashboardDto> crearDetalleSeguimiento(DetalleSeguimientoAgrupadoDto dto);
    void agregarActividadesPendientes(Long numeroSemanaActual, Long idInscripcion);


}
