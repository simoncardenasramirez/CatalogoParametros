package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.codec.DecodingException;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import reactor.core.publisher.Mono;
import tools.jackson.databind.exc.InvalidFormatException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var telemetryService = new TelemetryService(new SimpleMeterRegistry());
        var handler = new GlobalExceptionHandler(telemetryService, consultarMensajePort);
        webTestClient = WebTestClient.bindToController(new ControladorPrueba())
                .controllerAdvice(handler)
                .build();
    }

    @Test
    void debeDevolver400ConElMensajeCuandoOcurreValidationException() {
        webTestClient.get().uri("/prueba/validacion")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("El nombre del tipo de parametro es obligatorio.");
    }

    @Test
    void debeDevolver404ConElMensajeCuandoOcurreNotFoundException() {
        webTestClient.get().uri("/prueba/no-encontrado")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("No existe el tipo de parametro.");
    }

    @Test
    void debeDevolver409ConElMensajeCuandoOcurreConflictException() {
        webTestClient.get().uri("/prueba/conflicto")
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("Ya existe un tipo de parametro con ese nombre.");
    }

    @Test
    void debeDevolver500ConElMensajeCuandoOcurreTechnicalException() {
        webTestClient.get().uri("/prueba/tecnico")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("Error tecnico consultando el tipo de parametro.");
    }

    @Test
    void debeDevolver400ConElMensajeCuandoOcurreUnaExcepcionDeNegocioGenerica() {
        webTestClient.get().uri("/prueba/negocio")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("Mensaje de negocio generico.");
    }

    @Test
    void debeDevolver400ConMsg144CuandoOcurreDecodingExceptionSinCausaRaiz() {
        when(consultarMensajePort.consultarMensaje("MSG-144"))
                .thenReturn("El cuerpo de la petición es inválido.");

        webTestClient.get().uri("/prueba/decodificacion")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("El cuerpo de la petición es inválido.");
    }

    @Test
    void debeDevolver400ConMsg143CuandoLaCausaRaizEsFormatoInvalido() {
        when(consultarMensajePort.consultarMensaje("MSG-143"))
                .thenReturn("El campo '%s' debe ser de tipo %s.");

        webTestClient.get().uri("/prueba/formato-invalido")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("El campo '' debe ser de tipo Integer.");
    }

    @Test
    void debeDevolver400ConElMensajeDeValidacionCuandoLaCausaRaizEsValidationException() {
        webTestClient.get().uri("/prueba/decodificacion-validacion")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.mensajes[0]")
                .isEqualTo("El nombre del tipo de parametro es obligatorio.");
    }

    @RestController
    static class ControladorPrueba {

        @GetMapping("/prueba/validacion")
        public Mono<Void> lanzarValidationException() {
            return Mono.error(ValidationException.build("El nombre del tipo de parametro es obligatorio."));
        }

        @GetMapping("/prueba/no-encontrado")
        public Mono<Void> lanzarNotFoundException() {
            return Mono.error(NotFoundException.build("No existe el tipo de parametro."));
        }

        @GetMapping("/prueba/conflicto")
        public Mono<Void> lanzarConflictException() {
            return Mono.error(ConflictException.build("Ya existe un tipo de parametro con ese nombre."));
        }

        @GetMapping("/prueba/tecnico")
        public Mono<Void> lanzarTechnicalException() {
            return Mono.error(TechnicalException.build("Error tecnico consultando el tipo de parametro."));
        }

        @GetMapping("/prueba/negocio")
        public Mono<Void> lanzarExcepcionDeNegocio() {
            return Mono.error(new BusinessException("Mensaje de negocio generico.") {
            });
        }

        @GetMapping("/prueba/decodificacion")
        public Mono<Void> lanzarDecodingException() {
            return Mono.error(new DecodingException("El cuerpo de la peticion es invalido."));
        }

        @GetMapping("/prueba/formato-invalido")
        public Mono<Void> lanzarFormatoInvalido() {
            return Mono.error(new DecodingException("Error de formato en la peticion",
                    InvalidFormatException.from(null, "valor invalido", "abc", Integer.class)));
        }

        @GetMapping("/prueba/decodificacion-validacion")
        public Mono<Void> lanzarDecodificacionConCausaDeValidacion() {
            return Mono.error(new DecodingException("Error de decodificacion",
                    ValidationException.build("El nombre del tipo de parametro es obligatorio.")));
        }
    }
}