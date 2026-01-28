package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.InscripcionDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Mappers.AlumnoMapper;
import com.app.pga.App.Models.Mappers.InscripcionMapper;
import com.app.pga.App.Repositories.IAlumnoRepository;
import com.app.pga.App.Repositories.IGrupoRepository;
import com.app.pga.App.Repositories.IInscripcionRepository;
import com.app.pga.App.Services.Interfaces.IInscripcionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscripcionService implements IInscripcionService {

    private final InscripcionMapper inscripcionMapper;
    private final IInscripcionRepository inscripcionRepository;
    private final AlumnoMapper alumnoMapper;
    private final IAlumnoRepository iAlumnoRepository;
    private final IGrupoRepository grupoRepository;

    public InscripcionService (InscripcionMapper inscripcionMapper, IInscripcionRepository inscripcionRepository, AlumnoMapper alumnoMapper, IAlumnoRepository iAlumnoRepository, IGrupoRepository grupoRepository){
        this.inscripcionMapper=inscripcionMapper;
        this.inscripcionRepository=inscripcionRepository;
        this.alumnoMapper=alumnoMapper;
        this.iAlumnoRepository = iAlumnoRepository;
        this.grupoRepository = grupoRepository;
    }
    //Crear inscripciones
    public InscripcionDto createInscripcion (InscripcionDto inscripcionDto){
        Alumno alumno = iAlumnoRepository.findById(inscripcionDto.alumno().idAlumno())
                .orElseThrow(()->new NotFoundException("Alumno no encontrado."));
        if(!alumno.getActivo()){
            throw new ResourceDisabledException("Alumno deshbilitado");
        }
        //verificar que el alumno no tenga inscripciones activas
        inscripcionRepository.findByAlumno_IdAlumnoAndEstadoTrue(inscripcionDto.alumno().idAlumno()).ifPresent(InscripcionDto->{
            throw new DuplicateResourceException("El alumno tiene una inscripción activa");
        });

        Inscripcion inscripcionEntity = inscripcionMapper.toEntity(inscripcionDto);
        if (inscripcionEntity.getEstado()==null){
            inscripcionEntity.setEstado(true);
            inscripcionEntity.setFechaInscripcion(LocalDate.now());
            inscripcionEntity.setAlumno(alumno);
        }
        Inscripcion nuevaInscripcion = inscripcionRepository.save(inscripcionEntity);
        return inscripcionMapper.toDto(nuevaInscripcion);
    }

    //consulta para inscripciones activas
    @Transactional(readOnly = true)
    public List<InscripcionDto>findAllActivos(){
        return inscripcionRepository.findByEstadoTrue()
                .stream()
                .map(inscripcion ->inscripcionMapper.toDto(inscripcion))
                .collect(Collectors.toList());
    }


    //consulta general
    @Transactional(readOnly = true)
    public List<InscripcionDto>findAll(){
        return inscripcionRepository.findAll()
                .stream()
                .map(inscripcion ->inscripcionMapper.toDtoResumen(inscripcion))
                .collect(Collectors.toList());
    }


    //consulta por id
    @Transactional(readOnly = true)
    public InscripcionDto finfById(Long idInscripcion){
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(()->new NotFoundException("Registro no encontrado: "+idInscripcion));
        return inscripcionMapper.toDto(inscripcion);
    }

    //activar desactivar
    public InscripcionDto desactivarActivarInscripcion(Long idInscripcion){
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(()->new NotFoundException("Registro no encontrado: "+idInscripcion));
        if(inscripcion.getEstado()==true){
            inscripcion.setFechaBaja(LocalDate.now());
            inscripcion.setFechaInscripcion(null);
        }else{
            inscripcion.setFechaBaja(null);
            inscripcion.setFechaInscripcion(LocalDate.now());
        }
        inscripcion.setEstado(!inscripcion.getEstado());
        Inscripcion inscripcionDes = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.toDto(inscripcionDes);
    }


    //Asignar grupo
    public InscripcionDto asignarGrupo (InscripcionDto inscripcionDto){
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionDto.idInscripcion())
                .orElseThrow(()->new NotFoundException("Inscripción no encontrada: "+inscripcionDto.idInscripcion()));

            if (!inscripcion.getEstado()) {
                throw new ResourceDisabledException("La inscripción no está activa");
            }

            Grupo grupo = grupoRepository.findById(inscripcionDto.grupo().idGrupo())
                    .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));

            if (grupo.getEstado() == Estado.DESHABILITADO) {
                throw new ResourceDisabledException("El grupo no está activo");
            }

            boolean existe = inscripcionRepository.existsByAlumno_IdAlumnoAndGrupo_IdGrupoAndGrupo_Estado(
                    inscripcion.getAlumno().getIdAlumno(),
                    grupo.getIdGrupo(),
                    Estado.HABILITADO
            );


        if (existe) {
                throw new DuplicateResourceException("El alumno ya está inscrito en este grupo");
            }

            inscripcion.setGrupo(grupo);

            return inscripcionMapper.toDtoResumen(inscripcionRepository.save(inscripcion));
        }

    }


