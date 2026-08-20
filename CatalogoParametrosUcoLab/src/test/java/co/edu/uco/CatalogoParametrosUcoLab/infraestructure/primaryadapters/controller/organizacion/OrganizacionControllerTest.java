package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.organizacion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.ActualizarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher.ActualizarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.consultarorganizacion.primaryports.interactor.ConsultarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.CrearOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.primaryports.interactor.EliminarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher.EliminarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class OrganizacionControllerTest {

    private static final String BASE = "/catalogo-parametros/api/v1/organizaciones";

    @Mock
    private CrearOrganizacionInteractor crearOrganizacionInteractor;
    @Mock
    private CrearOrganizacionPublisher crearOrganizacionPublisher;
    @Mock
    private ActualizarOrganizacionInteractor actualizarOrganizacionInteractor;
    @Mock
    private ActualizarOrganizacionPublisher actualizarOrganizacionPublisher;
    @Mock
    private EliminarOrganizacionInteractor eliminarOrganizacionInteractor;
    @Mock
    private EliminarOrganizacionPublisher eliminarOrganizacionPublisher;
    @Mock
    private ConsultarOrganizacionInteractor consultarOrganizacionInteractor;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new GlobalExceptionHandler(new TelemetryService(new SimpleMeterRegistry()),
                consultarMensajePort);
        webTestClient = WebTestClient.bindToController(
                new OrganizacionController(crearOrganizacionInteractor, crearOrganizacionPublisher,
                        actualizarOrganizacionInteractor, actualizarOrganizacionPublisher,
                        eliminarOrganizacionInteractor, eliminarOrganizacionPublisher,
                        consultarOrganizacionInteractor))
                .controllerAdvice(handler)
                .build();
    }

    @Test
    void debeCrearOrganizacionYDevolver201() {
        webTestClient.post().uri(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"organizacion\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Organizacion creada exitosamente.");
    }

    @Test
    void debeDevolver400CuandoLaValidacionFallaEnElInteractor() {
        doThrow(ValidationException.build("El nombre de la organizacion es obligatorio."))
                .when(crearOrganizacionInteractor).execute(any());

        webTestClient.post().uri(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"organizacion\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("El nombre de la organizacion es obligatorio.");
    }

    @Test
    void debeDevolver400CuandoElCuerpoDeLaPeticionEsInvalido() {
        webTestClient.post().uri(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"ab\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver200YLasOrganizacionesAlConsultarTodas() {
        var entity = OrganizacionEntity.create(UUID.randomUUID(), "organizacion");
        when(consultarOrganizacionInteractor.execute(1, 10)).thenReturn(List.of(entity));

        webTestClient.get().uri(BASE)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.organizaciones[0].nombre").isEqualTo("organizacion");
    }

    @Test
    void debeDevolver200YLaOrganizacionAlConsultarPorId() {
        var entity = OrganizacionEntity.create(UUID.randomUUID(), "organizacion");
        when(consultarOrganizacionInteractor.execute(any(UUID.class))).thenReturn(List.of(entity));

        webTestClient.get().uri(BASE + "/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.organizaciones[0].nombre").isEqualTo("organizacion");
    }

    @Test
    void debeDevolver404CuandoNoExisteLaOrganizacionPorId() {
        doThrow(NotFoundException.build("La organizacion con id no existe."))
                .when(consultarOrganizacionInteractor).execute(any(UUID.class));

        webTestClient.get().uri(BASE + "/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void debeActualizarOrganizacionYDevolver200() {
        webTestClient.put().uri(BASE + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"organizacion actualizada\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Organizacion actualizada exitosamente.");
    }

    @Test
    void debeEliminarOrganizacionYDevolver200() {
        webTestClient.delete().uri(BASE + "/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Organizacion eliminada exitosamente.");
    }

    @Test
    void debeDevolver200AlSuscribirseAlStreamDeEventos() {
        when(crearOrganizacionPublisher.getStream()).thenReturn(Flux.empty());
        when(actualizarOrganizacionPublisher.getStream()).thenReturn(Flux.empty());
        when(eliminarOrganizacionPublisher.getStream()).thenReturn(Flux.empty());

        webTestClient.get().uri(BASE + "/events")
                .exchange()
                .expectStatus().isOk();
    }
}