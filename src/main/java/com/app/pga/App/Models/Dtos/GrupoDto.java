package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.EstadoEnum;
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
                       EstadoEnum estado,

                       LocalDate created_at,
                       String periodo,
                       CursoDto curso,
                       DocenteDto docente

) implements Serializable {
}