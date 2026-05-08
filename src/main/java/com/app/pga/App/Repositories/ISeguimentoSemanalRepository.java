package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Entities.SeguimentoSemanal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ISeguimentoSemanalRepository extends JpaRepository<SeguimentoSemanal, Long>, JpaSpecificationExecutor<SeguimentoSemanal> {

    Boolean existsByInscripcion_IdInscripcionAndNumeroSemana(Long idInscripcion, Long numeroSemana);
    SeguimentoSemanal findTopByInscripcion_IdInscripcionAndNumeroSemanaLessThanOrderByNumeroSemanaDesc(Long idInscripcion, Long numeroSemana);
    Boolean existsByInscripcionAndSemanaInicioLessThanEqualAndSemanaFinGreaterThanEqual(Inscripcion inscripcion, LocalDate fin, LocalDate inicio);

    Optional <SeguimentoSemanal> findById (Long idSemana);

    @Query("""
    SELECT NEW com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto(
    s.idSeguimientoSemanal,
    s.numeroSemana,
    s.semanaInicio,
    s.semanaFin,
    s.fechaLimiteEdicion,
    s.porcentajeAvance,
    null
    )
    FROM SeguimentoSemanal s
    WHERE s.inscripcion.idInscripcion = :idInscripcion
    ORDER BY s.numeroSemana DESC
    """)
    List<SeguimientoDashboardResponseDto> findSemanas(Long idInscripcion, Pageable pageable);
}