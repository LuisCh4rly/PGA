package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.AlumnoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.AlumnoResponseDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Mappers.AlumnoMapper;
import com.app.pga.App.Repositories.IAlumnoRepository;
import com.app.pga.App.Repositories.IDocenteRepository;
import com.app.pga.App.Repositories.IUsuarioRepository;
import com.app.pga.App.Services.Interfaces.IAlumnoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumnoService implements IAlumnoService {
    private final AlumnoMapper alumnoMapper;
    private final IAlumnoRepository alumnoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ExpedienteService expedienteService;
    private final IDocenteRepository docenteRepository;

    public AlumnoService(AlumnoMapper alumnoMapper, IAlumnoRepository alumnoRepository, IUsuarioRepository usuarioRepository, ExpedienteService expedienteService, IDocenteRepository docenteRepository) {
        this.alumnoMapper = alumnoMapper;
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.expedienteService = expedienteService;
        this.docenteRepository = docenteRepository;
    }

    //Crerar Alumnos
    public Alumno createAlumno (AlumnoRequestDto alumnoRequestDto){
        Usuario usuario = usuarioRepository.findById(alumnoRequestDto.idUsuario())
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado."));

        alumnoRepository.findByUsuario_IdUsuario(alumnoRequestDto.idUsuario()).ifPresent(AlumnoDto->{
            throw new DuplicateResourceException("El usuario ya se encuentra asociado a un alumno");
        });
        docenteRepository.findByUsuario_IdUsuario(alumnoRequestDto.idUsuario()).ifPresent(AlumnoDto->{
            throw new DuplicateResourceException("El usuario ya se encuentra asociado a un docente");
        });

        Alumno alumnoEntity = alumnoMapper.toEntity(alumnoRequestDto);
        if(alumnoEntity.getFechaAlta()==null){
            alumnoEntity.setFechaAlta(LocalDate.now());
            alumnoEntity.setActivo(true);
            alumnoEntity.setUsuario(usuario);
        }
        Alumno nuevoAlumno = alumnoRepository.save(alumnoEntity);
        expedienteService.crearExpediente(nuevoAlumno.getIdAlumno());
        return nuevoAlumno;
    }


    //Actualizar alumno
    public AlumnoResponseDto actualizarAlumno (Long idAlumno, AlumnoRequestDto alumnoRequestDto){
        Alumno alumnoExistente = alumnoRepository.findById(idAlumno)
                .orElseThrow(()->new NotFoundException("Alumno no encontrado: "+idAlumno));

        if(!alumnoExistente.getActivo()){
            throw new ResourceDisabledException("No se puede modificar un alumno deshabilitado");
        }

        alumnoExistente.setFechaTermino(alumnoRequestDto.fechaTermino());

        Alumno alumnoActualizado = alumnoRepository.save(alumnoExistente);
        return alumnoMapper.toDto(alumnoActualizado);
    }


    //Consulta general para activos
    @Transactional(readOnly = true)
    public List<AlumnoResponseDto>findAllActivos(){
        return alumnoRepository.findByActivoTrue()
                .stream()
                .map(alumno ->alumnoMapper.toDto(alumno))
                .collect(Collectors.toList());
    }


    //Consulta general
    @Transactional(readOnly = true)
    public List<AlumnoResponseDto>findAll(){
        return alumnoRepository.findAll()
                .stream()
                .map(alumno ->alumnoMapper.toDto(alumno))
                .collect(Collectors.toList());
    }


    //Cosulta por id
    @Transactional(readOnly = true)
    public AlumnoResponseDto findById (Long idAlumno){
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(()->new NotFoundException("Alumno no encontrado: "+idAlumno));
        return alumnoMapper.toDto(alumno);
    }


    //Desactivar - Activar
    public AlumnoResponseDto desactivarActivarAlumno (long idAlumno){

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(()->new NotFoundException("Alumno no encontrado:" +idAlumno));

        if (alumno.getActivo()==true){
            alumno.setFechaBaja(LocalDate.now());
            alumno.setFechaAlta(null);
        }else{
            alumno.setFechaAlta(LocalDate.now());
            alumno.setFechaBaja(null);
            Expediente expediente = expedienteService.obtenerPorAlumno(idAlumno);
            expedienteService.sincronizarExpediente(expediente);
        }
        alumno.setActivo(!alumno.getActivo());
        Alumno alumnoActDes = alumnoRepository.save(alumno);
        return alumnoMapper.toDto(alumnoActDes);
    }
}
