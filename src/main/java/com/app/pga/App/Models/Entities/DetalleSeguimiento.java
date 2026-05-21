package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.EstadoTarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "detalle_seguimientos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetalleSeguimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleSeguimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarea estadoSemana;

    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 100, message = "El valor máximo es 100")
    private Long avanceEsperado;

    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 100, message = "El valor máximo es 100")
    private Long avanceReal;


    private String observacionesAlumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_actividad_alumno", referencedColumnName = "idActividadAlumno")
    private ActividadAlumno actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_seguimiento_semanal", referencedColumnName = "idSeguimientoSemanal")
    private SeguimentoSemanal semana;



}
