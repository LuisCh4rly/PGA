package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Enum.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Grupo}
 */
public record GrupoDto(Long idGrupo,
                       @Size(message = "Máximo 60 carácteres", max = 60)
                       @NotBlank(message = "Campo obligatorio")
                       String nombre,

                       @NotNull
                       Estado estado,

                       LocalDate created_at,
                       String periodo,
                       CursoDto curso,
                       UsuarioResponseDto usuario

) implements Serializable {
}