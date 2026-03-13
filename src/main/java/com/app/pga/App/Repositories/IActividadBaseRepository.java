package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Entities.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IActividadBaseRepository extends JpaRepository<ActividadBase, Long>, JpaSpecificationExecutor<ActividadBase> {
    boolean existsByTituloEqualsIgnoreCase(String titulo);
    Optional<ActividadBase> findById(Long aLong);
    List<ActividadBase> findByActivoTrue();


    List<ActividadBase> findByCampoFormativo_IdCampo(Long IdCampo);


    Page<ActividadBase> findAll(Specification<ActividadBase> filtrar, Pageable pageable );
}