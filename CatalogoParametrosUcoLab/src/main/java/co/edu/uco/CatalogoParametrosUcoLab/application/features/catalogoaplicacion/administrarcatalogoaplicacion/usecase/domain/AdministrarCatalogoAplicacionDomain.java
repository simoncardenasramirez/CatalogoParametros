package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class AdministrarCatalogoAplicacionDomain extends Domain {
    public enum Operacion { CREAR, ACTUALIZAR, ELIMINAR }
    private final UUID idAplicacion;
    private final UUID idPadre;
    private final RecursoCatalogoAplicacion recurso;
    private final Operacion operacion;
    private AdministrarCatalogoAplicacionDomain(final UUID id, final UUID idAplicacion, final UUID idPadre,
            final RecursoCatalogoAplicacion recurso, final Operacion operacion) {
        super(id);
        this.idAplicacion = UUIDHelper.getDefault(idAplicacion);
        this.idPadre = UUIDHelper.getDefault(idPadre);
        this.recurso = recurso;
        this.operacion = operacion;
    }
    public static AdministrarCatalogoAplicacionDomain create(final UUID id, final UUID idAplicacion,
            final UUID idPadre, final RecursoCatalogoAplicacion recurso, final Operacion operacion) {
        return new AdministrarCatalogoAplicacionDomain(id, idAplicacion, idPadre, recurso, operacion);
    }
    public UUID getIdAplicacion() { return idAplicacion; }
    public UUID getIdPadre() { return idPadre; }
    public RecursoCatalogoAplicacion getRecurso() { return recurso; }
    public Operacion getOperacion() { return operacion; }
}
