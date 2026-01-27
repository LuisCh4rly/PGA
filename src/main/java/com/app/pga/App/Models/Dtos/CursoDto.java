package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Entities.Curso_ActividadBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Curso}
 */
public record CursoDto(Long idCurso,
                       @Size(message = "Máximo 60 carácteres", max = 60)
                       @NotBlank(message = "Campo obligatorio")
                       String nombre,

                       @Size(message = "Máximo 120 carácteres", max = 120)
                       @NotBlank String descripcion,

                       @JsonFormat (pattern = "dd/MM/yyyy")
                       LocalDate fechaAlta,

                       @JsonFormat (pattern = "dd/MM/yyyy")
                       LocalDate fechaBaja,

                       Boolean activo,

                       List<ActividadBaseDto> actividades

) implements Serializable {
}