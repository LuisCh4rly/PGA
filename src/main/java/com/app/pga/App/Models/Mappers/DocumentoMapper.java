package com.app.pga.App.Models.Mappers;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;
import com.app.pga.App.Models.Entities.Documento;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentoMapper {

    Documento toEntity(DocumentoRequestDto documentoRequestDto);

    DocumentoResponseDto toDto(Documento documento);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Documento partialUpdate(DocumentoResponseDto documentoResponseDto, @MappingTarget Documento documento);
}