package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia;

import java.time.OffsetDateTime;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import static co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia.*;

public enum RegistroVigenciaMapper {
    INSTANCE;

    private java.time.Instant instante(final OffsetDateTime fecha) {
        return fecha == null ? null : fecha.toInstant();
    }
    public RegistroVigencia toRegistro(final OrganizacionEntity e) {
        return new RegistroVigencia(ORGANIZACION, e.getId(), null, null, null, true,
                null, null);
    }
    public RegistroVigencia toRegistro(final AplicacionEntity e) {
        return new RegistroVigencia(APLICACION, e.getId(), e.getIdOrganizacion(), null, null, e.isActiva(),
                instante(e.getFechaInicio()), instante(e.getFechaFinal()));
    }
    public RegistroVigencia toRegistro(final ModuloEntity e) {
        return new RegistroVigencia(MODULO, e.getId(), e.getIdAplicacion(), null, null, e.isActivo(),
                instante(e.getFechaInicio()), instante(e.getFechaFinal()));
    }
    public RegistroVigencia toRegistro(final FuncionalidadEntity e) {
        return new RegistroVigencia(FUNCIONALIDAD, e.getId(), e.getIdModulo(), null, null, e.isActivo(),
                instante(e.getFechaInicio()), instante(e.getFechaFinal()));
    }
    public RegistroVigencia toRegistro(final ParametroEntity e) {
        return new RegistroVigencia(PARAMETRO, e.getId(), e.getIdFuncionalidad(), null, null, e.isActivo(), null, null);
    }
    public RegistroVigencia toRegistro(final MetadatoEntity e) {
        return new RegistroVigencia(METADATO, e.getId(), e.getIdParametro(), e.getIdAmbiente(), null, true, null, null);
    }
    public RegistroVigencia toRegistro(final MetadatoAmbienteEntity e) {
        return new RegistroVigencia(METADATO_AMBIENTE, e.getId(), e.getIdParametro(), e.getIdAmbiente(),
                e.getIdEstadoMetadatoAmbiente(), true, null, null);
    }
    public RegistroVigencia toRegistro(final TokenEntity e) {
        return new RegistroVigencia(TOKEN, e.getId(), e.getAplicacionId(), e.getAmbienteId(),
                e.getEstadoToken(), true, null, e.getFechaExpiracion() == null ? null
                        : e.getFechaExpiracion().toInstant());
    }
}
