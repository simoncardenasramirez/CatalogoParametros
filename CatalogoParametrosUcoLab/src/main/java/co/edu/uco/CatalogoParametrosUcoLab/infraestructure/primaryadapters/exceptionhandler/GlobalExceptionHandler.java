package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ConsultarMensajePort consultarMensajePort;

    public GlobalExceptionHandler(final ConsultarMensajePort consultarMensajePort) {
        this.consultarMensajePort = consultarMensajePort;
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<Response> manejarErrorDeFormato(
            final DecodingException exception) {

        Throwable causa = obtenerCausaRaiz(exception);

        if (causa instanceof InvalidFormatException invalidFormatException) {
            return manejarFormatoInvalido(invalidFormatException);
        }

        return manejarErrorDecodificacion(exception);
    }

    private ResponseEntity<Response> manejarFormatoInvalido(
            final InvalidFormatException exception) {

        var response = new Response();

        String campo = exception.getPath().stream()
                .map(referencia -> referencia.getPropertyName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));

        String tipoEsperado = exception.getTargetType().getSimpleName();

        response.getMensajes().add(
                consultarMensajePort.consultarMensaje("MSG-143")
                        .formatted(campo, tipoEsperado));

        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<Response> manejarErrorDecodificacion(
            final DecodingException exception) {

        var response = new Response();

        response.getMensajes().add(
                consultarMensajePort.consultarMensaje("MSG-144"));

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Response> manejarValidacion(
            final ValidationException exception) {

        var response = new Response();

        response.getMensajes().add(exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> manejarNoEncontrado(
            final NotFoundException exception) {

        var response = new Response();

        response.getMensajes().add(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Response> manejarConflicto(
            final ConflictException exception) {

        var response = new Response();

        response.getMensajes().add(exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<Response> manejarTecnico(
            final TechnicalException exception) {

        var response = new Response();

        response.getMensajes().add(exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response> manejarNegocio(
            final BusinessException exception) {

        var response = new Response();

        response.getMensajes().add(exception.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    private Throwable obtenerCausaRaiz(final Throwable exception) {

        Throwable causa = exception;

        while (causa.getCause() != null) {
            causa = causa.getCause();
        }

        return causa;
    }
}