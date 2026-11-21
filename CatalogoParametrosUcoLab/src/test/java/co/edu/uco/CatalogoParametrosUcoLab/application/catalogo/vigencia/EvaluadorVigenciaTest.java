package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia;

import static co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class EvaluadorVigenciaTest {
    private final Instant ahora = Instant.parse("2026-10-23T17:00:00Z");
    private final UUID org = UUID.randomUUID();
    private final UUID app = UUID.randomUUID();
    private final UUID modulo = UUID.randomUUID();
    private final UUID funcion = UUID.randomUUID();
    private final UUID parametro = UUID.randomUUID();
    private final UUID ambiente = UUID.randomUUID();
    private final UUID asociacion = UUID.randomUUID();
    private final UUID activo = UUID.randomUUID();
    private final UUID inactivo = UUID.randomUUID();

    private RegistroVigencia registro(final TipoRegistroVigencia tipo, final UUID id, final UUID padre,
            final boolean habilitado, final Instant fin) {
        return new RegistroVigencia(tipo, id, padre, ambiente,
                tipo == METADATO_AMBIENTE ? activo : null, habilitado, null, fin);
    }

    private List<RegistroVigencia> cadena() {
        return new ArrayList<>(List.of(registro(ORGANIZACION, org, null, true, null),
                registro(APLICACION, app, org, true, ahora.plusSeconds(600)),
                registro(MODULO, modulo, app, true, null),
                registro(FUNCIONALIDAD, funcion, modulo, true, null),
                registro(PARAMETRO, parametro, funcion, true, null),
                registro(METADATO_AMBIENTE, asociacion, parametro, true, null)));
    }

    private EvaluadorVigencia evaluar(final List<RegistroVigencia> registros) {
        return new EvaluadorVigencia(new CatalogoVigencia(registros,
                Map.of(activo, "activo", inactivo, "inactivo")), ahora);
    }

    @Test void debeExponerParametrosSoloConAsociacionActivaPorAmbiente() {
        assertEquals(Set.of(parametro), evaluar(cadena()).parametrosDisponibles(app, ambiente));
        var registros = cadena();
        registros.set(5, new RegistroVigencia(METADATO_AMBIENTE, asociacion, parametro, ambiente,
                inactivo, true, null, null));
        assertTrue(evaluar(registros).parametrosDisponibles(app, ambiente).isEmpty());
    }

    @Test void debeDesactivarDependientesCuandoLaAplicacionVence() {
        var registros = cadena();
        registros.set(1, registro(APLICACION, app, org, true, ahora));
        var cambios = registros.stream().filter(evaluar(registros)::requiereDesactivacion)
                .map(RegistroVigencia::id).collect(java.util.stream.Collectors.toSet());
        assertEquals(Set.of(app, modulo, funcion, parametro, asociacion), cambios);
    }

    @Test void debeBloquearRegistroNuevoCuandoElPadreEstaInactivo() {
        var registros = cadena();
        registros.set(1, registro(APLICACION, app, org, false, null));
        assertThrows(ValidationException.class,
                () -> evaluar(registros).validar(registro(MODULO, UUID.randomUUID(), app, false, null), true));
    }

    @Test void debeRespetarZonaHorariaAlConvertirEntidad() {
        var entidad = AplicacionEntity.create(app, "Aplicacion", org, true, null,
                java.time.OffsetDateTime.parse("2026-10-23T12:00:00-05:00"));
        assertTrue(evaluar(cadena()).vencido(RegistroVigenciaMapper.INSTANCE.toRegistro(entidad)));
    }
}
