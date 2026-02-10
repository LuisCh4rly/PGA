package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Documento;
import com.app.pga.App.Models.Entities.Documento_Expediente;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Enum.EstadoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IDocumento_ExpedienteRepository extends JpaRepository<Documento_Expediente, Long> {
   List <Documento_Expediente> findByExpediente(Expediente exp);

    Optional<Documento_Expediente> findByExpedienteAndDocumento(Expediente expediente, Documento documento);
    boolean existsByExpedienteAndDocumento(Expediente expediente, Documento documento);

    boolean existsByExpedienteAndEstadoDocumentoNot(Expediente exp, EstadoDocumento estadoDocumento);
}