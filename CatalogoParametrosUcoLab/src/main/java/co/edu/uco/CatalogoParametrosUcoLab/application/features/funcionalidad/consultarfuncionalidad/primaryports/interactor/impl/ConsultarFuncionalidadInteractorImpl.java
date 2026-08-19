package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.consultarfuncionalidad.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.consultarfuncionalidad.primaryports.interactor.ConsultarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;

@Service
public class ConsultarFuncionalidadInteractorImpl implements ConsultarFuncionalidadInteractor {

    private final FuncionalidadRepository funcionalidadRepository;

    public ConsultarFuncionalidadInteractorImpl(final FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    @Override
    public List<FuncionalidadEntity> execute() {
        return funcionalidadRepository.findAll();
    }

    @Override
    public List<FuncionalidadEntity> execute(final int pagina, final int tamanoPagina) {
        final var paginaSegura = Math.max(pagina, 1);
        final var tamanoSeguro = Math.max(tamanoPagina, 1);
        return funcionalidadRepository.findAllPaginado(paginaSegura, tamanoSeguro);
    }

    @Override
    public List<FuncionalidadEntity> execute(final UUID id) {
        return funcionalidadRepository.findById(id)
                .map(List::of)
                .orElse(List.of());
    }
}
