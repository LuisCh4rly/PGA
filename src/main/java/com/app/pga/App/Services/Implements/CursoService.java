package com.app.pga.App.Services.Implements;


import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;

import com.app.pga.App.Models.Dtos.RequestDto.AgregarActividadesCursoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CursoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CursoResponseDto;
import com.app.pga.App.Models.Entities.ActividadBase;

import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Mappers.CursoMapper;
import com.app.pga.App.Repositories.IActividadBaseRepository;
import com.app.pga.App.Repositories.ICursoRepository;
import com.app.pga.App.Repositories.ICurso_ActividadBaseRepository;
import com.app.pga.App.Repositories.IGrupoRepository;
import com.app.pga.App.Services.Interfaces.ICursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class CursoService implements ICursoService {
    private final ICursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final IActividadBaseRepository actividadBaseRepository;
    private final ICurso_ActividadBaseRepository cursoActividadBaseRepository;
    private final IGrupoRepository grupoRepository;


    @Override
    public CursoResponseDto crearCurso(CursoRequestDto cursoRequestDto) {
        if (cursoRepository.existsByNombreEqualsIgnoreCase(cursoRequestDto.nombre())) {
            throw new DuplicateResourceException("Curso existente");
        }
        Curso cursoNuevo = cursoMapper.toEntity(cursoRequestDto);
        cursoNuevo.setActivo(true);
        cursoNuevo.setFechaAlta(LocalDate.now());
        cursoNuevo.setActividades(new ArrayList<Curso_ActividadBase>());

        if (!cursoRequestDto.idActividadesBase().isEmpty()) {
            for (Long id : cursoRequestDto.idActividadesBase()) {
                ActividadBase ab = actividadBaseRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Actividad Base no encontrada"));
                if (!ab.getActivo()) {
                    throw new ResourceDisabledException("Actividad: " + ab.getTitulo() + " está deshabilitada");
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
    @Transactional(readOnly = true)
    public CursoResponseDto obtenerCurso(Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));
        return cursoMapper.toDtoConActividades(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDto> obtenerCursosGeneral() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
                .map(c -> cursoMapper.toDtoSimple(c))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDto> obtenerCursosActivos() {
        List<Curso> cursos = cursoRepository.findByActivoTrue();
        return cursos.stream()
                .map(c -> cursoMapper.toDtoSimple(c))
                .collect(Collectors.toList());
    }

    @Override
    public CursoResponseDto actualizarCurso(CursoRequestDto cursoRequestDto, Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));
        if (!curso.getActivo()) {
            throw new ResourceDisabledException("No se puede modificar un curso deshabilitado");
        }
        curso.setDescripcion(cursoRequestDto.descripcion());
        return cursoMapper.toDtoSimple(cursoRepository.save(curso));
    }


    @Override
    public CursoResponseDto habitarDeshabilitar(Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));
        if (curso.getActivo()) {
            if (grupoRepository.existsByEstadoAndCurso_IdCurso(Estado.HABILITADO, idCurso)){
                throw new ResourceDisabledException("Existen grupos activos aun");
            }
            curso.setActivo(false);
            curso.setFechaBaja(LocalDate.now());
        } else {
            curso.setActivo(true);
            curso.setFechaAlta(LocalDate.now());
        }
        return cursoMapper.toDtoSimple(cursoRepository.save(curso));
    }

    @Override
    public CursoResponseDto asignarActividades(Long idCurso, AgregarActividadesCursoDto agregarActividadesCursoDto) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        if (!curso.getActivo()){
            throw new ResourceDisabledException("No se puede asignar actividades a un curso deshabilitado");
        }
        for (Long id : agregarActividadesCursoDto.idsActividadesBase()) {
            ActividadBase ab = actividadBaseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Actividad Base no encontrada"));

            if (!ab.getActivo()) {
                throw new ResourceDisabledException("Actividad: " + ab.getTitulo() + " está deshabilitada");
            }
            if (cursoActividadBaseRepository.existsByCurso_IdCursoAndActividadBase_IdActividad(curso.getIdCurso(), ab.getIdActividad())) {
                throw new DuplicateResourceException("La actividad ya está asignada al curso");
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
                .orElseThrow(()->new NotFoundException("La actividad no está asignada al curso"));
        cursoActividadBaseRepository.delete(cab);



    }

}





