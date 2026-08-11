package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.consultaraplicacion.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.consultaraplicacion.primaryports.interactor.ConsultarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;

@Service
public class ConsultarAplicacionInteractorImpl implements ConsultarAplicacionInteractor {

    private final AplicacionRepository aplicacionRepository;

    public ConsultarAplicacionInteractorImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public List<AplicacionEntity> execute() {
        return aplicacionRepository.findAll();
    }

    @Override
    public List<AplicacionEntity> execute(final UUID id) {
        return aplicacionRepository.findById(id)
                .map(List::of)
                .orElse(List.of());
    }
}
