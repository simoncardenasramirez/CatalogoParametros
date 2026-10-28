package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia;

import java.time.Instant;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

/** Reglas de negocio puras. La vigencia es [inicio, fin); limites nulos son abiertos. */
public final class EvaluadorVigencia {
    private final CatalogoVigencia catalogo;
    private final Instant ahora;
    private final Map<TipoRegistroVigencia, Map<UUID, RegistroVigencia>> indice = new EnumMap<>(TipoRegistroVigencia.class);

    public EvaluadorVigencia(final CatalogoVigencia catalogo, final Instant ahora) {
        this.catalogo = catalogo;
        this.ahora = ahora;
        for (var tipo : TipoRegistroVigencia.values()) indice.put(tipo, new HashMap<>());
        catalogo.registros().forEach(r -> indice.get(r.tipo()).put(r.id(), r));
    }

    public RegistroVigencia buscar(final TipoRegistroVigencia tipo, final UUID id) {
        return indice.get(tipo).get(id);
    }

    public boolean activo(final RegistroVigencia registro) {
        return switch (registro.tipo()) {
            case METADATO_AMBIENTE -> esActivo(catalogo.estadosAsociacion().get(registro.idEstado()));
            case TOKEN -> esActivo(catalogo.estadosToken().get(registro.idEstado()));
            default -> registro.activo();
        };
    }

    private boolean esActivo(final String estado) {
        return estado != null && "activo".equalsIgnoreCase(estado.trim());
    }

    public boolean disponible(final TipoRegistroVigencia tipo, final UUID id) {
        var registro = buscar(tipo, id);
        return registro != null && activo(registro) && !vencido(registro)
                && (registro.inicio() == null || !ahora.isBefore(registro.inicio()))
                && padreDisponible(registro);
    }

    public boolean vencido(final RegistroVigencia registro) {
        return registro.fin() != null && !ahora.isBefore(registro.fin());
    }

    private boolean padreDisponible(final RegistroVigencia registro) {
        var tipoPadre = tipoPadre(registro.tipo());
        return tipoPadre == null || disponible(tipoPadre, registro.idPadre());
    }

    public TipoRegistroVigencia tipoPadre(final TipoRegistroVigencia tipo) {
        return switch (tipo) {
            case ORGANIZACION -> null;
            case APLICACION -> TipoRegistroVigencia.ORGANIZACION;
            case MODULO -> TipoRegistroVigencia.APLICACION;
            case FUNCIONALIDAD -> TipoRegistroVigencia.MODULO;
            case PARAMETRO -> TipoRegistroVigencia.FUNCIONALIDAD;
            case METADATO, METADATO_AMBIENTE -> TipoRegistroVigencia.PARAMETRO;
            case TOKEN -> TipoRegistroVigencia.APLICACION;
        };
    }

    /** Un inicio futuro impide el uso pero no borra una activacion programada. */
    public boolean requiereDesactivacion(final RegistroVigencia registro) {
        if (!activo(registro) || registro.tipo() == TipoRegistroVigencia.ORGANIZACION
                || registro.tipo() == TipoRegistroVigencia.METADATO) return false;
        return vencido(registro) || padreDesactivado(registro);
    }

    private boolean padreDesactivado(final RegistroVigencia registro) {
        var tipo = tipoPadre(registro.tipo());
        if (tipo == null) return false;
        var padre = buscar(tipo, registro.idPadre());
        return padre == null || !activo(padre) || vencido(padre) || padreDesactivado(padre);
    }

    public void validar(final RegistroVigencia registro, final boolean nuevo) {
        if (registro.inicio() != null && registro.fin() != null && registro.fin().isBefore(registro.inicio())) {
            throw ValidationException.build("La fecha final no puede ser anterior a la fecha de inicio.");
        }
        if (activo(registro) && registro.tipo() != TipoRegistroVigencia.ORGANIZACION && vencido(registro)) {
            throw ValidationException.build("No se puede activar un registro cuya vigencia ha terminado.");
        }
        var anterior = buscar(registro.tipo(), registro.id());
        boolean cambiaPadre = anterior == null || !java.util.Objects.equals(anterior.idPadre(), registro.idPadre())
                || !java.util.Objects.equals(anterior.idAmbiente(), registro.idAmbiente());
        // Se permite corregir o reactivar un inactivo; nunca vincularlo a un padre no disponible.
        if ((nuevo || activo(registro) || cambiaPadre) && !padreDisponible(registro)) {
            throw ValidationException.build("El registro padre esta inactivo o fuera de su vigencia.");
        }
    }

    public Set<UUID> parametrosDisponibles(final UUID aplicacion, final UUID ambiente) {
        if (!disponible(TipoRegistroVigencia.APLICACION, aplicacion)) return Set.of();
        return indice.get(TipoRegistroVigencia.METADATO).values().stream()
                .filter(r -> ambiente.equals(r.idAmbiente()) && disponible(r.tipo(), r.id()))
                .map(r -> buscar(TipoRegistroVigencia.PARAMETRO, r.idPadre()))
                .filter(p -> perteneceAplicacion(p, aplicacion))
                .map(RegistroVigencia::id).collect(Collectors.toUnmodifiableSet());
    }

    private boolean perteneceAplicacion(final RegistroVigencia parametro, final UUID aplicacion) {
        if (parametro == null) return false;
        var funcionalidad = buscar(TipoRegistroVigencia.FUNCIONALIDAD, parametro.idPadre());
        var modulo = funcionalidad == null ? null : buscar(TipoRegistroVigencia.MODULO, funcionalidad.idPadre());
        return modulo != null && aplicacion.equals(modulo.idPadre());
    }
}
