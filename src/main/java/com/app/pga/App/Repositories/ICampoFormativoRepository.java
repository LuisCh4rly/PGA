package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.CampoFormativo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ICampoFormativoRepository extends JpaRepository < CampoFormativo , Long>, JpaSpecificationExecutor<CampoFormativo> {

    boolean  existsByNombreEqualsIgnoreCase(String nombre);
    Optional<CampoFormativo> findById (Long id);
    List<CampoFormativo> findByActivoTrue();

    Page <CampoFormativo> findAll (Specification<CampoFormativo> filtrar, Pageable pageable);
}
