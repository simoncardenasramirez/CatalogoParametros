package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.cargamasiva;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.primaryports.interactor.CargaMasivaParametrosInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.cargamasiva.CargaMasivaResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/catalogo-parametros/api/v1/cargas-masivas/parametros")
public final class CargaMasivaParametrosController {
    private static final MediaType XLSX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    private final CargaMasivaParametrosInteractor interactor;

    public CargaMasivaParametrosController(final CargaMasivaParametrosInteractor interactor) {
        this.interactor = interactor;
    }

    @GetMapping("/plantilla")
    public Mono<ResponseEntity<byte[]>> plantilla() {
        return Mono.fromCallable(() -> ResponseEntity.ok().contentType(XLSX)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("plantilla-parametros.xlsx").build().toString())
                .body(interactor.plantilla())).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(value = "/validar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<CargaMasivaResponse>> validar(@RequestPart("archivo") final FilePart file) {
        return bytes(file).flatMap(archivo -> Mono.fromCallable(() -> interactor.validar(archivo)))
                .map(CargaMasivaResponse::from)
                .map(ResponseEntity::ok).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<CargaMasivaResponse>> cargar(@RequestPart("archivo") final FilePart file) {
        return bytes(file).flatMap(archivo -> Mono.fromCallable(() -> interactor.cargar(archivo)))
                .map(CargaMasivaResponse::from)
                .map(ResponseEntity::ok).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(value = "/reporte-errores", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<byte[]>> reporteErrores(@RequestPart("archivo") final FilePart file) {
        return bytes(file).flatMap(archivo -> Mono.fromCallable(() -> interactor.reporteErrores(archivo)))
                .map(bytes -> ResponseEntity.ok().contentType(XLSX)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("reporte-errores-parametros.xlsx").build().toString())
                .body(bytes)).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<byte[]> bytes(final FilePart file) {
        return DataBufferUtils.join(file.content()).map(buffer -> {
            try {
                var bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                return bytes;
            } finally {
                DataBufferUtils.release(buffer);
            }
        });
    }
}
