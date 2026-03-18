package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRevisionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoExpedienteResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Entities.Documento;
import com.app.pga.App.Models.Entities.Documento_Expediente;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Enum.EstadoDocumento;
import com.app.pga.App.Models.Enum.EstadoExpediente;
import com.app.pga.App.Models.Mappers.Documento_ExpedienteMapper;
import com.app.pga.App.Repositories.IDocumentoRepository;
import com.app.pga.App.Repositories.IDocumento_ExpedienteRepository;
import com.app.pga.App.Services.Interfaces.IDocumentoExpedienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
class DocuentoExpedienteService implements IDocumentoExpedienteService {
    private final IDocumentoRepository documentoRepository;
    private final StorageService storageService;
    private final ExpedienteService expedienteService;
    private final IDocumento_ExpedienteRepository documento_expedienteRepository;
    private final Documento_ExpedienteMapper documento_ExpedienteMapper;


    @Override
    public DocumentoExpedienteResponseDto subirDocumento(Long idAlumno, String tipoDocumento, MultipartFile archivo) {

        Documento tipo = documentoRepository.findByTipo (tipoDocumento)
                .orElseThrow(() -> new NotFoundException("Tipo de documento no válido"));
        Expediente expediente = expedienteService.obtenerPorAlumno(idAlumno);
        String url = storageService.guardarDocumentoExpediente( idAlumno, tipoDocumento, archivo);

        Documento_Expediente doc = documento_expedienteRepository.findByExpedienteAndDocumento(expediente, tipo)
                .orElse(new Documento_Expediente());

        doc.setExpediente(expediente);
        doc.setDocumento(tipo);
        doc.setUrlDocumento(url);
        doc.setEstadoDocumento(EstadoDocumento.PENDIENTE);
        doc.setFechaCarga(LocalDate.now());

        return documento_ExpedienteMapper.toDto(documento_expedienteRepository.save(doc));

    }

    @Override
    public void revisarDocumento(Long idDocumento, DocumentoRevisionDto dto) {

        Documento_Expediente doc = documento_expedienteRepository.findById(idDocumento)
                .orElseThrow(() -> new NotFoundException("Documento no encontrado"));

        doc.setEstadoDocumento(dto.estadoDocumento());
        doc.setObservacion(dto.observacion());
        doc.setFechaRevision(LocalDate.now());
        recalcularEstadoExpediente(doc.getExpediente());
    }

    public Resource verDocumento(Long idDocumentoExpediente){

        Documento_Expediente doc = documento_expedienteRepository.findById(idDocumentoExpediente)
                .orElseThrow(()-> new NotFoundException("Archivo no encontrado"));

        if(doc.getUrlDocumento()== null || doc.getUrlDocumento().isBlank()){
            throw new NotFoundException("No se ha subido el documento");
        }
        return storageService.loadAsResource(doc.getUrlDocumento());

    }


    private void recalcularEstadoExpediente(Expediente exp) {
        boolean todosAprobados = documento_expedienteRepository.existsByExpedienteAndEstadoDocumentoNot( exp, EstadoDocumento.APROBADO) == false;
        if (todosAprobados){
            exp.setEstado(EstadoExpediente.APROBADO);
        }else {
            exp.setEstado(EstadoExpediente.NO_APROBADO);
            exp.setObservaciones("");
        }
    }


}
