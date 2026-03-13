package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;
import com.app.pga.App.Models.Entities.Documento;
import com.app.pga.App.Models.Entities.Documento_Expediente;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Enum.EstadoDocumento;
import com.app.pga.App.Models.Filtros.DocumentoFiltro;
import com.app.pga.App.Models.Mappers.DocumentoMapper;
import com.app.pga.App.Models.Specification.DocumentoSpecification;
import com.app.pga.App.Repositories.IDocumentoRepository;
import com.app.pga.App.Repositories.IDocumento_ExpedienteRepository;
import com.app.pga.App.Repositories.IExpedienteRepository;
import com.app.pga.App.Services.Interfaces.IDocumentoService;
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
class DocumentoService implements IDocumentoService {
    private final IDocumentoRepository documentoRepository;
    private final DocumentoMapper documentoMapper;
    private final IExpedienteRepository expedienteRepository;
    private final IDocumento_ExpedienteRepository documento_expedienteRepository;

    @Override
    public DocumentoResponseDto crearDocumento(DocumentoRequestDto dto) {
        if(documentoRepository.existsDocumentoByTipo(dto.tipo())){
            throw new DuplicateResourceException("Tipo de Documento ya registrado");
        }
        Documento documento = documentoMapper.toEntity(dto);
        documento.setActivo(true);
        Documento doc = documentoRepository.save(documento);

       List<Expediente> expedientes = expedienteRepository.findExpedientesAlumnoActivo();
       List<Documento_Expediente> documentos = expedientes.stream()
                .map(exp-> {
                    Documento_Expediente d = new Documento_Expediente();
                    d.setExpediente(exp);
                    d.setDocumento(doc);
                    d.setEstadoDocumento(EstadoDocumento.PENDIENTE);
                    return d;
                })
                .toList();

        documento_expedienteRepository.saveAll(documentos);

        return documentoMapper.toDto(doc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoResponseDto> obtenerDocumentos() {
        return documentoRepository.findAll()
                .stream()
                .map(d-> documentoMapper.toDto(d))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoResponseDto obtenerDocumento(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de Documento no encontrado"));
        return documentoMapper.toDto(documentoRepository.save(documento));
    }

    @Override
    public DocumentoResponseDto actualizarDocumento(Long id, DocumentoRequestDto dto) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de Documento no encontrado"));
        if(!documento.getActivo()){
            throw new ResourceDisabledException("Documento deshabilitado");
        }
        documento.setNombre(dto.nombre());
        documento.setObligatorio(dto.obligatorio());
        return documentoMapper.toDto(documentoRepository.save(documento)) ;
    }

    @Override
    public void habilitarDeshabilitar(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Tipo de Documento no encontrado"));
        if (documento.getActivo()){
            documento.setActivo(false);
        }else{
           documento.setActivo(true);
        }
    }

    //lista con paginacion y uso de api criteria
    public Page<DocumentoResponseDto> findAll(DocumentoFiltro filtro, Pageable pageable){
        Specification<Documento> spec = DocumentoSpecification.filtrarDocumentos(filtro);
        Page<Documento> documentos = documentoRepository.findAll(spec, pageable);
        return documentos.map(documentoMapper::toDto);
    }

}
