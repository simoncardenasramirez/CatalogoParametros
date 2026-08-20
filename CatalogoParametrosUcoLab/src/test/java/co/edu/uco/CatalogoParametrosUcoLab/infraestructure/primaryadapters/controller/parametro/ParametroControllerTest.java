package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametro.primaryports.interactor.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.EliminarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class ParametroControllerTest {

    @Mock
    private CrearParametroInteractor crearParametroInteractor;
    @Mock
    private ActualizarParametroInteractor actualizarParametroInteractor;
    @Mock
    private EliminarParametroInteractor eliminarParametroInteractor;
    @Mock
    private ConsultarParametroInteractor consultarParametroInteractor;
    @Mock
    private CrearParametroPublisher crearParametroPublisher;
    @Mock
    private ActualizarParametroPublisher actualizarParametroPublisher;
    @Mock
    private EliminarParametroPublisher eliminarParametroPublisher;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new GlobalExceptionHandler(new TelemetryService(new SimpleMeterRegistry()),
                consultarMensajePort);
        webTestClient = WebTestClient.bindToController(
                new ParametroController(crearParametroInteractor, actualizarParametroInteractor,
                        eliminarParametroInteractor, consultarParametroInteractor,
                        crearParametroPublisher, actualizarParametroPublisher, eliminarParametroPublisher))
                .controllerAdvice(handler)
                .build();
    }

    private String jsonParametroValido() {
        return "{\"nombre\":\"parametro\",\"idFuncionalidad\":\"" + UUID.randomUUID()
                + "\",\"idTipoParametro\":\"" + UUID.randomUUID() + "\",\"activo\":\"true\"}";
    }

    private ParametroEntity entidad() {
        return ParametroEntity.create(UUID.randomUUID(), "parametro", UUID.randomUUID(),
                UUID.randomUUID(), true);
    }

    @Test
    void debeCrearParametroYDevolver201() {
        webTestClient.post().uri("/catalogo-parametros/api/v1/parametros")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonParametroValido())
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Parametro creado exitosamente.");
    }

    @Test
    void debeDevolver400CuandoElInteractorLanzaValidationException() {
        org.mockito.Mockito.doThrow(ValidationException.build("El nombre del parametro es obligatorio."))
                .when(crearParametroInteractor).execute(any());

        webTestClient.post().uri("/catalogo-parametros/api/v1/parametros")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonParametroValido())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("El nombre del parametro es obligatorio.");
    }

    @Test
    void debeDevolver200YLasEntidadesAlConsultarTodos() {
        var entity = entidad();
        when(consultarParametroInteractor.execute(1, 10)).thenReturn(List.of(entity));

        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.parametros[0].nombre").isEqualTo("parametro");
    }

    @Test
    void debeDevolver200CuandoExisteElParametroPorId() {
        var entity = entidad();
        when(consultarParametroInteractor.execute(any(UUID.class))).thenReturn(List.of(entity));

        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.parametros[0].nombre").isEqualTo("parametro");
    }

    @Test
    void debeDevolver404CuandoNoExisteElParametroPorId() {
        when(consultarParametroInteractor.execute(any(UUID.class))).thenReturn(List.of());

        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("No se encontro el parametro con el id especificado.");
    }

    @Test
    void debeActualizarParametroYDevolver200() {
        webTestClient.put().uri("/catalogo-parametros/api/v1/parametros/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonParametroValido())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Parametro actualizado exitosamente.");
    }

    @Test
    void debeEliminarParametroYDevolver200() {
        webTestClient.delete().uri("/catalogo-parametros/api/v1/parametros/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Parametro eliminado exitosamente.");
    }

    @Test
    void debeExponerElStreamDeEventosCuandoSeConsultaEvents() {
        when(crearParametroPublisher.getStream()).thenReturn(Flux.empty());
        when(actualizarParametroPublisher.getStream()).thenReturn(Flux.empty());
        when(eliminarParametroPublisher.getStream()).thenReturn(Flux.empty());

        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros/events")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);
    }
}