package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

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
    private Date fechaAlta;
    private Date fechaBaja;
    private Boolean activo;
    @OneToOne
    @JoinColumn(name="idUser")//relacion con usuario FK
    private Usuario usuario;

}
