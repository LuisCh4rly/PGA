package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.CampoFormativoDto;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Mappers.CampoFormativoMapper;
import com.app.pga.App.Repositories.ICampoFormativoRepository;
import com.app.pga.App.Services.Interfaces.ICampoFormativoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CampoFormativoService implements ICampoFormativoService {


    private final ICampoFormativoRepository campoFormativoRepository;
    private final CampoFormativoMapper campoFormativoMapper;

    @Override
    public CampoFormativoDto crearCampo(CampoFormativoDto campoFormativoDto) {

        if(campoFormativoRepository.existsByNombreEqualsIgnoreCase(campoFormativoDto.nombre())) {
            throw new DataIntegrityViolationException("");
        }

        CampoFormativo campoNuevo = campoFormativoMapper.toEntity(campoFormativoDto);

        if(campoNuevo.getActivo() == null){
            campoNuevo.setActivo(true);
        }
        return campoFormativoMapper.toDto(campoFormativoRepository.save(campoNuevo));
    }

    @Override
    public CampoFormativoDto obtenerCampo(Long idCampo) {
        CampoFormativo campoFormativo = campoFormativoRepository.findById(idCampo)
                .orElseThrow(()->new NotFoundException("Registro no encontrado"));
        return campoFormativoMapper.toDto(campoFormativo);
    }

    @Override
    public List<CampoFormativoDto> obtenerCampos() {
        List <CampoFormativo>  camposFormativos = campoFormativoRepository.findAll();

        return camposFormativos
                .stream()
                .map(c-> campoFormativoMapper.toDto(c))
                .collect(Collectors.toList());
    }

    @Override
    public List<CampoFormativoDto> obtenerCamposActivos() {
        List <CampoFormativo>  camposFormativosActivos = campoFormativoRepository.findByActivoTrue();

        return camposFormativosActivos
                .stream()
                .map(c-> campoFormativoMapper.toDto(c))
                .collect(Collectors.toList());
    }

    @Override
    public CampoFormativoDto actualizarCampo(CampoFormativoDto campoFormativoDto, Long id) {
            CampoFormativo campoFormativo = campoFormativoRepository.findById(id)
                    .orElseThrow(()-> new NotFoundException("Registro no encontrado"));

            if(!campoFormativo.getActivo()){
                throw new IllegalStateException("No se puede modificar una campo deshabilitado");
            }

            campoFormativo.setDescripcion(campoFormativoDto.descripcion());

            return campoFormativoMapper.toDto(campoFormativoRepository.save(campoFormativo));
    }

    @Override
    public CampoFormativoDto habitarDeshabilitar(Long id) {
        CampoFormativo campoFormativo = campoFormativoRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Registro no encontrado"));

        if (campoFormativo.getActivo()){
            campoFormativo.setActivo(false);
        }else{
            campoFormativo.setActivo(true);
        }

        return campoFormativoMapper.toDto(campoFormativoRepository.save(campoFormativo));
    }


}
