package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.token;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.dto.ActualizarEstadoTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.interactor.ActualizarEstadoTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultartoken.primaryports.interactor.ConsultarTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.primaryports.interactor.ConsultarParametrosPorTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.dto.CrearTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.interactor.CrearTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.dto.ValidarTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.interactor.ValidarTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token.TokenResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token.ValidarTokenResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token.ParametrosTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/tokens")
@Tag(name = "Tokens", description = "Creacion, consulta, cambio de estado y validacion de tokens.")
public final class TokenController {
    private final CrearTokenInteractor crear;
    private final ConsultarTokenInteractor consultar;
    private final ActualizarEstadoTokenInteractor actualizarEstado;
    private final ValidarTokenInteractor validar;
    private final ConsultarParametrosPorTokenInteractor consultarParametros;

    public TokenController(final CrearTokenInteractor crear, final ConsultarTokenInteractor consultar,
            final ActualizarEstadoTokenInteractor actualizarEstado, final ValidarTokenInteractor validar,
            final ConsultarParametrosPorTokenInteractor consultarParametros) {
        this.crear = crear;
        this.consultar = consultar;
        this.actualizarEstado = actualizarEstado;
        this.validar = validar;
        this.consultarParametros = consultarParametros;
    }

    @PostMapping
    @Operation(summary = "Crear token", description = "Cifra el token con BCrypt y almacena solamente su hash con salt.")
    public Mono<ResponseEntity<TokenResponse>> crearToken(@RequestBody final CrearTokenDtoRequest request) {
        return Mono.fromCallable(() -> {
            var response = new TokenResponse();
            response.add(crear.execute(request));
            response.getMensajes().add("Token creado exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping
    @Operation(summary = "Consultar tokens")
    public Mono<ResponseEntity<TokenResponse>> consultarTokens() {
        return Mono.fromCallable(() -> {
            var response = new TokenResponse();
            response.addAll(consultar.execute());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar token por id")
    public Mono<ResponseEntity<TokenResponse>> consultarToken(@PathVariable final UUID id) {
        return Mono.fromCallable(() -> {
            var response = new TokenResponse();
            var tokens = consultar.execute(id);
            response.addAll(tokens);
            if (tokens.isEmpty()) {
                response.getMensajes().add("No se encontro el token.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del token")
    public Mono<ResponseEntity<TokenResponse>> actualizarEstadoToken(@PathVariable final UUID id,
            @RequestBody final ActualizarEstadoTokenDtoRequest request) {
        return Mono.fromCallable(() -> {
            var response = new TokenResponse();
            response.add(actualizarEstado.execute(id, request));
            response.getMensajes().add("Estado del token actualizado exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("/{id}/validar")
    @Operation(summary = "Validar token", description = "Valida el secreto con BCrypt, el estado activo y la expiracion.")
    public Mono<ResponseEntity<ValidarTokenResponse>> validarToken(@PathVariable final UUID id,
            @RequestBody final ValidarTokenDtoRequest request) {
        return Mono.fromCallable(() -> {
            var valido = validar.execute(id, request);
            var response = new ValidarTokenResponse(valido);
            if (!valido) response.getMensajes().add("El token no es valido.");
            return new ResponseEntity<>(response, valido ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}/parametros")
    @Operation(summary = "Consultar parametros autorizados por el token",
            description = "Valida el Bearer token y filtra por la aplicacion y el ambiente asociados.")
    public Mono<ResponseEntity<ParametrosTokenResponse>> consultarParametrosPorToken(@PathVariable final UUID id,
            @RequestHeader(name = "Authorization", required = false) final String authorization) {
        return Mono.fromCallable(() -> {
            var resultado = consultarParametros.execute(id, authorization);
            var response = new ParametrosTokenResponse(resultado.aplicacion(), resultado.ambiente(),
                    resultado.parametros());
            if (!resultado.autorizado()) {
                response.getMensajes().add("El token no es valido, esta inactivo, revocado o expirado.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
