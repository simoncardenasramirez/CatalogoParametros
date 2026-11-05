package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.messageucolab.client.api.MessageReader;
import co.edu.uco.messageucolab.client.api.model.MessageDetails;
import reactor.core.publisher.Mono;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ConsultarMensajeAdapterTest {

    private final ConsultarMensajeAdapter adapter = new ConsultarMensajeAdapter();

    @Test
    void debeDevolverElTextoRealCuandoExisteLaLlave() {
        assertEquals("El nombre de la organizacion es obligatorio.",
                adapter.consultarMensaje("MSG-100"));
    }

    @Test
    void debeDevolverElMensajeConPlaceholdersCuandoLaLlaveContieneFormato() {
        assertEquals("El campo '%s' debe ser de tipo %s.",
                adapter.consultarMensaje("MSG-143"));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaLlaveNoExiste() {
        assertThrows(NotFoundException.class, () -> adapter.consultarMensaje("MSG-999"));
    }

    @Test
    void debeConsultarMessageUcoLabUsandoElCodigoNormalizado() {
        final MessageReader reader = org.mockito.Mockito.mock(MessageReader.class);
        final var detalle = new MessageDetails("MSG_100", "es", "es", "Titulo remoto",
                "Texto administrado en MessageUcoLab", null, null, null, null, null);
        when(reader.getDetails("MSG_100")).thenReturn(Mono.just(detalle));

        final var integrado = new ConsultarMensajeAdapter(reader);

        assertEquals("Texto administrado en MessageUcoLab", integrado.consultarMensaje("MSG-100"));
        verify(reader).getDetails("MSG_100");
    }
}
