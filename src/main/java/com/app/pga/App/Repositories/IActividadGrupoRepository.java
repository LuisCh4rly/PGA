package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Entities.ActividadGrupo;
import com.app.pga.App.Models.Enum.Alcance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IActividadGrupoRepository extends JpaRepository<ActividadGrupo, Long> {
    List<ActividadGrupo> findByGrupo_IdGrupo(Long idGrupo);
    List<ActividadGrupo> findByGrupo_IdGrupoAndAlcance(Long idGrupo, Alcance alcance);

}