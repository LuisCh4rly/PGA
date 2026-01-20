package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.DocenteDto;
import com.app.pga.App.Models.Entities.Docente;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Mappers.DocenteMapper;
import com.app.pga.App.Repositories.IDocenteRepository;
import com.app.pga.App.Repositories.IUsuarioRepository;
import com.app.pga.App.Services.Interfaces.IDocenteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocenteService implements IDocenteService {
    private final DocenteMapper docenteMapper;
    private final IDocenteRepository docenteRepository;
    private final IUsuarioRepository iUsuarioRepository;

    public DocenteService(DocenteMapper docenteMapper, IDocenteRepository docenteRepository, IUsuarioRepository iUsuarioRepository){
        this.docenteMapper=docenteMapper;
        this.docenteRepository=docenteRepository;
        this.iUsuarioRepository=iUsuarioRepository;
    }

    //Crear docente
    public DocenteDto createDocente (DocenteDto docenteDto){
        Usuario usuario = iUsuarioRepository.findById(docenteDto.idUser()).orElseThrow(()-> new NotFoundException("Registro no encontrado"));

        docenteRepository.findByUsuario_IdUser(docenteDto.idUser()).ifPresent(DocenteDto->{
            throw new IllegalArgumentException("El usuario ya se encuentra registrado a un docente");
        });

        Docente docenteEntity = docenteMapper.toEntity(docenteDto);
        if(docenteEntity.getFechaAlta()==null){
            docenteEntity.setFechaAlta(Date.from(Instant.now()));
            docenteEntity.setActivo(true);
            docenteEntity.setUsuario(usuario);
        }
        Docente nuevoDocente = docenteRepository.save(docenteEntity);
        return docenteMapper.toDto(nuevoDocente);
    }


    //Consulta general para activos
    @Transactional(readOnly = true)
    public List<DocenteDto>findAllActivos(){
        return docenteRepository.findByActivoTrue()
                .stream()
                .map(docente ->docenteMapper.toDto(docente))
                .collect(Collectors.toList());
    }

    //Consulta general
    @Transactional(readOnly = true)
    public List<DocenteDto>findAll(){
        return docenteRepository.findAll()
                .stream()
                .map(docente ->docenteMapper.toDto(docente))
                .collect(Collectors.toList());
    }


    //Consulta por id
    @Transactional(readOnly = true)
    public DocenteDto findById(Long idDocente) {
        Docente docente = docenteRepository.findById(idDocente).orElseThrow(()->new NotFoundException("Registro no encontrado: "+idDocente));
        return docenteMapper.toDto(docente);
    }


    //Desactivar - Activar
    public DocenteDto desactivarActivarDocente (Long idDocente){
        Docente docente = docenteRepository.findById(idDocente).orElseThrow(()->new NotFoundException("Registro no encontrado: "+idDocente));
        if (docente.getActivo()==true){
            docente.setFechaBaja(Date.from(Instant.now()));
            docente.setFechaAlta(null);
        } else {
            docente.setFechaAlta(Date.from(Instant.now()));
            docente.setFechaBaja(null);
        }
        docente.setActivo(!docente.getActivo());//dasactivar activar
        Docente docenteActDes = docenteRepository.save(docente);
        return docenteMapper.toDto(docenteActDes);
    }

}