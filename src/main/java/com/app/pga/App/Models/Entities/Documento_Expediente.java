package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.EstadoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "documentos_expedientes",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"id_expediente", "id_documento"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Documento_Expediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumentoExpediente;

    private String urlDocumento;

    @Enumerated(EnumType.STRING)
    private EstadoDocumento estadoDocumento;
    private LocalDate fechaRevision;
    private String observacion;
    private LocalDate fechaCarga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_expediente", referencedColumnName = "idExpediente")
    private Expediente expediente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_documento", referencedColumnName = "idDocumento")
    private Documento documento;

}
