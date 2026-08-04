package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.codec.DecodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tools.jackson.databind.exc.InvalidFormatException;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<Response> manejarErrorDeFormato(final DecodingException exception) {
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

        response.getMensajes().add("El cuerpo de la petición es inválido.");
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
