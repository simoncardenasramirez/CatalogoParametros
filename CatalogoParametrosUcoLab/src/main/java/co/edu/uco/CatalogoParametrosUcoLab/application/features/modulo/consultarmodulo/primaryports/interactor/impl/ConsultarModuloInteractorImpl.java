package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.consultarmodulo.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.consultarmodulo.primaryports.interactor.ConsultarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;

@Service
public class ConsultarModuloInteractorImpl implements ConsultarModuloInteractor {

    private final ModuloRepository moduloRepository;

    public ConsultarModuloInteractorImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public List<ModuloEntity> execute() {
        return moduloRepository.findAll();
    }

    @Override
    public List<ModuloEntity> execute(final int pagina, final int tamanoPagina) {
        final var paginaSegura = Math.max(pagina, 1);
        final var tamanoSeguro = Math.max(tamanoPagina, 1);
        return moduloRepository.findAllPaginado(paginaSegura, tamanoSeguro);
    }

    @Override
    public List<ModuloEntity> execute(final UUID id) {
        return moduloRepository.findById(id)
                .map(List::of)
                .orElse(List.of());
    }
}
