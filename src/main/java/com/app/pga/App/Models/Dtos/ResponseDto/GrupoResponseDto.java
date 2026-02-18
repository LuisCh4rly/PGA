package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record GrupoResponseDto(

        Long idGrupo,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String nombre,
        @NotNull
        Estado estado,
        LocalDate created_at,
        String periodo,
        Long idCurso,
        String nombreCurso,
        Long  idUsuario,
        String docente

) implements Serializable {
}