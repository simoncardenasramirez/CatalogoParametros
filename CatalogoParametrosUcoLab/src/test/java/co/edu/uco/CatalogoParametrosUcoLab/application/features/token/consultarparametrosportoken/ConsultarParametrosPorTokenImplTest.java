package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
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

    @Test
    void debeAislarValoresPorAmbienteYExcluirHistoricosSinAmbienteCuandoConsultaToken() {
        var app = UUID.randomUUID(); var dev = UUID.randomUUID(); var prod = UUID.randomUUID();
        var token = TokenEntity.create(tokenId, "hash", UUID.randomUUID(), app, dev,
                OffsetDateTime.now(java.time.ZoneOffset.UTC), OffsetDateTime.now(java.time.ZoneOffset.UTC).plusDays(1));
        var parametro = ParametroEntity.create(UUID.randomUUID(), "Endpoint", UUID.randomUUID(), UUID.randomUUID(), true);
        var tipo = UUID.randomUUID();
        var fabrica = tools.jackson.databind.node.JsonNodeFactory.instance;
        var desarrollo = co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity.create(UUID.randomUUID(), parametro.getId(), tipo, dev, fabrica.stringNode("solo-dev"));
        var produccion = co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity.create(UUID.randomUUID(), parametro.getId(), tipo, prod, fabrica.stringNode("solo-prod"));
        var historico = co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity.create(UUID.randomUUID(), parametro.getId(), tipo, fabrica.stringNode("sin-ambiente"));
        when(validarToken.execute(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token));
        when(parametros.findByAplicacionAndAmbiente(app, dev)).thenReturn(List.of(parametro));
        when(metadatos.findByIdParametro(parametro.getId())).thenReturn(List.of(produccion, historico, desarrollo));
        var resultado = useCase.execute(tokenId, "secreto");
        assertEquals(List.of(desarrollo), resultado.parametros().get(0).metadatos().stream().map(detalle -> detalle.metadato()).toList());
    }

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
                OffsetDateTime.now(java.time.ZoneOffset.UTC), OffsetDateTime.now(java.time.ZoneOffset.UTC).plusDays(1));
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
    @Test
    void debeExcluirInactivosCuandoConsultaParametrosConTokenValido() {
        var aplicacionId = UUID.randomUUID();
        var ambienteId = UUID.randomUUID();
        var token = TokenEntity.create(tokenId, "hash", UUID.randomUUID(), aplicacionId, ambienteId,
                OffsetDateTime.now(java.time.ZoneOffset.UTC), OffsetDateTime.now(java.time.ZoneOffset.UTC).plusDays(1));
        var activo = ParametroEntity.create(UUID.randomUUID(), "Activo", UUID.randomUUID(), UUID.randomUUID(), true);
        var inactivo = ParametroEntity.create(UUID.randomUUID(), "Inactivo", UUID.randomUUID(), UUID.randomUUID(), false);
        when(validarToken.execute(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token));
        when(parametros.findByAplicacionAndAmbiente(aplicacionId, ambienteId)).thenReturn(List.of(inactivo, activo));

        var resultado = useCase.execute(tokenId, "secreto");

        assertTrue(resultado.autorizado());
        assertEquals(List.of(activo), resultado.parametros().stream().map(detalle -> detalle.parametro()).toList());
        verify(metadatos, org.mockito.Mockito.never()).findByIdParametro(inactivo.getId());
        verify(metadatosAmbiente, org.mockito.Mockito.never())
                .findByIdParametroAndIdAmbiente(inactivo.getId(), ambienteId);
    }
    @Test
    void debeRetornarListaVaciaCuandoTodosLosParametrosEstanInactivos() {
        var app = UUID.randomUUID(); var ambiente = UUID.randomUUID();
        var token = TokenEntity.create(tokenId, "hash", UUID.randomUUID(), app, ambiente,
                OffsetDateTime.now(java.time.ZoneOffset.UTC), OffsetDateTime.now(java.time.ZoneOffset.UTC).plusDays(1));
        when(validarToken.execute(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token));
        when(parametros.findByAplicacionAndAmbiente(app, ambiente)).thenReturn(List.of(
                ParametroEntity.create(UUID.randomUUID(), "Inactivo", UUID.randomUUID(), UUID.randomUUID(), false)));
        var resultado = useCase.execute(tokenId, "secreto");
        assertTrue(resultado.autorizado());
        assertTrue(resultado.parametros().isEmpty());
        verifyNoInteractions(funcionalidades, modulos, tiposParametro, metadatos, metadatosAmbiente);
    }
}
