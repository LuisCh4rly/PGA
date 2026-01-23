package com.app.pga.App.Models.Dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.ActividadBase}
 */
public record ActividadBaseDto(Long idActividad,
                               @Size(message = "Máximo 30 carácteres", max = 30)
                               @NotBlank(message = "Campo obligatorio")
                               String titulo,
                               @Size(message = "Máximo 60 carácteres", max = 60)
                               @NotBlank(message = "Campo obligatorio")
                               String descripcion,

                               Boolean activo,

                               @Valid
                               CampoFormativoDto campoFormativo

                               ) implements Serializable {
}