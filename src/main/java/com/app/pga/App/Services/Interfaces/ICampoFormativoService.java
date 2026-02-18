package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICampoFormativoService {

    CampoResponseDto crearCampo (CampoRequestDto campoRequestDto);
    CampoResponseDto  obtenerCampo (Long idCampo);
    List<CampoResponseDto > obtenerCampos ();
    List<CampoResponseDto > obtenerCamposActivos ();
    CampoResponseDto  actualizarCampo( CampoRequestDto campoRequestDto, Long id);
    CampoResponseDto  habitarDeshabilitar(Long id);

}
