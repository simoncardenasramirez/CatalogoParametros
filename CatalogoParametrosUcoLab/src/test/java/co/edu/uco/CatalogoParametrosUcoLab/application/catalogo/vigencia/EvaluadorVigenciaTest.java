package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.vigencia;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import static co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia.*;
import static org.junit.jupiter.api.Assertions.*;

class EvaluadorVigenciaTest {
    private final Instant ahora = Instant.parse("2026-10-23T17:00:00Z");
    private final UUID org = UUID.randomUUID(), app = UUID.randomUUID(), modulo = UUID.randomUUID(),
            funcion = UUID.randomUUID(), parametro = UUID.randomUUID(), ambiente = UUID.randomUUID(),
            metadato = UUID.randomUUID(), asociacion = UUID.randomUUID(), token = UUID.randomUUID(), activo = UUID.randomUUID(),
            inactivo = UUID.randomUUID(), revocado = UUID.randomUUID();

    private RegistroVigencia registro(TipoRegistroVigencia tipo, UUID id, UUID padre, boolean habilitado, Instant fin) {
        return new RegistroVigencia(tipo, id, padre, ambiente,
                tipo == TOKEN || tipo == METADATO_AMBIENTE ? activo : null, habilitado, null, fin);
    }
    private List<RegistroVigencia> cadena() {
        return new ArrayList<>(List.of(registro(ORGANIZACION, org, null, true, null),
                registro(APLICACION, app, org, true, ahora.plusSeconds(600)),
                registro(MODULO, modulo, app, true, null), registro(FUNCIONALIDAD, funcion, modulo, true, null),
                registro(PARAMETRO, parametro, funcion, true, null),
                registro(METADATO, metadato, parametro, true, null),
                registro(METADATO_AMBIENTE, asociacion, parametro, true, null),
                registro(TOKEN, token, app, true, ahora.plusSeconds(100))));
    }
    private EvaluadorVigencia evaluar(List<RegistroVigencia> registros) {
        return new EvaluadorVigencia(new CatalogoVigencia(registros,
                Map.of(activo,"activo",inactivo,"inactivo"), Map.of(activo,"activo",inactivo,"inactivo",revocado,"revocado")), ahora);
    }
    private Set<UUID> cambios(List<RegistroVigencia> registros) {
        var evaluador = evaluar(registros);
        return registros.stream().filter(evaluador::requiereDesactivacion).map(RegistroVigencia::id)
                .collect(java.util.stream.Collectors.toSet());
    }
    private void sustituir(List<RegistroVigencia> registros, RegistroVigencia registro) {
        registros.removeIf(r -> r.id().equals(registro.id()));
        registros.add(registro);
    }

