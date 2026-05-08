package com.app.pga.App.Models.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name= "seguimientos_semanales")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeguimentoSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeguimientoSemanal;

    private Long numeroSemana;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate semanaInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate semanaFin;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaLimiteEdicion;

    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 100, message = "El valor máximo es 100")
    private Long porcentajeAvance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_inscripcion", referencedColumnName = "idInscripcion")
    private Inscripcion inscripcion;

    public boolean getEditable() {
        return false;
    }
}
