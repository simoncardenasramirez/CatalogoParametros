package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaFila;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLibro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.impl.CargarCatalogoMasivoImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.bulk.CargaMasivaExcelPort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CargaMasivaParametrosRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import tools.jackson.databind.ObjectMapper;

class CargarCatalogoMasivoImplTest {
    private final CargaMasivaExcelPort excel = Mockito.mock(CargaMasivaExcelPort.class);
    private final CargaMasivaParametrosRepository lote = Mockito.mock(CargaMasivaParametrosRepository.class);
    private final AplicacionRepository aplicaciones = Mockito.mock(AplicacionRepository.class);
    private final ModuloRepository modulos = Mockito.mock(ModuloRepository.class);
    private final FuncionalidadRepository funcionalidades = Mockito.mock(FuncionalidadRepository.class);
    private final ParametroRepository parametros = Mockito.mock(ParametroRepository.class);
    private final TipoParametroRepository tiposParametro = Mockito.mock(TipoParametroRepository.class);
    private final MetadatoRepository metadatos = Mockito.mock(MetadatoRepository.class);
    private final TipoMetadatoRepository tiposMetadato = Mockito.mock(TipoMetadatoRepository.class);
    private final AmbienteRepository ambientes = Mockito.mock(AmbienteRepository.class);
    private final EstadoMetadatoAmbienteRepository estados = Mockito.mock(EstadoMetadatoAmbienteRepository.class);
    private final MetadatoAmbienteRepository metadatosAmbiente = Mockito.mock(MetadatoAmbienteRepository.class);
    private final CrearFuncionalidadPublisher publisherFuncionalidad = Mockito.mock(CrearFuncionalidadPublisher.class);
    private final CrearParametroPublisher publisherParametro = Mockito.mock(CrearParametroPublisher.class);
    private final CrearMetadatoPublisher publisherMetadato = Mockito.mock(CrearMetadatoPublisher.class);
    private final CrearMetadatoAmbientePublisher publisherAmbiente = Mockito.mock(CrearMetadatoAmbientePublisher.class);
    private final CargarCatalogoMasivoImpl useCase;
    private final UUID appId = UUID.randomUUID();
    private final UUID moduloId = UUID.randomUUID();
    private final UUID tipoParametroId = UUID.randomUUID();
    private final UUID tipoMetadatoId = UUID.randomUUID();
    private final UUID ambienteId = UUID.randomUUID();
    private final UUID estadoId = UUID.randomUUID();

    CargarCatalogoMasivoImplTest() {
        useCase = new CargarCatalogoMasivoImpl(excel, lote, aplicaciones, modulos, funcionalidades, parametros,
                tiposParametro, metadatos, tiposMetadato, ambientes, estados, metadatosAmbiente,
                publisherFuncionalidad, publisherParametro, publisherMetadato, publisherAmbiente,
                new TelemetryService(new SimpleMeterRegistry()));
    }

    @BeforeEach
    void prepararReferencias() {
        when(aplicaciones.findAll()).thenReturn(List.of(AplicacionEntity.create(appId, "Aplicacion", UUID.randomUUID(), true, null, null)));
        when(modulos.findAll()).thenReturn(List.of(ModuloEntity.create(moduloId, "Modulo", appId, true, null, null)));
        when(funcionalidades.findAll()).thenReturn(List.of());
        when(parametros.findAll()).thenReturn(List.of());
        when(tiposParametro.findAll()).thenReturn(List.of(TipoParametroEntity.create(tipoParametroId, "texto")));
        when(metadatos.findAll()).thenReturn(List.of());
        when(tiposMetadato.findAll()).thenReturn(List.of(TipoMetadatoEntity.create(tipoMetadatoId, "json", "Objeto JSON")));
        when(ambientes.findAll()).thenReturn(List.of(AmbienteEntity.create(ambienteId, "dev")));
        when(estados.findAll()).thenReturn(List.of(EstadoMetadatoAmbienteEntity.create(estadoId, "vigente")));
        when(metadatosAmbiente.findAll()).thenReturn(List.of());
        doNothing().when(lote).guardar(any());
    }

    @Test
    void debeValidarYGuardarTodoElLoteCuandoLasReferenciasExisten() {
        when(excel.leer(any())).thenReturn(new CargaMasivaLibro(List.of(filaValida()), List.of()));

        var resultado = useCase.validar(new byte[] { 1 });
        assertTrue(resultado.ready());
        assertEquals(1, resultado.filasValidas());
        assertEquals(0, resultado.filasInvalidas());

        var cargado = useCase.cargar(new byte[] { 1 });
        assertTrue(cargado.ready());
        verify(lote).guardar(any());
        verify(publisherFuncionalidad).sendEvent(any());
        verify(publisherParametro).sendEvent(any());
        verify(publisherMetadato).sendEvent(any());
        verify(publisherAmbiente).sendEvent(any());
    }

    @Test
    void debeRechazarSinGuardarCuandoLaAplicacionNoExiste() {
        when(excel.leer(any())).thenReturn(new CargaMasivaLibro(List.of(filaValida()), List.of()));
        when(aplicaciones.findAll()).thenReturn(List.of());

        var resultado = useCase.cargar(new byte[] { 1 });

        assertFalse(resultado.ready());
        assertEquals("APPLICATION_NOT_FOUND", resultado.errores().getFirst().codigo());
        verifyNoInteractions(lote, publisherFuncionalidad, publisherParametro, publisherMetadato, publisherAmbiente);
    }

    @Test
    void debeRechazarCuandoElParametroExistenteTieneOtroEstadoOTipo() {
        var funcionId = UUID.randomUUID();
        when(funcionalidades.findAll()).thenReturn(List.of(co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity
                .create(funcionId, "Funcion", moduloId, true, null, null)));
        when(parametros.findAll()).thenReturn(List.of(co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity
                .create(UUID.randomUUID(), "Parametro", funcionId, tipoParametroId, false)));
        when(excel.leer(any())).thenReturn(new CargaMasivaLibro(List.of(filaValidaConFuncion(funcionId, "true")), List.of()));

        var resultado = useCase.validar(new byte[] { 1 });

        assertFalse(resultado.ready());
        assertEquals("PARAMETER_DATA_MISMATCH", resultado.errores().getFirst().codigo());
    }

    @Test
    void debeRechazarFechaInvalidaYValorNoCompatible() {
        var fila = new CargaMasivaFila(2, "Aplicacion", "Modulo", "Funcion", "fecha-invalida", "", "true",
                "Parametro", "texto", "true", "json", "dev", "vigente", new ObjectMapper().createObjectNode());
        when(excel.leer(any())).thenReturn(new CargaMasivaLibro(List.of(fila), List.of()));

        var resultado = useCase.validar(new byte[] { 1 });

        assertFalse(resultado.ready());
        assertEquals("INVALID_DATE", resultado.errores().getFirst().codigo());
    }

    private CargaMasivaFila filaValida() {
        return filaValidaConFuncion(UUID.randomUUID(), "true");
    }

    private CargaMasivaFila filaValidaConFuncion(final UUID ignored, final String activoParametro) {
        return new CargaMasivaFila(2, "Aplicacion", "Modulo", "Funcion", "", "", "true", "Parametro",
                "texto", activoParametro, "json", "dev", "vigente", new ObjectMapper().createObjectNode().put("clave", "valor"));
    }
}
