package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.funcionalidad;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.ActualizarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.consultarfuncionalidad.primaryports.interactor.ConsultarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.EliminarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class FuncionalidadControllerTest {

    private static final String RUTA_BASE = "/catalogo-parametros/api/v1/funcionalidades";

    @Mock
    private CrearFuncionalidadInteractor crearFuncionalidadInteractor;
    @Mock
    private ActualizarFuncionalidadInteractor actualizarFuncionalidadInteractor;
    @Mock
    private EliminarFuncionalidadInteractor eliminarFuncionalidadInteractor;
    @Mock
    private ConsultarFuncionalidadInteractor consultarFuncionalidadInteractor;
    @Mock
    private CrearFuncionalidadPublisher crearFuncionalidadPublisher;
    @Mock
    private ActualizarFuncionalidadPublisher actualizarFuncionalidadPublisher;
    @Mock
    private EliminarFuncionalidadPublisher eliminarFuncionalidadPublisher;
    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new GlobalExceptionHandler(new TelemetryService(new SimpleMeterRegistry()),
                consultarMensajePort);
        webTestClient = WebTestClient
                .bindToController(new FuncionalidadController(crearFuncionalidadInteractor,
                        actualizarFuncionalidadInteractor, eliminarFuncionalidadInteractor,
                        consultarFuncionalidadInteractor, crearFuncionalidadPublisher,
                        actualizarFuncionalidadPublisher, eliminarFuncionalidadPublisher))
                .controllerAdvice(handler)
                .build();
    }

    private String bodyJsonValido() {
        return "{\"nombre\":\"funcionalidad\",\"idModulo\":\"" + UUID.randomUUID()
                + "\",\"activo\":\"true\",\"fechaInicio\":\"\",\"fechaFinal\":\"\"}";
    }

    @Test
    void debeDevolver201CuandoSeCreaUnaFuncionalidad() {
        webTestClient.post().uri(RUTA_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJsonValido())
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Funcionalidad creada exitosamente.");
    }

    @Test
    void debeDevolver400CuandoLaCreacionFallaPorValidacion() {
        doThrow(ValidationException.build("El nombre de la funcionalidad es obligatorio."))
                .when(crearFuncionalidadInteractor).execute(any(CrearFuncionalidadDtoRequest.class));

        webTestClient.post().uri(RUTA_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJsonValido())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver400CuandoElCuerpoDeLaCreacionEsInvalido() {
        webTestClient.post().uri(RUTA_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"\",\"idModulo\":\"1234\",\"activo\":\"true\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver200CuandoSeActualizaUnaFuncionalidad() {
        var id = UUID.randomUUID();

        webTestClient.put().uri(RUTA_BASE + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJsonValido())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Funcionalidad actualizada exitosamente.");
    }

    @Test
    void debeDevolver400CuandoLaActualizacionFallaPorValidacion() {
        var id = UUID.randomUUID();
        doThrow(ValidationException.build("El id de la funcionalidad es obligatorio para actualizar."))
                .when(actualizarFuncionalidadInteractor).execute(any(UUID.class),
                        any(ActualizarFuncionalidadDtoRequest.class));

        webTestClient.put().uri(RUTA_BASE + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJsonValido())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver200CuandoSeEliminaUnaFuncionalidad() {
        var id = UUID.randomUUID();

        webTestClient.delete().uri(RUTA_BASE + "/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Funcionalidad eliminada exitosamente.");
    }

    @Test
    void debeDevolver400CuandoLaEliminacionFallaPorValidacion() {
        var id = UUID.randomUUID();
        doThrow(ValidationException.build("El id de la funcionalidad es obligatorio para eliminar."))
                .when(eliminarFuncionalidadInteractor).execute(any(UUID.class));

        webTestClient.delete().uri(RUTA_BASE + "/{id}", id)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void debeDevolver200ConLasFuncionalidadesCuandoSeConsultaTodas() {
        var funcionalidad = FuncionalidadEntity.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true,
                null, null);
        when(consultarFuncionalidadInteractor.execute(anyInt(), anyInt())).thenReturn(List.of(funcionalidad));

        webTestClient.get().uri(RUTA_BASE)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.funcionalidades[0].nombre").isEqualTo("funcionalidad");
    }

    @Test
    void debeDevolver200ConLaFuncionalidadCuandoExisteElId() {
        var id = UUID.randomUUID();
        var funcionalidad = FuncionalidadEntity.create(id, "funcionalidad", UUID.randomUUID(), true, null, null);
        when(consultarFuncionalidadInteractor.execute(any(UUID.class))).thenReturn(List.of(funcionalidad));

        webTestClient.get().uri(RUTA_BASE + "/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.funcionalidades[0].nombre").isEqualTo("funcionalidad");
    }

    @Test
    void debeDevolver404ConMensajeCuandoNoExisteLaFuncionalidadPorId() {
        when(consultarFuncionalidadInteractor.execute(any(UUID.class))).thenReturn(List.of());

        webTestClient.get().uri(RUTA_BASE + "/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("No se encontro la funcionalidad con el id especificado.");
    }

    @Test
    void debeDevolver200CuandoSeEscuchanLosEventos() {
        when(crearFuncionalidadPublisher.getStream()).thenReturn(Flux.empty());
        when(actualizarFuncionalidadPublisher.getStream()).thenReturn(Flux.empty());
        when(eliminarFuncionalidadPublisher.getStream()).thenReturn(Flux.empty());

        webTestClient.get().uri(RUTA_BASE + "/events")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk();
    }
}