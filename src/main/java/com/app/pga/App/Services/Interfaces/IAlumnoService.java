package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.AlumnoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.AlumnoResponseDto;
import com.app.pga.App.Models.Entities.Alumno;

import java.util.List;

public interface IAlumnoService {
    Alumno createAlumno (AlumnoRequestDto alumnoDto);
    AlumnoResponseDto actualizarAlumno (Long idAlumno, AlumnoRequestDto alumnoDto);
    List<AlumnoResponseDto> findAllActivos();
    List<AlumnoResponseDto>findAll();
    AlumnoResponseDto findById (Long idAlumno);
    AlumnoResponseDto desactivarActivarAlumno (long idAlumno);
}
