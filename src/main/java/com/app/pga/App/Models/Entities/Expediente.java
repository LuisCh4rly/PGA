package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.EstadoExpediente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name= "expedientes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Expediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExpediente;

    @Enumerated(EnumType.STRING)
    private EstadoExpediente estado;

    private String observaciones;

    @OneToOne
    @JoinColumn(name = "id_alumno", unique = true)
    private Alumno alumno;

}
