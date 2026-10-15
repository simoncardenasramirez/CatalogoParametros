package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.usecase.impl.ConsultarParametrosPorTokenImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.ValidarToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;

@ExtendWith(MockitoExtension.class)
class ConsultarParametrosPorTokenImplTest {
    @Mock private TokenRepository tokens;
    @Mock private ParametroRepository parametros;
    @Mock private ValidarToken validarToken;
    @Mock private AplicacionRepository aplicaciones;
    @Mock private AmbienteRepository ambientes;
    @Mock private FuncionalidadRepository funcionalidades;
    @Mock private ModuloRepository modulos;
    @Mock private TipoParametroRepository tiposParametro;
    @Mock private MetadatoRepository metadatos;
    @Mock private TipoMetadatoRepository tiposMetadato;
    @Mock private MetadatoAmbienteRepository metadatosAmbiente;
    @Mock private EstadoMetadatoAmbienteRepository estadosMetadatoAmbiente;
    private ConsultarParametrosPorTokenImpl useCase;
    private UUID tokenId;

    @BeforeEach
    void preparar() {
        useCase = new ConsultarParametrosPorTokenImpl(tokens, parametros, validarToken, aplicaciones, ambientes,
                funcionalidades, modulos, tiposParametro, metadatos, tiposMetadato, metadatosAmbiente,
                estadosMetadatoAmbiente);
        tokenId = UUID.randomUUID();
    }

    @Test
    void debeFiltrarConAplicacionYAmbienteDelTokenValido() {
        var aplicacionId = UUID.randomUUID();
        var ambienteId = UUID.randomUUID();
        var token = TokenEntity.create(tokenId, "hash", UUID.randomUUID(), aplicacionId, ambienteId,
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        var parametro = ParametroEntity.create(UUID.randomUUID(), "SMMLV", UUID.randomUUID(), UUID.randomUUID(), true);
        when(validarToken.execute(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token));
        when(parametros.findByAplicacionAndAmbiente(aplicacionId, ambienteId)).thenReturn(List.of(parametro));
        when(metadatos.findByIdParametro(parametro.getId())).thenReturn(List.of());
        when(metadatosAmbiente.findByIdParametroAndIdAmbiente(parametro.getId(), ambienteId))
                .thenReturn(List.of());

        var resultado = useCase.execute(tokenId, "Juanjo");

        assertTrue(resultado.autorizado());
        assertEquals(1, resultado.parametros().size());
        assertEquals(parametro, resultado.parametros().get(0).parametro());
        verify(parametros).findByAplicacionAndAmbiente(aplicacionId, ambienteId);
    }

    @Test
    void noDebeConsultarParametrosCuandoElTokenNoEsValido() {
        when(validarToken.execute(org.mockito.ArgumentMatchers.any())).thenReturn(false);

        var resultado = useCase.execute(tokenId, "incorrecto");

        assertFalse(resultado.autorizado());
        assertTrue(resultado.parametros().isEmpty());
        verifyNoInteractions(tokens, parametros);
    }
}
