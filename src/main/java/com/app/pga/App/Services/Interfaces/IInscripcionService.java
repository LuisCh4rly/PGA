package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.InscripcionRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionResponseDto;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import com.app.pga.App.Models.Specification.InscripcionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInscripcionService {
    InscripcionResponseDto createInscripcion (InscripcionRequestDto inscripcionDto);
    List<InscripcionResponseDto>findAllActivos();
    Page<InscripcionResponseDto> findAll(InscripcionFiltro filtro, Pageable pageable);
    InscripcionResponseDto finfById(Long idInscripcion);
    InscripcionResponseDto desactivarActivarInscripcion(Long idInscripcion);
}
