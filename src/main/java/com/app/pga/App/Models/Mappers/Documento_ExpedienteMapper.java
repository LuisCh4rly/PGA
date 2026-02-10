package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoExpedienteResponseDto;
import com.app.pga.App.Models.Entities.Documento_Expediente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface Documento_ExpedienteMapper {
    Documento_Expediente toEntity(DocumentoExpedienteResponseDto documentoExpedienteResponseDto);

    @Mapping(target = "tipoDocumento", source = "documento.tipo")
    DocumentoExpedienteResponseDto toDto(Documento_Expediente documento_Expediente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Documento_Expediente partialUpdate(DocumentoExpedienteResponseDto documentoExpedienteResponseDto, @MappingTarget Documento_Expediente documento_Expediente);
}