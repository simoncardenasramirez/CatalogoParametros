package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class RegistrarIdentidadServicioDtoTest {

    @Test
    void debeCrearYNormalizarLaSolicitud() {
        final var request = RegistrarIdentidadServicioDtoRequest.create("  ambiente  ", "  client-id  ");

        assertEquals("ambiente", request.getIdAmbiente());
        assertEquals("client-id", request.getClientId());
    }

    @Test
    void debePermitirActualizarLaSolicitud() {
        final var request = new RegistrarIdentidadServicioDtoRequest();

        request.setIdAmbiente("ambiente-2");
        request.setClientId("client-2");

        assertEquals("ambiente-2", request.getIdAmbiente());
        assertEquals("client-2", request.getClientId());
    }

    @Test
    void debeCrearYNormalizarLaEntradaInterna() {
        final UUID ambiente = UUID.randomUUID();
        final var input = RegistrarIdentidadServicioDtoInput.create(ambiente, "  client-id  ");

        assertEquals(ambiente, input.getIdAmbiente());
        assertEquals("client-id", input.getClientId());
    }
}
