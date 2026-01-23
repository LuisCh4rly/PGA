package com.app.pga.App.Services.Implements;


import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.CursoDto;
import com.app.pga.App.Models.Entities.ActividadBase;

import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import com.app.pga.App.Models.Mappers.ActividadBaseMapper;
import com.app.pga.App.Models.Mappers.CursoMapper;
import com.app.pga.App.Repositories.IActividadBaseRepository;
import com.app.pga.App.Repositories.ICursoRepository;
import com.app.pga.App.Repositories.ICurso_ActividadBaseRepository;
import com.app.pga.App.Services.Interfaces.ICursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CursoService implements ICursoService {
    private final ICursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final IActividadBaseRepository actividadBaseRepository;
    private final ICurso_ActividadBaseRepository cursoActividadBaseRepository;


    @Override
    public CursoDto crearCurso(CursoDto cursoDto) {
        if (cursoRepository.existsByNombreEqualsIgnoreCase(cursoDto.nombre())) {
            throw new IllegalArgumentException("El registro ya existe");
        }
        Curso cursoNuevo = cursoMapper.toEntity(cursoDto);
        cursoNuevo.setActivo(true);
        cursoNuevo.setFechaAlta(LocalDate.now());
        cursoNuevo.setActividades(new ArrayList<Curso_ActividadBase>());

        if (!cursoDto.actividades().isEmpty()) {
            for (ActividadBaseDto a : cursoDto.actividades()) {
                ActividadBase ab = actividadBaseRepository.findById(a.idActividad())
                        .orElseThrow(() -> new RuntimeException("Actividad base no encontrada"));
                if (!ab.getActivo()) {
                    throw new IllegalArgumentException("Actividad: " + ab.getTitulo() + " está deshabilitada");
                }
                Curso_ActividadBase cab = Curso_ActividadBase.builder()
                        .curso(cursoNuevo)
                        .actividadBase(ab)
                        .build();
                cursoNuevo.getActividades().add(cab);
            }

        }

        return cursoMapper.toDtoConActividades(cursoRepository.save(cursoNuevo));
    }

    @Override
    public CursoDto obtenerCurso(Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado"));
        return cursoMapper.toDtoConActividades(curso);
    }

    @Override
    public List<CursoDto> obtenerCursosGeneral() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
                .map(c -> cursoMapper.toDtoSimple(c))
                .collect(Collectors.toList());
    }

    @Override
    public List<CursoDto> obtenerCursosActivos() {
        List<Curso> cursos = cursoRepository.findByActivoTrue();
        return cursos.stream()
                .map(c -> cursoMapper.toDtoSimple(c))
                .collect(Collectors.toList());
    }

    @Override
    public CursoDto actualizarCurso(CursoDto cursoDto, Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado"));
        if (!curso.getActivo()) {
            throw new IllegalStateException("No se puede modificar un curso deshabilitado");
        }
        curso.setDescripcion(cursoDto.descripcion());
        return cursoMapper.toDtoSimple(cursoRepository.save(curso));
    }


    @Override
    public CursoDto habitarDeshabilitar(Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado"));
        if (curso.getActivo()) {
            curso.setActivo(false);
            curso.setFechaBaja(LocalDate.now());
            curso.setFechaAlta(null);
        } else {
            curso.setActivo(true);
            curso.setFechaAlta(LocalDate.now());
            curso.setFechaBaja(null);
        }
        return cursoMapper.toDtoSimple(cursoRepository.save(curso));
    }

    @Override
    public CursoDto asignarActividades(Long idCurso, List<ActividadBaseDto> actividades) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado"));

        if (!curso.getActivo()){
            throw new IllegalArgumentException("No se puede asignar actividades a un curso deshabilitado");
        }
        for (ActividadBaseDto a : actividades) {
            ActividadBase ab = actividadBaseRepository.findById(a.idActividad())
                    .orElseThrow(() -> new RuntimeException("Actividad base no encontrada"));

            if (!ab.getActivo()) {
                throw new IllegalArgumentException("Actividad: " + ab.getTitulo() + " está deshabilitada");
            }
            if (cursoActividadBaseRepository.existsByCurso_IdCursoAndActividadBase_IdActividad(curso.getIdCurso(), ab.getIdActividad())) {
                throw new IllegalStateException("La actividad ya está asignada al curso");
            }

            Curso_ActividadBase cab = Curso_ActividadBase.builder()
                    .curso(curso)
                    .actividadBase(ab)
                    .build();
            curso.getActividades().add(cab);
        }
        return cursoMapper.toDtoConActividades(cursoRepository.save(curso));
    }
    public void quitarActividades(Long idCurso, Long idActividad) {

        Curso_ActividadBase cab = cursoActividadBaseRepository.findByCurso_IdCursoAndActividadBase_IdActividad(idCurso, idActividad)
                .orElseThrow(()->new IllegalArgumentException("La actividad no está asignada al curso"));
        cursoActividadBaseRepository.delete(cab);



    }

}





