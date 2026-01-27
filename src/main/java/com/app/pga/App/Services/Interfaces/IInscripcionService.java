package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.InscripcionDto;

import java.util.List;

public interface IInscripcionService {
    InscripcionDto createInscripcion (InscripcionDto inscripcionDto);
    List<InscripcionDto>findAllActivos();
    List<InscripcionDto>findAll();
    InscripcionDto finfById(Long idInscripcion);
    InscripcionDto desactivarActivarInscripcion(Long idInscripcion);
}
