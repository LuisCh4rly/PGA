package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "catalogo_documentos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;

    @Column(unique = true, nullable = false)
    private String tipo;
    private String nombre;
    private Boolean obligatorio;
    private Boolean activo;
}
