package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDocumentoRepository extends JpaRepository<Documento, Long> {
    boolean existsDocumentoByTipo(String tipo);
    Optional<Documento> findByTipo(String tipoDocumento);
    List<Documento> findByActivoTrue();
}