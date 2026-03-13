package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IDocumentoRepository extends JpaRepository<Documento, Long>, JpaSpecificationExecutor {
    boolean existsDocumentoByTipo(String tipo);
    Optional<Documento> findByTipo(String tipoDocumento);
    List<Documento> findByActivoTrue();
}