package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.CampoFormativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IActividadBaseRepository extends JpaRepository<ActividadBase, Long> {
    boolean existsByTituloEqualsIgnoreCase(String titulo);
    Optional<ActividadBase> findById(Long aLong);
    List<ActividadBase> findByActivoTrue();

    List<ActividadBase> findByCampoFormativo_IdCampo(Long IdCampo);
}