package com.app.pga.App.Services.Interfaces;


import com.app.pga.App.Models.Dtos.RequestDto.DocenteRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocenteResponseDto;
import com.app.pga.App.Models.Entities.Docente;

import java.util.List;

public interface IDocenteService {
    Docente createDocente (DocenteRequestDto docenteRequestDto);
    List<DocenteResponseDto> findAllActivos();
    List<DocenteResponseDto>findAll();
    DocenteResponseDto findById(Long idDocente);
    DocenteResponseDto desactivarActivarDocente (Long idDocente);
}
