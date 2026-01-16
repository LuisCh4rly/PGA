package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.CampoFormativo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICampoFormativoRepository extends JpaRepository < CampoFormativo , Long> {

    boolean  existsByNombreEqualsIgnoreCase(String nombre);

    @Override
    Optional<CampoFormativo> findById (Long id);

    List<CampoFormativo> findByActivoTrue();

}
