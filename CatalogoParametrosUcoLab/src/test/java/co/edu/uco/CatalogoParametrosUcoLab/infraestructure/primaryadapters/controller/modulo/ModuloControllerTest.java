package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.modulo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.ActualizarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.consultarmodulo.primaryports.interactor.ConsultarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class ModuloControllerTest {

    private static final String RUTA_BASE = "/catalogo-parametros/api/v1/modulos";

    @Mock
    private CrearModuloInteractor crearModuloInteractor;
    @Mock
    private ConsultarModuloInteractor consultarModuloInteractor;
    @Mock
    private CrearModuloPublisher crearModuloPublisher;
    @Mock
    private ActualizarModuloInteractor actualizarModuloInteractor;
    @Mock
    private ActualizarModuloPublisher actualizarModuloPublisher;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new GlobalExceptionHandler(new TelemetryService(new SimpleMeterRegistry()),
                consultarMensajePort);
        webTestClient = WebTestClient.bindToController(
                new ModuloController(crearModuloInteractor, consultarModuloInteractor,
                        crearModuloPublisher, actualizarModuloInteractor, actualizarModuloPublisher))
                .controllerAdvice(handler)
                .build();
    }

    private String bodyJsonValido(final String nombre) {
        return "{\"nombre\":\"" + nombre + "\",\"idAplicacion\":\"" + UUID.randomUUID()
                + "\",\"activo\":\"true\",\"fechaInicio\":\"2026-01-01 00:00:00\",\"fechaFinal\":\"2026-12-31 23:59:59\"}";
    }

    private ModuloEntity entidad(final String nombre) {
        return ModuloEntity.create(UUID.randomUUID(), nombre, UUID.randomUUID(), true,
                LocalDateTime.now(), null);
    }

    @Test
    void debeCrearModuloYDevolver201() {
        webTestClient.post().uri(RUTA_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJsonValido("modulo"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Modulo creado exitosamente.");
    }

    @Test
    void debeDevolver400CuandoElCuerpoDeCrearNoEsValido() {
        webTestClient.post().uri(RUTA_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"\",\"idAplicacion\":\"" + UUID.randomUUID() + "\",\"activo\":\"true\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver200YLasEntidadesAlConsultarTodosLosModulos() {
        var modulo = entidad("modulo");
        when(consultarModuloInteractor.execute(anyInt(), anyInt())).thenReturn(List.of(modulo));

        webTestClient.get().uri(RUTA_BASE)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.modulos[0].nombre").isEqualTo("modulo");
    }

    @Test
    void debeDevolver200YLaEntidadCuandoExisteElId() {
        var modulo = entidad("modulo");
        when(consultarModuloInteractor.execute(any(UUID.class))).thenReturn(List.of(modulo));

        webTestClient.get().uri(RUTA_BASE + "/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.modulos[0].nombre").isEqualTo("modulo");
    }

    @Test
    void debeDevolver404CuandoNoExisteElId() {
        when(consultarModuloInteractor.execute(any(UUID.class))).thenReturn(List.of());

        webTestClient.get().uri(RUTA_BASE + "/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("No se encontro el modulo con el id especificado.");
    }

    @Test
    void debeActualizarModuloYDevolver200() {
        webTestClient.put().uri(RUTA_BASE + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJsonValido("modulo"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Modulo actualizado exitosamente.");
    }

    @Test
    void debeDevolver200CuandoSeConsultanLosEventos() {
        when(crearModuloPublisher.getStream()).thenReturn(Flux.empty());
        when(actualizarModuloPublisher.getStream()).thenReturn(Flux.empty());

        webTestClient.get().uri(RUTA_BASE + "/events")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);
    }
}