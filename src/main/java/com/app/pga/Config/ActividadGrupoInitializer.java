package com.app.pga.Config;

import com.app.pga.App.Models.Entities.ActividadGrupo;
import com.app.pga.App.Repositories.IActividadGrupoRepository;
import com.app.pga.App.Services.Implements.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActividadGrupoInitializer implements CommandLineRunner {
    private final IActividadGrupoRepository actividadGrupoRepository;
    private final StorageService storageService;

        public ActividadGrupoInitializer(IActividadGrupoRepository actividadGrupoRepository,
                                         StorageService storageService) {
            this.actividadGrupoRepository = actividadGrupoRepository;
            this.storageService = storageService;
        }

        @Override
        public void run(String... args) {
            System.out.println("Inicializando archivos de actividades grupo...");

            List<ActividadGrupo> lista = actividadGrupoRepository.findAll();

            for (ActividadGrupo ag : lista) {

                String ruta = ag.getUrlInstrucciones();

                if (ruta != null && ruta.startsWith("actividades/actividades-base")) {

                    String nuevaRuta = storageService.copiarInstruccionesActividadBase(
                            ruta,
                            ag.getIdActividadGrupo()
                    );

                    ag.setUrlInstrucciones(nuevaRuta);
                }
            }

            actividadGrupoRepository.saveAll(lista);
        }
}

