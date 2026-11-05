package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.CargarCatalogoMasivo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.primaryports.interactor.CargaMasivaParametrosInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaResultado;

@Service
public final class CargaMasivaParametrosInteractorImpl implements CargaMasivaParametrosInteractor {
    private final CargarCatalogoMasivo useCase;

    public CargaMasivaParametrosInteractorImpl(final CargarCatalogoMasivo useCase) {
        this.useCase = useCase;
    }

    @Override public byte[] plantilla() { return useCase.plantilla(); }
    @Override public CargaMasivaResultado validar(final byte[] archivo) { return useCase.validar(archivo); }
    @Override public CargaMasivaResultado cargar(final byte[] archivo) { return useCase.cargar(archivo); }
    @Override public byte[] reporteErrores(final byte[] archivo) { return useCase.reporteErrores(archivo); }
}
