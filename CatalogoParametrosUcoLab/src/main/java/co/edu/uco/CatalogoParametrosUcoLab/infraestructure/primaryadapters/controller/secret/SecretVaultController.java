package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.secret;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.secret.SecretVaultPort;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/** Controller temporal para probar la integración con el vault de secretos. */
@RestController
@RequestMapping("/catalogo-parametros/api/v1/secretos")
public class SecretVaultController {

    private final SecretVaultPort secretVaultPort;

    public SecretVaultController(final SecretVaultPort secretVaultPort) {
        this.secretVaultPort = secretVaultPort;
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> crearSecreto(@RequestParam final String nombre,
            @RequestParam final String valor) {
        return Mono.fromCallable(() -> {
            secretVaultPort.crearSecreto(nombre, valor);
            return ResponseEntity.status(HttpStatus.CREATED).<Void>build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{nombre}")
    public Mono<ResponseEntity<String>> consultarSecreto(@PathVariable final String nombre) {
        return Mono.fromCallable(() -> ResponseEntity.ok(secretVaultPort.consultarSecreto(nombre)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
