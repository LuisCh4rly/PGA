package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.DocenteDto;

import java.util.List;

public interface IDocenteService {
    DocenteDto createDocente (DocenteDto docenteDto);
    List<DocenteDto> findAllActivos();
    List<DocenteDto>findAll();
    DocenteDto findById(Long idDocente);
    DocenteDto desactivarActivarDocente (Long idDocente);
}
