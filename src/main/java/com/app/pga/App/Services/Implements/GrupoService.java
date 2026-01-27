package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Docente;
import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Enum.EstadoEnum;
import com.app.pga.App.Models.Mappers.GrupoMapper;
import com.app.pga.App.Repositories.ICursoRepository;
import com.app.pga.App.Repositories.IDocenteRepository;
import com.app.pga.App.Repositories.IGrupoRepository;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class GrupoService implements IGrupoService {
    private final ICursoRepository cursoRepository;
    private final IDocenteRepository docenteRepository;
    private final GrupoMapper grupoMapper;
    private final IGrupoRepository grupoRepository;
    @Override

    public GrupoDto crear(GrupoDto dto) {
        Curso curso = cursoRepository.findById(dto.curso().idCurso())
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));
        if (!curso.getActivo()) {
            throw new IllegalStateException("Curso deshabilitado");
        }
       Docente docente = docenteRepository.findById(dto.docente().idDocente())
                .orElseThrow(() -> new NotFoundException("Docente no encontrado"));
        if (!docente.getActivo()) {
            throw new IllegalStateException("Docente deshabilitado");
        }
        Grupo grupo = grupoMapper.toEntity(dto);
        grupo.setEstado(EstadoEnum.HABILITADO);
        grupo.setCreated_at(LocalDate.now());
        grupo.setCurso(curso);
        grupo.setDocente(docente);
        return grupoMapper.toDtoActividades(grupoRepository.save(grupo));


    }
    public GrupoDto obtenerPorId(Long idGrupo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
        return grupoMapper.toDtoActividades(grupo);
    }

    @Override
    public List<GrupoDto> obtenerGrupoGeneral() {
        List <Grupo> grupos = grupoRepository.findAll();
        return grupos.stream()
                .map(a->grupoMapper.toDtoSimple(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<GrupoDto> obtenerPorCurso(Long idCurso) {
        return grupoRepository.findByCursoIdCurso(idCurso)
                .stream()
                .map(c->grupoMapper.toDtoSimple(c))
                .toList();
    }

    @Override
    public List<GrupoDto> obtenerPorDocente(Long idDocente) {
        return  grupoRepository.findByDocenteIdDocente(idDocente)
                .stream()
                .map(c->grupoMapper.toDtoSimple(c))
                .toList();
    }

    @Override
    public GrupoDto cambiarEstado(Long idGrupo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException(" Grupo no encontrado"));

    if (grupo.getEstado().equals(EstadoEnum.HABILITADO)) {
        grupo.setEstado(EstadoEnum.DESHABILITADO);
    }else{
        grupo.setEstado(EstadoEnum.HABILITADO);
    }

        return grupoMapper.toDtoSimple(grupoRepository.save(grupo));
    }

    @Override
    public GrupoDto cambiarDocente(GrupoDto dto) {
        Grupo grupo = grupoRepository.findById(dto.idGrupo())
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));

        Docente docente = docenteRepository.findById(dto.docente().idDocente())
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));

        if (!docente.getActivo()) {
            throw new IllegalStateException("Docente deshabilitado");
        }

        grupo.setDocente(docente);
        return grupoMapper.toDtoSimple(grupoRepository.save(grupo));
    }
}
