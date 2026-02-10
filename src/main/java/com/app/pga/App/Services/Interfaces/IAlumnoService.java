package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.AlumnoDto;

import java.util.List;

public interface IAlumnoService {
    AlumnoDto createAlumno (AlumnoDto alumnoDto);
    AlumnoDto actualizarAlumno (Long idAlumno, AlumnoDto alumnoDto);
    List<AlumnoDto> findAllActivos();
    List<AlumnoDto>findAll();
    AlumnoDto findById (Long idAlumno);
    AlumnoDto desactivarActivarAlumno (long idAlumno);
}
