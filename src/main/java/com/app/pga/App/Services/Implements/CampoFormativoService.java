package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Filtros.CampoFiltro;
import com.app.pga.App.Models.Mappers.CampoFormativoMapper;
import com.app.pga.App.Models.Specification.CampoSpecification;
import com.app.pga.App.Repositories.ICampoFormativoRepository;
import com.app.pga.App.Services.Interfaces.ICampoFormativoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CampoFormativoService implements ICampoFormativoService {


    private final ICampoFormativoRepository campoFormativoRepository;
    private final CampoFormativoMapper campoFormativoMapper;
    private final ActividadBaseService actividadBaseService;

    @Override
    public CampoResponseDto crearCampo(CampoRequestDto campoRequestDto) {

        if(campoFormativoRepository.existsByNombreEqualsIgnoreCase(campoRequestDto.nombre())) {
            throw new DuplicateResourceException("Campo Formativo existente");
        }

        CampoFormativo campoNuevo = campoFormativoMapper.toEntity(campoRequestDto);

        if(campoNuevo.getActivo() == null){
            campoNuevo.setActivo(true);
        }
        return campoFormativoMapper.toDto(campoFormativoRepository.save(campoNuevo));
    }

    @Override
    @Transactional(readOnly = true)
    public CampoResponseDto  obtenerCampo(Long idCampo) {
        CampoFormativo campoFormativo = campoFormativoRepository.findById(idCampo)
                .orElseThrow(()->new NotFoundException("Campo Formativo no encontrado"));
        return campoFormativoMapper.toDto(campoFormativo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampoResponseDto > obtenerCampos(CampoFiltro filtro, Pageable pageable) {
        Page<CampoFormativo>  camposFormativos = campoFormativoRepository.findAll(CampoSpecification.filtrar(filtro), pageable);
        return camposFormativos
                .map(c-> campoFormativoMapper.toDto(c));

    }

    @Override
    @Transactional(readOnly = true)
    public List<CampoResponseDto > obtenerCamposActivos() {
        List <CampoFormativo>  camposFormativosActivos = campoFormativoRepository.findByActivoTrue();

        return camposFormativosActivos
                .stream()
                .map(c-> campoFormativoMapper.toDto(c))
                .collect(Collectors.toList());
    }

    @Override
    public CampoResponseDto  actualizarCampo(CampoRequestDto campoRequestDto, Long id) {
            CampoFormativo campoFormativo = campoFormativoRepository.findById(id)
                    .orElseThrow(()-> new NotFoundException("Campo Formativo no encontrado"));

            if(!campoFormativo.getActivo()){
                throw new ResourceDisabledException("Campo Formativo deshabilitado");
            }

            campoFormativo.setDescripcion(campoRequestDto.descripcion());

            return campoFormativoMapper.toDto(campoFormativoRepository.save(campoFormativo));
    }

    @Override
    public CampoResponseDto  habitarDeshabilitar(Long id) {
        CampoFormativo campoFormativo = campoFormativoRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Campo Formativo no encontrado"));

        if (campoFormativo.getActivo()){
            campoFormativo.setActivo(false);
            actividadBaseService.habitarDeshabilitarPorCampo(id,false);

        }else{
            campoFormativo.setActivo(true);
            actividadBaseService.habitarDeshabilitarPorCampo(id,true);
        }

        return campoFormativoMapper.toDto(campoFormativoRepository.save(campoFormativo));
    }


}
