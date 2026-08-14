package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler;

import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final TelemetryService telemetryService;

    public GlobalExceptionHandler(final TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<Response> manejarErrorDeFormato(final DecodingException exception) {
        logger.error("[EXCEPTION-HANDLER] Error de formato en la peticion", exception);
        telemetryService.recordError("decoding-exception", "Error de formato en la peticion");
        var response = new Response();
        Throwable causa = obtenerCausaRaiz(exception);

        if (causa instanceof InvalidFormatException invalidFormat) {
            String campo = invalidFormat.getPath().stream()
                    .map(referencia -> referencia.getPropertyName())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));

            String tipoEsperado = invalidFormat.getTargetType().getSimpleName();

            response.getMensajes().add(
                    "El campo '" + campo + "' debe ser de tipo " + tipoEsperado + ".");

            return ResponseEntity.badRequest().body(response);
        }

        if (causa instanceof ValidationException validationException) {
            response.getMensajes().add(validationException.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        if (causa instanceof NotFoundException notFoundException) {
            response.getMensajes().add(notFoundException.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (causa instanceof ConflictException conflictException) {
            response.getMensajes().add(conflictException.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (causa instanceof TechnicalException technicalException) {
            response.getMensajes().add(technicalException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        if (causa instanceof BusinessException businessException) {
            response.getMensajes().add(businessException.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        response.getMensajes().add("El cuerpo de la peticion es invalido.");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Response> manejarValidacion(final ValidationException exception) {
        logger.warn("[EXCEPTION-HANDLER] Error de validacion: {}", exception.getMessage());
        telemetryService.recordError("validation-exception", exception.getMessage());
        var response = new Response();
        response.getMensajes().add(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> manejarNoEncontrado(final NotFoundException exception) {
        logger.warn("[EXCEPTION-HANDLER] Recurso no encontrado: {}", exception.getMessage());
        telemetryService.recordError("not-found-exception", exception.getMessage());
        var response = new Response();
        response.getMensajes().add(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Response> manejarConflicto(final ConflictException exception) {
        logger.warn("[EXCEPTION-HANDLER] Conflicto: {}", exception.getMessage());
        telemetryService.recordError("conflict-exception", exception.getMessage());
        var response = new Response();
        response.getMensajes().add(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<Response> manejarTecnico(final TechnicalException exception) {
        logger.error("[EXCEPTION-HANDLER] Error tecnico: {}", exception.getMessage(), exception);
        telemetryService.recordError("technical-exception", exception.getMessage());
        var response = new Response();
        response.getMensajes().add(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private Throwable obtenerCausaRaiz(final Throwable exception) {
        Throwable causa = exception;

        while (causa.getCause() != null) {
            causa = causa.getCause();
        }

        return causa;
    }
}
