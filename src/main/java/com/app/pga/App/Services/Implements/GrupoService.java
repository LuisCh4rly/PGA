package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.GrupoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;
import com.app.pga.App.Models.Entities.Curso;
import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Filtros.GrupoFiltro;
import com.app.pga.App.Models.Mappers.GrupoMapper;
import com.app.pga.App.Models.Specification.GrupoSpecification;
import com.app.pga.App.Repositories.ICursoRepository;
import com.app.pga.App.Repositories.IGrupoRepository;
import com.app.pga.App.Repositories.IUsuarioRepository;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class GrupoService implements IGrupoService {
    private final ICursoRepository cursoRepository;
    private final GrupoMapper grupoMapper;
    private final IGrupoRepository grupoRepository;
    private final ActividadGrupoService actividadGrupoService;
    private final IUsuarioRepository iUsuarioRepository;

    @Override

    public GrupoResponseDto crear(GrupoRequestDto dto) {
        if (grupoRepository.existsByNombreEqualsIgnoreCase(dto.nombre())) {
            throw new DuplicateResourceException("Grupo existente");
        }
        Curso curso = cursoRepository.findById(dto.idCurso())
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));
        if (!curso.getActivo()) {
            throw new ResourceDisabledException("Curso deshabilitado");
        }

        //se verifica que tenga rol docente
       Usuario usuario = iUsuarioRepository.findDocenteById(dto.idDocente())
                .orElseThrow(() -> new NotFoundException("Docente no encontrado"));
        if (!usuario.getActivo()) {
            throw new ResourceDisabledException("Docente deshabilitado");
        }
        Grupo grupo = grupoMapper.toEntity(dto);
        grupo.setEstado(Estado.HABILITADO);
        grupo.setCreated_at(LocalDate.now());
        grupo.setCurso(curso);
        grupo.setUsuario(usuario);
        Grupo nuevo = grupoRepository.save(grupo);
        actividadGrupoService.precargarDesdeCurso(nuevo.getIdGrupo());
        return grupoMapper.toDto(grupoRepository.save(grupo));


    }
    @Transactional(readOnly = true)
    public GrupoResponseDto obtenerPorId(Long idGrupo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
        return grupoMapper.toDto(grupo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDto> obtenerGrupoGeneral() {
        List <Grupo> grupos = grupoRepository.findAll();
        return grupos.stream()
                .map(a->grupoMapper.toDto(a))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDto> obtenerGruposActivos() {
        List<Grupo> grupos = grupoRepository.findByEstado(Estado.HABILITADO);
        return grupos.stream()
                .map(c -> grupoMapper.toDto(c))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDto> obtenerPorCurso(Long idCurso) {
        return grupoRepository.findByCursoIdCurso(idCurso)
                .stream()
                .map(c->grupoMapper.toDto(c))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDto> obtenerPorDocente(Long idDocente) {
        return  grupoRepository.findGruposByDocente(idDocente)
                .stream()
                .map(c->grupoMapper.toDto(c))
                .toList();
    }

    @Override
    public GrupoResponseDto cambiarEstado(Long idGrupo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException(" Grupo no encontrado"));

    if (grupo.getEstado().equals(Estado.HABILITADO)) {
        grupo.setEstado(Estado.DESHABILITADO);
    }else{
        grupo.setEstado(Estado.HABILITADO);
    }

        return grupoMapper.toDto(grupoRepository.save(grupo));
    }

    @Override
    public GrupoResponseDto cambiarDocente(Long idGrupo, Long idDocente) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));

        Usuario usuario = iUsuarioRepository.findDocenteById(idDocente)
                .orElseThrow(() -> new NotFoundException("Docente no encontrado"));

        if (!usuario.getActivo()) {
            throw new ResourceDisabledException("Docente deshabilitado");
        }

        grupo.setUsuario(usuario);
        return grupoMapper.toDto(grupoRepository.save(grupo));
    }

    //lista con paginacion y uso de api criteria
    public Page<GrupoResponseDto> findAll(GrupoFiltro filtro, Pageable pageable){

        Specification<Grupo> spec = GrupoSpecification.filtrarGrupos(filtro);
        Page<Grupo> grupos = grupoRepository.findAll(spec, pageable);
        return grupos.map(grupoMapper::toDto);
    }
}
