package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroCatalogoRecord;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ParametroTipoParametroIsValidRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @Mock
    private TipoParametroRepository tipoParametroRepository;

    @InjectMocks
    private ParametroTipoParametroIsValidRuleImpl rule;

    private CrearParametroDomain domainConIdTipoParametro(final UUID idTipoParametro) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), idTipoParametro, true);
    }

    @Test
    void debePasarCuandoElIdTipoParametroEsReal() {
        var id = TipoParametroCatalogoRecord.ID_TECNICO;
        when(tipoParametroRepository.findById(id)).thenReturn(Optional.of(TipoParametroEntity.create(id, "Tecnico")));
        assertDoesNotThrow(() -> rule.execute(domainConIdTipoParametro(id)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElDomainEsNulo() {
        when(consultarMensajePort.consultarMensaje("MSG-137"))
                .thenReturn("El tipo de parametro asociado es obligatorio.");

        assertThrows(ValidationException.class, () -> rule.execute(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdTipoParametroEsElPorDefecto() {
        when(consultarMensajePort.consultarMensaje("MSG-137"))
                .thenReturn("El tipo de parametro asociado es obligatorio.");

        assertThrows(ValidationException.class,
                () -> rule.execute(domainConIdTipoParametro(UUIDHelper.getDefault())));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoElIdTipoParametroNoPerteneceAlCatalogo() {
        var id = UUID.randomUUID();
        when(tipoParametroRepository.findById(id)).thenReturn(Optional.empty());
        when(consultarMensajePort.consultarMensaje("MSG-145"))
                .thenReturn("El tipo de parametro con el id especificado no existe en el catalogo.");

        assertThrows(co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException.class,
                () -> rule.execute(domainConIdTipoParametro(id)));
    }
}
