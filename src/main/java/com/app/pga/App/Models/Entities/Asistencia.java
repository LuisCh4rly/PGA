package com.app.pga.App.Models.Entities;


import com.app.pga.App.Models.Enum.EstadoAsistencia;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "asistencias")
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsistencia;
    @OneToOne
    @JoinColumn(name = "idSesionAlumno", nullable = false, unique = true)
    private SesionAlumno sesionAlumno;
    @Enumerated(EnumType.STRING)
    private EstadoAsistencia estado;

}