    @Test void debeDesactivarCadenaCompletaCuandoAplicacionVenceEnElInstanteActual() {
        var registros = cadena();
        sustituir(registros, registro(APLICACION, app, org, true, ahora));
        assertEquals(Set.of(app,modulo,funcion,parametro,asociacion,token), cambios(registros));
        assertFalse(evaluar(registros).disponible(PARAMETRO,parametro));
    }
    @Test void debeAislarOtrasAplicacionesCuandoCompartenAmbiente() {
        var registros = cadena();
        sustituir(registros, registro(APLICACION, app, org, false, null));
        var otraApp = UUID.randomUUID();
        var otroToken = UUID.randomUUID();
        registros.add(registro(APLICACION,otraApp,org,true,null));
        registros.add(registro(TOKEN,otroToken,otraApp,true,ahora.plusSeconds(100)));
        assertFalse(cambios(registros).contains(otroToken));
        assertTrue(evaluar(registros).disponible(TOKEN,otroToken));
    }
    @Test void debeConservarTokenDeAplicacionCuandoDesactivaSoloUnModulo() {
        var registros = cadena();
        sustituir(registros, registro(MODULO,modulo,app,false,null));
        assertEquals(Set.of(funcion,parametro,asociacion),cambios(registros));
        assertTrue(evaluar(registros).disponible(TOKEN,token));
    }
    @Test void debeDesactivarAsociacionCuandoParametroEstaInactivo() {
        var registros = cadena();
        sustituir(registros, registro(PARAMETRO,parametro,funcion,false,null));
        assertEquals(Set.of(asociacion),cambios(registros));
        assertEquals(Set.of(),evaluar(registros).parametrosDisponibles(app,ambiente));
    }
    @Test void debeDesactivarTokenCuandoExpiraSinAlterarRevocados() {
        var registros = cadena();
        sustituir(registros,registro(TOKEN,token,app,true,ahora));
        var revocadoId=UUID.randomUUID();
        registros.add(new RegistroVigencia(TOKEN,revocadoId,app,ambiente,revocado,true,null,ahora.minusSeconds(1)));
        assertEquals(Set.of(token),cambios(registros));
    }
    @Test void debeEsperarFechaInicialSinDesactivarCuandoRegistroEstaProgramado() {
        var registros = cadena();
        sustituir(registros,new RegistroVigencia(APLICACION,app,org,null,null,true,ahora.plusSeconds(1),null));
        assertFalse(evaluar(registros).disponible(APLICACION,app));
        assertTrue(cambios(registros).isEmpty());
        var despues = new EvaluadorVigencia(new CatalogoVigencia(registros,Map.of(activo,"activo"),Map.of(activo,"activo")),
                ahora.plusSeconds(1));
        assertTrue(despues.disponible(APLICACION,app));
    }
    @Test void debeAceptarLimitesAbiertosCuandoFechasSonNulas() {
        var registros=cadena();
        sustituir(registros,registro(APLICACION,app,org,true,null));
        assertTrue(evaluar(registros).disponible(APLICACION,app));
        assertEquals(Set.of(parametro),evaluar(registros).parametrosDisponibles(app,ambiente));
    }
    @Test void debeBloquearCreacionYReactivacionCuandoPadreEstaInactivo() {
        var registros=cadena();
        sustituir(registros,registro(APLICACION,app,org,false,null));
        var nuevo=registro(MODULO,UUID.randomUUID(),app,false,null);
        assertThrows(ValidationException.class,()->evaluar(registros).validar(nuevo,true));
        assertThrows(ValidationException.class,()->evaluar(registros).validar(registro(MODULO,modulo,app,true,null),false));
        assertDoesNotThrow(()->evaluar(registros).validar(registro(MODULO,modulo,app,false,null),false));
    }
    @Test void debeRechazarReactivacionCuandoVigenciaYaTermino() {
        assertThrows(ValidationException.class,()->evaluar(cadena()).validar(registro(APLICACION,app,org,true,ahora),false));
    }
    @Test void debeConservarHijosInactivosCuandoReactivaPadre() {
        var registros=cadena();
        sustituir(registros,registro(MODULO,modulo,app,false,null));
        assertFalse(evaluar(registros).disponible(MODULO,modulo));
        assertFalse(cambios(registros).contains(modulo));
    }
    @Test void debeBloquearHuerfanosCuandoFaltaElPadre() {
        var registros=cadena();
        registros.removeIf(r->r.id().equals(app));
        assertFalse(evaluar(registros).disponible(TOKEN,token));
        assertTrue(cambios(registros).containsAll(Set.of(modulo,funcion,parametro,asociacion,token)));
    }
    @Test void elAmbienteDelMetadatoDeterminaLaPertenenciaDelParametro() {
        var registros=cadena();
        sustituir(registros,new RegistroVigencia(METADATO_AMBIENTE,asociacion,parametro,ambiente,inactivo,true,null,null));
        assertEquals(Set.of(parametro), evaluar(registros).parametrosDisponibles(app,ambiente));
        registros.removeIf(r -> r.id().equals(metadato));
        assertTrue(evaluar(registros).parametrosDisponibles(app,ambiente).isEmpty());
    }
    @Test void debeRespetarZonaHorariaCuandoComparaFechaFinal() {
        var entidad=AplicacionEntity.create(app,"Aplicacion",org,true,null,OffsetDateTime.parse("2026-10-23T12:00:00-05:00"));
        assertTrue(evaluar(cadena()).vencido(RegistroVigenciaMapper.INSTANCE.toRegistro(entidad)));
    }
    @Test void debeConservarOrganizacionesSinEstadoCuandoTienenFechasHistoricas() {
        var entidad=OrganizacionEntity.create(org,"Organizacion",null,OffsetDateTime.parse("2020-01-01T00:00:00Z"));
        var registro=RegistroVigenciaMapper.INSTANCE.toRegistro(entidad);
        assertNull(registro.fin());
        var registros=cadena();
        sustituir(registros,registro);
        assertTrue(evaluar(registros).disponible(APLICACION,app));
        assertFalse(evaluar(registros).requiereDesactivacion(registro));
    }
}
