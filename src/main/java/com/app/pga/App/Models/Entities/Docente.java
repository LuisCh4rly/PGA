package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="docentes")
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocente;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private Boolean activo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_usuario")//relacion con usuario FK
    private Usuario usuario;

}
