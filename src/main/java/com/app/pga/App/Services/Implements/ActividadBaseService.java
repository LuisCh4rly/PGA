package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Mappers.ActividadBaseMapper;
import com.app.pga.App.Repositories.IActividadBaseRepository;
import com.app.pga.App.Repositories.ICampoFormativoRepository;
import com.app.pga.App.Services.Interfaces.IActividadBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ActividadBaseService implements IActividadBaseService {

    private final IActividadBaseRepository actividadBaseRepository;
    private final ActividadBaseMapper actividadBaseMapper;
    private final ICampoFormativoRepository campoFormativoRepository;

    @Override
    public ActividadBaseDto crearActividadBase(ActividadBaseDto actividadBaseDto) {
        CampoFormativo campoFormativo = campoFormativoRepository.findById(actividadBaseDto.campoFormativo().idCampo())
                .orElseThrow(()-> new NotFoundException("Campo formativo no encontrado"));
        if(!campoFormativo.getActivo()){
            throw new IllegalStateException("El campo formativo está inhabilitado");
        }
        if(actividadBaseRepository.existsByTituloEqualsIgnoreCase(actividadBaseDto.titulo())) {
            throw new IllegalArgumentException("El registro ya existe");
        }

        ActividadBase actividadBaseNueva = actividadBaseMapper.toEntity(actividadBaseDto);
        actividadBaseNueva.setActivo(true);
        actividadBaseNueva.setCampoFormativo(campoFormativo);
        return actividadBaseMapper.toDto(actividadBaseRepository.save(actividadBaseNueva));
    }

    @Override
    public ActividadBaseDto obtenerActividadBase(Long idActividadBase) {
        ActividadBase actividadBase = actividadBaseRepository.findById(idActividadBase)
                .orElseThrow(()-> new NotFoundException("Registro no encontrado"));
        return actividadBaseMapper.toDto(actividadBase);
    }

    @Override
    public List<ActividadBaseDto> obtenerActividadesBaseGeneral() {
        List <ActividadBase> actividadesBase = actividadBaseRepository.findAll();
        return actividadesBase.stream()
                .map(a->actividadBaseMapper.toDto(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadBaseDto> obtenerActividadesBaseActivas() {
        List <ActividadBase> actividadeActivas = actividadBaseRepository.findByActivoTrue();
        return actividadeActivas.stream()
                .map(a-> actividadBaseMapper.toDto(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadBaseDto> obtenerActividadesBasePorCampo(Long idCampo) {
        List <ActividadBase> actividadesCampo = actividadBaseRepository.findByCampoFormativo_IdCampo(idCampo);
        return actividadesCampo.stream()
                .map(a-> actividadBaseMapper.toDto(a))
                .collect(Collectors.toList());
    }

    @Override
    public ActividadBaseDto actualizarActividadBase(ActividadBaseDto actividadBaseDto, Long idActividadBase) {
        ActividadBase actividadBase = actividadBaseRepository.findById(idActividadBase)
                .orElseThrow(()-> new NotFoundException("Registro no encontrado"));
        if(!actividadBase.getActivo()){
            throw new IllegalStateException("No se puede modificar una actividad deshabilitada");
        }
        actividadBase.setDescripcion(actividadBaseDto.descripcion());
        return actividadBaseMapper.toDto(actividadBaseRepository.save(actividadBase));
    }

    @Override
    public ActividadBaseDto habitarDeshabilitar(Long idActividadBase) {
       ActividadBase actividadBase = actividadBaseRepository.findById(idActividadBase)
                .orElseThrow(()-> new NotFoundException("Registro no encontrado"));
        if(!actividadBase.getCampoFormativo().getActivo()){
            throw new IllegalStateException("No se puede modificar una actividad cuyo campo está deshabilitado");
        }
        if (actividadBase.getActivo()){
            actividadBase.setActivo(false);
        }else{
            actividadBase.setActivo(true);
        }
        return actividadBaseMapper.toDto(actividadBaseRepository.save(actividadBase));
    }

    @Override
    public List<ActividadBaseDto> habitarDeshabilitarPorCampo(Long idCampo, boolean estado) {
        List <ActividadBase> actividadesPorCampo = actividadBaseRepository.findByCampoFormativo_IdCampo(idCampo);

        return actividadesPorCampo.stream()
                .map(a-> {
                    a.setActivo(estado);
                    return actividadBaseMapper.toDto(a);
                })
                .collect(Collectors.toList());
    }
}
