package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteReporteDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Entities.Expediente;

import java.util.List;

public interface IExpedienteService {

    ExpedienteResponseDto verExpediente(Long idAlumno);
    Expediente obtenerPorAlumno(Long idAlumno);
    Expediente crearExpediente(Long idAlumno);
    ExpedienteResponseDto agregarObservaciones( Long idExpediente, String observaciones);
    void sincronizarExpediente(Expediente expediente);
    List<ExpedienteReporteDto> findAll();
}
