package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoExpedienteResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Models.Entities.Documento;
import com.app.pga.App.Models.Entities.Documento_Expediente;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Enum.EstadoDocumento;
import com.app.pga.App.Models.Enum.EstadoExpediente;
import com.app.pga.App.Models.Mappers.DocumentoMapper;
import com.app.pga.App.Models.Mappers.Documento_ExpedienteMapper;
import com.app.pga.App.Models.Mappers.ExpedienteMapper;
import com.app.pga.App.Repositories.IAlumnoRepository;
import com.app.pga.App.Repositories.IDocumentoRepository;
import com.app.pga.App.Repositories.IDocumento_ExpedienteRepository;
import com.app.pga.App.Repositories.IExpedienteRepository;
import com.app.pga.App.Services.Interfaces.IExpedienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class ExpedienteService implements IExpedienteService {

    private final IExpedienteRepository expedienteRepository;
    private final IDocumento_ExpedienteRepository documento_expedienteRepository;
    private final IAlumnoRepository alumnoRepository;
    private final ExpedienteMapper expedienteMapper;
    private final Documento_ExpedienteMapper documentoExpedienteMapper;
    private final IDocumentoRepository documentoRepository;

    @Override
    public ExpedienteResponseDto verExpediente(Long idAlumno) {
        Expediente exp = obtenerPorAlumno(idAlumno);

        List<DocumentoExpedienteResponseDto> docs =
                documento_expedienteRepository.findByExpediente(exp)
                        .stream()
                        .map(d->documentoExpedienteMapper.toDto(d))
                        .toList();

        return new ExpedienteResponseDto(
                exp.getIdExpediente(),
                idAlumno,
                exp.getEstado(),
                exp.getObservaciones(),
                docs
        );

    }
    @Override
    public Expediente obtenerPorAlumno(Long idAlumno) {
        return expedienteRepository.findByAlumno_IdAlumno(idAlumno)
                .orElseGet(() -> crearExpediente(idAlumno));
    }
    @Override
    public Expediente crearExpediente(Long idAlumno) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new NotFoundException("Alumno no encontrado"));
        if(!alumno.getActivo())
            throw new ResourceDisabledException("Alumno Deshabilitado");
        Expediente exp = new Expediente();
        exp.setAlumno(alumno);
        exp.setEstado(EstadoExpediente.NO_APROBADO);

       List<Documento> catalogo = documentoRepository.findByActivoTrue();

       List<Documento_Expediente> documentos = catalogo.stream()
                .map(docCat -> {
                    Documento_Expediente doc = new Documento_Expediente();
                    doc.setExpediente(exp);
                    doc.setDocumento(docCat);
                    doc.setEstadoDocumento(EstadoDocumento.PENDIENTE);
                    return doc;
                })
                .toList();
        documento_expedienteRepository.saveAll(documentos);
        return expedienteRepository.save(exp);
    }
    @Override
    public ExpedienteResponseDto agregarObservaciones( Long idExpediente, String observaciones) {

        Expediente exp = expedienteRepository.findById(idExpediente)
                .orElseThrow(() -> new NotFoundException("Expediente no encontrado"));

        if (exp.getEstado() == EstadoExpediente.APROBADO) {
            throw new ResourceDisabledException("No se pueden agregar observaciones a un expediente aprobado");
        }
        exp.setObservaciones(observaciones);
        return expedienteMapper.toDto(expedienteRepository.save(exp));
    }
    @Override
    public void sincronizarExpediente(Expediente expediente) {
        List<Documento> documentosObligatorios = documentoRepository.findByActivoTrue();
        for (Documento doc : documentosObligatorios) {
            boolean existe = documento_expedienteRepository.existsByExpedienteAndDocumento(expediente, doc);
            if (!existe) {
                Documento_Expediente nuevo = new Documento_Expediente();
                nuevo.setExpediente(expediente);
                nuevo.setDocumento(doc);
                nuevo.setEstadoDocumento(EstadoDocumento.PENDIENTE);
                documento_expedienteRepository.save(nuevo);
            }
        }
    }



}
