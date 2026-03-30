package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Filtros.ActividadBaseFiltro;
import com.app.pga.App.Models.Mappers.ActividadBaseMapper;
import com.app.pga.App.Models.Specification.ActividadBaseSpecification;
import com.app.pga.App.Repositories.IActividadBaseRepository;
import com.app.pga.App.Repositories.ICampoFormativoRepository;
import com.app.pga.App.Services.Interfaces.IActividadBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class ActividadBaseService implements IActividadBaseService {

    private final IActividadBaseRepository actividadBaseRepository;
    private final ActividadBaseMapper actividadBaseMapper;
    private final ICampoFormativoRepository campoFormativoRepository;

    @Override
    public ActividadBaseResponseDto crearActividadBase(ActividadBaseRequestDto actividadBaseRequestDto) {
        CampoFormativo campoFormativo = campoFormativoRepository.findById(actividadBaseRequestDto.idCampoFormativo())
                .orElseThrow(()-> new NotFoundException("Campo formativo no encontrado"));
        if(!campoFormativo.getActivo()){
            throw new ResourceDisabledException("Campo formativo deshabilitado");
        }
        if(actividadBaseRepository.existsByTituloEqualsIgnoreCase(actividadBaseRequestDto.titulo())) {
            throw new DuplicateResourceException("Actividad Base existente");
        }

        ActividadBase actividadBaseNueva = actividadBaseMapper.toEntity(actividadBaseRequestDto);
        actividadBaseNueva.setActivo(true);
        actividadBaseNueva.setCampoFormativo(campoFormativo);
        return actividadBaseMapper.toDto(actividadBaseRepository.save(actividadBaseNueva));
    }


    @Override
    @Transactional(readOnly = true)
    public ActividadBaseResponseDto obtenerActividadBase(Long idActividadBase) {
        ActividadBase actividadBase = actividadBaseRepository.findById(idActividadBase)
                .orElseThrow(()-> new NotFoundException("Actividad Base no encontrada"));
        return actividadBaseMapper.toDto(actividadBase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActividadBaseResponseDto> obtenerActividadesBaseGeneral(ActividadBaseFiltro filtro, Pageable pageable) {
        Page <ActividadBase> actividadesBase = actividadBaseRepository.findAll(ActividadBaseSpecification.filtrar(filtro), pageable);
        return actividadesBase
                .map(a->actividadBaseMapper.toDto(a));

    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadBaseResponseDto> obtenerActividadesBaseActivas() {
        List <ActividadBase> actividadeActivas = actividadBaseRepository.findByActivoTrue();
        return actividadeActivas.stream()
                .map(a-> actividadBaseMapper.toDto(a))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadBaseResponseDto> obtenerActividadesBasePorCampo(Long idCampo) {
        List <ActividadBase> actividadesCampo = actividadBaseRepository.findByCampoFormativo_IdCampo(idCampo);
        return actividadesCampo.stream()
                .map(a-> actividadBaseMapper.toDto(a))
                .collect(Collectors.toList());
    }

    @Override  
    public ActividadBaseResponseDto actualizarActividadBase(ActividadBaseRequestDto actividadBaseRequestDto, Long idActividadBase) {
        ActividadBase actividadBase = actividadBaseRepository.findById(idActividadBase)
                .orElseThrow(()-> new NotFoundException("Actividad Base no encontrada"));
        if(!actividadBase.getActivo()){
            throw new ResourceDisabledException("No se puede modificar una actividad deshabilitada");
        }
        actividadBase.setDescripcion(actividadBaseRequestDto.descripcion());
        return actividadBaseMapper.toDto(actividadBaseRepository.save(actividadBase));
    }

    @Override
    public ActividadBaseResponseDto habitarDeshabilitar(Long idActividadBase) {
       ActividadBase actividadBase = actividadBaseRepository.findById(idActividadBase)
                .orElseThrow(()-> new NotFoundException("Actividad Base no encontrada"));

        if(!actividadBase.getCampoFormativo().getActivo()){
            throw new ResourceDisabledException("Campo formativo deshabilitado");
        }
        if (actividadBase.getActivo()){
            actividadBase.setActivo(false);
        }else{
            actividadBase.setActivo(true);
        }
        return actividadBaseMapper.toDto(actividadBaseRepository.save(actividadBase));
    }

    @Override
    public List<ActividadBaseResponseDto> habitarDeshabilitarPorCampo(Long idCampo, boolean estado) {
        List <ActividadBase> actividadesPorCampo = actividadBaseRepository.findByCampoFormativo_IdCampo(idCampo);

        return actividadesPorCampo.stream()
                .map(a-> {
                    a.setActivo(estado);
                    return actividadBaseMapper.toDto(a);
                })
                .collect(Collectors.toList());
    }
}
