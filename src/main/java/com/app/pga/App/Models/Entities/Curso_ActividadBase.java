package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "cursosActividades")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Curso_ActividadBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne (optional = false)
        @JoinColumn(name="curso_id", referencedColumnName = "idCurso")
    private Curso curso;

    @ManyToOne (optional = false)
        @JoinColumn(name="actividad_base_id", referencedColumnName = "idActividad")
    private  ActividadBase actividadBase;

}
