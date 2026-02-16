package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Mappers.UsuarioMapper;
import com.app.pga.App.Repositories.IUsuarioRepository;
import com.app.pga.App.Services.Interfaces.IUsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioMapper usuarioMapper, IUsuarioRepository usuarioRepository){
        this.usuarioMapper=usuarioMapper;
        this.usuarioRepository=usuarioRepository;
    }


    // Crear Usuario
    public Usuario createUsuario (UsuarioRequestDto usuarioRequesDto){

        usuarioRepository.findByNombreAndApellidoPaternoAndApellidoMaterno(usuarioRequesDto.nombre(), usuarioRequesDto.apellidoPaterno(), usuarioRequesDto.apellidoMaterno()).ifPresent(UsuarioDto -> {
            throw new IllegalArgumentException("El registro ya existe");
        });

        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioRequesDto);
        if (usuarioEntity.getCreated_At()==null){
            usuarioEntity.setCreated_At(LocalDate.now());
            usuarioEntity.setActivo(true);//nuevo usuario inicia como activo
            usuarioEntity.setFechaAlta(LocalDate.now());
        }
        Usuario nuevoUsuario = usuarioRepository.save(usuarioEntity);

        return nuevoUsuario;
    }

    //Actualizar usuario
    public UsuarioResponseDto actualizarUsuario (Long idUsuario, UsuarioRequestDto usuarioRequestDto){

        Usuario usuarioExistente = usuarioRepository.findById(idUsuario).orElseThrow(() -> new NotFoundException("Registro no encontrado: "+ idUsuario));

        if(!usuarioExistente.getActivo()){
            throw new IllegalStateException("No se puede modificar un registro inactivo");
        }

        usuarioExistente.setDireccion(usuarioRequestDto.direccion());
        usuarioExistente.setTelefono(usuarioRequestDto.telefono());

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDto(usuarioActualizado);
    }


    //Consulta general para activos
   @Transactional(readOnly = true)
   public List<UsuarioResponseDto> findAllActivos(){
     return usuarioRepository.findByActivoTrue()
             .stream().map(usuario -> usuarioMapper.toDto(usuario))
             .collect(Collectors.toList());
   }

    //Consulta general
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> findAll(){
        return usuarioRepository.findAll()
                .stream().map(usuario -> usuarioMapper.toDto(usuario))
                .collect(Collectors.toList());
    }

   //Consulta por id
   @Transactional(readOnly = true)
   public UsuarioResponseDto findById (Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(()-> new NotFoundException("Registro no encontrado: "+ idUsuario));
        return usuarioMapper.toDto(usuario);
   }

   //Desactivar usuario
    public UsuarioResponseDto desactivarUsuario (Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(()-> new NotFoundException("Registro no encontrado: "+ idUsuario));
        if (usuario.getActivo()==true){
            usuario.setFechaBaja(LocalDate.now());
            usuario.setFechaAlta(null);
        } else{
          usuario.setFechaAlta(LocalDate.now());
          usuario.setFechaBaja(null);
        }
        usuario.setActivo(!usuario.getActivo());//desactivado
        Usuario usuarioDesactivado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioDesactivado);
    }


    //----------------DOCENTES---------------------------
    //docentes activos
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto>findAllActivosDocentes(){
        return usuarioRepository.findDocentesActivos()
                .stream()
                .map(usuario -> usuarioMapper.toDto(usuario))
                .collect((Collectors.toList()));
    }

    //consulta general docentes
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto>findAllDocentes(){
        return usuarioRepository.findDocentes()
                .stream()
                .map(usuario -> usuarioMapper.toDto(usuario))
                .collect((Collectors.toList()));
    }

    //--------ALUMNOS----------------------------------------
    //alumnos activos
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto>findAllActivosAlumnos(){
        return usuarioRepository.findAlumnosActivos()
                .stream()
                .map(usuario -> usuarioMapper.toDto(usuario))
                .collect((Collectors.toList()));
    }

    //consulta general alumnos
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto>findAllAlumnos(){
        return usuarioRepository.findAlumnos()
                .stream()
                .map(usuario -> usuarioMapper.toDto(usuario))
                .collect((Collectors.toList()));
    }
}
