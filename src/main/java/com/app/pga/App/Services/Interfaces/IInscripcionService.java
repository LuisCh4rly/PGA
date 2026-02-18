package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.InscripcionRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionResponseDto;

import java.util.List;

public interface IInscripcionService {
    InscripcionResponseDto createInscripcion (InscripcionRequestDto inscripcionDto);
    List<InscripcionResponseDto>findAllActivos();
    List<InscripcionResponseDto>findAll();
    InscripcionResponseDto finfById(Long idInscripcion);
    InscripcionResponseDto desactivarActivarInscripcion(Long idInscripcion);
}
