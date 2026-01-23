package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.InscripcionDto;
import com.app.pga.App.Models.Dtos.InscripcionResumenDto;

import java.util.List;

public interface IInscripcionService {
    InscripcionDto createInscripcion (InscripcionDto inscripcionDto);
    List<InscripcionDto>findAllActivos();
    List<InscripcionResumenDto>findResumen();
    InscripcionDto finfById(Long idInscripcion);
    InscripcionDto desactivarActivarInscripcion(Long idInscripcion);
}
