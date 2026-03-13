package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
import com.app.pga.App.Models.Filtros.CampoFiltro;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICampoFormativoService {

    CampoResponseDto crearCampo (CampoRequestDto campoRequestDto);
    CampoResponseDto  obtenerCampo (Long idCampo);

    List<CampoResponseDto > obtenerCamposActivos ();
    CampoResponseDto  actualizarCampo( CampoRequestDto campoRequestDto, Long id);
    CampoResponseDto  habitarDeshabilitar(Long id);


    Page<CampoResponseDto > obtenerCampos (CampoFiltro filtro, Pageable pageable);
}
