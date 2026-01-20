package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.UsuarioDto;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Mappers.UsuarioMapper;
import com.app.pga.App.Repositories.IUsuarioRepository;
import com.app.pga.App.Services.IUsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    public UsuarioDto createUsuario (UsuarioDto usuarioDto){

        usuarioRepository.findByNombreAndApellidoPaternoAndApellidoMaterno(usuarioDto.nombre(), usuarioDto.apellidoPaterno(), usuarioDto.apellidoMaterno()).ifPresent(UsuarioDto -> {
            throw new IllegalArgumentException("El registro ya existe");
        });

        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDto);
        if (usuarioEntity.getCreated_At()==null){
            usuarioEntity.setCreated_At(Date.from(Instant.now()));
            usuarioEntity.setActivo(true);//nuevo usuario inicia como activo
        }
        Usuario nuevoUsuario = usuarioRepository.save(usuarioEntity);


        return usuarioMapper.toDto(nuevoUsuario);
    }

    //Actualizar usuario
    public UsuarioDto actualizarUsuario (Long idUsuario, UsuarioDto usuarioDto){

        Usuario usuarioExistente = usuarioRepository.findById(idUsuario).orElseThrow(() -> new NotFoundException("Registro no encontrado: "+ idUsuario));

        if(!usuarioExistente.getActivo()){
            throw new IllegalStateException("No se puede modificar un registro inactivo");
        }

        usuarioExistente.setDireccion(usuarioDto.direccion());
        usuarioExistente.setTelefono(usuarioDto.telefono());

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDto(usuarioActualizado);
    }


    //Consulta general para activos
   @Transactional(readOnly = true)
   public List<UsuarioDto> findAllActivos(){
     return usuarioRepository.findByActivoTrue()
             .stream().map(usuario -> usuarioMapper.toDto(usuario))
             .collect(Collectors.toList());
   }

    //Consulta general
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll(){
        return usuarioRepository.findAll()
                .stream().map(usuario -> usuarioMapper.toDto(usuario))
                .collect(Collectors.toList());
    }

   //Consulta por id
   @Transactional(readOnly = true)
   public UsuarioDto findById (Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(()-> new NotFoundException("Registro no encontrado: "+ idUsuario));
        return usuarioMapper.toDto(usuario);
   }

   //Desactivar usuario
    public UsuarioDto desactivarUsuario (Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(()-> new NotFoundException("Registro no encontrado: "+ idUsuario));
        usuario.setActivo(!usuario.getActivo());//desactivado
        Usuario usuarioDesactivado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioDesactivado);
    }
}
