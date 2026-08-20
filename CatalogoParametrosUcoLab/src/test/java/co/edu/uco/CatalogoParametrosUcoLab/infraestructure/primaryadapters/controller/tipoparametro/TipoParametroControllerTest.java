package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.tipoparametro;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.tipoparametro.consultartipoparametro.primaryports.interactor.ConsultarTipoParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class TipoParametroControllerTest {

    @Mock
    private ConsultarTipoParametroInteractor consultarTipoParametroInteractor;

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new GlobalExceptionHandler(new TelemetryService(new SimpleMeterRegistry()),
                consultarMensajePort);
        webTestClient = WebTestClient
                .bindToController(new TipoParametroController(consultarTipoParametroInteractor))
                .controllerAdvice(handler)
                .build();
    }

    @Test
    void debeDevolver200ConLosTiposParametroCuandoSeConsultaLosTiposParametro() {
        var tipoParametro = TipoParametroEntity.create(UUID.randomUUID(), "Texto");
        when(consultarTipoParametroInteractor.execute()).thenReturn(List.of(tipoParametro));

        webTestClient.get().uri("/catalogo-parametros/api/v1/tipos-parametro")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.tiposParametro[0].nombre").isEqualTo("Texto");
    }

    @Test
    void debeDevolver200ConElTipoParametroCuandoExisteElId() {
        var id = UUID.randomUUID();
        var tipoParametro = TipoParametroEntity.create(id, "Numero");
        when(consultarTipoParametroInteractor.execute(id)).thenReturn(List.of(tipoParametro));

        webTestClient.get().uri("/catalogo-parametros/api/v1/tipos-parametro/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.tiposParametro[0].nombre").isEqualTo("Numero");
    }

    @Test
    void debeDevolver404ConMensajeCuandoNoExisteElTipoParametroPorId() {
        when(consultarTipoParametroInteractor.execute(any(UUID.class))).thenReturn(List.of());

        webTestClient.get().uri("/catalogo-parametros/api/v1/tipos-parametro/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("No se encontro el tipo de parametro con el id especificado.");
    }

    @Test
    void debeDevolver500ConMensajeCuandoOcurreUnErrorAlConsultarTodosLosTiposParametro() {
        when(consultarTipoParametroInteractor.execute()).thenThrow(new RuntimeException("error interno"));

        webTestClient.get().uri("/catalogo-parametros/api/v1/tipos-parametro")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("Ocurrio un error consultando los tipos de parametro.");
    }

    @Test
    void debeDevolver500ConMensajeCuandoOcurreUnErrorAlConsultarElTipoParametroPorId() {
        when(consultarTipoParametroInteractor.execute(any(UUID.class)))
                .thenThrow(new RuntimeException("error interno"));

        webTestClient.get().uri("/catalogo-parametros/api/v1/tipos-parametro/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("Ocurrio un error consultando el tipo de parametro.");
    }
}