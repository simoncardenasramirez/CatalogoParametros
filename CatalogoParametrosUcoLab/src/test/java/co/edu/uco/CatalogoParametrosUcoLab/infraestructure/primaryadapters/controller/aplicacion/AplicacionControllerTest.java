package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.aplicacion;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.ActualizarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.consultaraplicacion.primaryports.interactor.ConsultarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.CrearAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.primaryports.interactor.EliminarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.publisher.EliminarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class AplicacionControllerTest {

    @Mock
    private CrearAplicacionInteractor crearAplicacionInteractor;
    @Mock
    private ConsultarAplicacionInteractor consultarAplicacionInteractor;
    @Mock
    private CrearAplicacionPublisher crearAplicacionPublisher;
    @Mock
    private ActualizarAplicacionInteractor actualizarAplicacionInteractor;
    @Mock
    private ActualizarAplicacionPublisher actualizarAplicacionPublisher;
    @Mock
    private EliminarAplicacionInteractor eliminarAplicacionInteractor;
    @Mock
    private EliminarAplicacionPublisher eliminarAplicacionPublisher;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new GlobalExceptionHandler(new TelemetryService(new SimpleMeterRegistry()),
                consultarMensajePort);
        webTestClient = WebTestClient.bindToController(
                new AplicacionController(crearAplicacionInteractor, consultarAplicacionInteractor,
                        crearAplicacionPublisher, actualizarAplicacionInteractor,
                        actualizarAplicacionPublisher, eliminarAplicacionInteractor,
                        eliminarAplicacionPublisher))
                .controllerAdvice(handler)
                .build();
    }

    private AplicacionEntity entidad(final String nombre) {
        return AplicacionEntity.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    private String bodyCrearValido() {
        var idOrganizacion = UUID.randomUUID();
        return "{\"nombre\":\"aplicacion\",\"idOrganizacion\":\"" + idOrganizacion
                + "\",\"activa\":\"true\",\"fechaInicio\":\"2024-01-01 00:00:00\",\"fechaFinal\":\"2024-12-31 23:59:59\"}";
    }

    @Test
    void debeCrearAplicacionYDevolver201() {
        webTestClient.post().uri("/catalogo-parametros/api/v1/aplicaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyCrearValido())
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Aplicacion creada exitosamente.");
    }

    @Test
    void debeDevolver400CuandoElCuerpoDeLaPeticionEsInvalido() {
        webTestClient.post().uri("/catalogo-parametros/api/v1/aplicaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"\",\"idOrganizacion\":\"no-es-uuid\",\"activa\":\"true\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver200YLasEntidadesAlConsultarTodas() {
        var aplicacion = entidad("aplicacion");
        when(consultarAplicacionInteractor.execute(1, 10)).thenReturn(List.of(aplicacion));

        webTestClient.get().uri("/catalogo-parametros/api/v1/aplicaciones")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.aplicaciones[0].nombre").isEqualTo("aplicacion");
    }

    @Test
    void debeDevolver200CuandoExisteLaAplicacionPorId() {
        var aplicacion = entidad("aplicacion");
        when(consultarAplicacionInteractor.execute(any(UUID.class))).thenReturn(List.of(aplicacion));

        webTestClient.get().uri("/catalogo-parametros/api/v1/aplicaciones/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.aplicaciones[0].nombre").isEqualTo("aplicacion");
    }

    @Test
    void debeDevolver404CuandoNoExisteLaAplicacionPorId() {
        when(consultarAplicacionInteractor.execute(any(UUID.class))).thenReturn(List.of());

        webTestClient.get().uri("/catalogo-parametros/api/v1/aplicaciones/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("No se encontro la aplicacion con el id especificado.");
    }

    @Test
    void debeActualizarAplicacionYDevolver200() {
        webTestClient.put().uri("/catalogo-parametros/api/v1/aplicaciones/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyCrearValido())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Aplicacion actualizada exitosamente.");
    }

    @Test
    void debeEliminarAplicacionYDevolver200() {
        webTestClient.delete().uri("/catalogo-parametros/api/v1/aplicaciones/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Aplicacion eliminada exitosamente.");
    }

    @Test
    void debeDevolver200YElComentarioConnectedAlConsultarLosEventos() {
        when(crearAplicacionPublisher.getStream()).thenReturn(Flux.empty());
        when(actualizarAplicacionPublisher.getStream()).thenReturn(Flux.empty());
        when(eliminarAplicacionPublisher.getStream()).thenReturn(Flux.empty());

        webTestClient.get().uri("/catalogo-parametros/api/v1/aplicaciones/events")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> assertTrue(result.getResponseBody().contains("connected")));
    }
}