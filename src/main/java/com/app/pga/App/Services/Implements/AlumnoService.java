package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.AlumnoDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Mappers.AlumnoMapper;
import com.app.pga.App.Repositories.IAlumnoRepository;
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

    public AlumnoService(AlumnoMapper alumnoMapper, IAlumnoRepository alumnoRepository, IUsuarioRepository usuarioRepository){
        this.alumnoMapper=alumnoMapper;
        this.alumnoRepository=alumnoRepository;
        this.usuarioRepository=usuarioRepository;
    }

    //Crerar Alumnos
    public AlumnoDto ccreateAlumno (AlumnoDto alumnoDto){
        Usuario usuario = usuarioRepository.findById(alumnoDto.usuarioDto().idUser()).orElseThrow(()-> new NotFoundException("Registro no encontrado."));

        alumnoRepository.findByUsuario_IdUser(alumnoDto.usuarioDto().idUser()).ifPresent(AlumnoDto->{
            throw new IllegalArgumentException("El usuario ya se encuentra asociado a un alumno");
        });

        Alumno alumnoEntity = alumnoMapper.toEntity(alumnoDto);
        if(alumnoEntity.getFechaAlta()==null){
            alumnoEntity.setFechaAlta(LocalDate.now());
            alumnoEntity.setActivo(true);
            alumnoEntity.setUsuario(usuario);
        }
        Alumno nuevoAlumno = alumnoRepository.save(alumnoEntity);
        return alumnoMapper.toDto(nuevoAlumno);
    }


    //Actualizar alumno
    public AlumnoDto actualizarAlumno (Long idAlumno, AlumnoDto alumnoDto){
        Alumno alumnoExistente = alumnoRepository.findById(idAlumno).orElseThrow(()->new NotFoundException("Registro no encontrado: "+idAlumno));

        if(!alumnoExistente.getActivo()){
            throw new IllegalStateException("No se puede modificar un registro inactivo");
        }

        alumnoExistente.setFechaTermino(alumnoDto.fechaTermino());

        Alumno alumnoActualizado = alumnoRepository.save(alumnoExistente);
        return alumnoMapper.toDto(alumnoActualizado);
    }


    //Consulta general para activos
    @Transactional(readOnly = true)
    public List<AlumnoDto>findAllActivos(){
        return alumnoRepository.findByActivoTrue()
                .stream()
                .map(alumno ->alumnoMapper.toDto(alumno))
                .collect(Collectors.toList());
    }


    //Consulta general
    @Transactional(readOnly = true)
    public List<AlumnoDto>findAll(){
        return alumnoRepository.findAll()
                .stream()
                .map(alumno ->alumnoMapper.toDto(alumno))
                .collect(Collectors.toList());
    }


    //Cosulta por id
    @Transactional(readOnly = true)
    public AlumnoDto findById (Long idAlumno){
        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(()->new NotFoundException("Registro no encontrado: "+idAlumno));
        return alumnoMapper.toDto(alumno);
    }


    //Desactivar - Activar
    public AlumnoDto desactivarActivarAlumno (long idAlumno){
        Alumno alumno = alumnoRepository.findById(idAlumno).orElseThrow(()->new NotFoundException("Registro no encontrado: "+idAlumno));
        if (alumno.getActivo()==true){
            alumno.setFechaBaja(LocalDate.now());
            alumno.setFechaAlta(null);
        }else{
            alumno.setFechaAlta(LocalDate.now());
            alumno.setFechaBaja(null);
        }
        alumno.setActivo(!alumno.getActivo());
        Alumno alumnoActDes = alumnoRepository.save(alumno);
        return alumnoMapper.toDto(alumnoActDes);
    }
}
