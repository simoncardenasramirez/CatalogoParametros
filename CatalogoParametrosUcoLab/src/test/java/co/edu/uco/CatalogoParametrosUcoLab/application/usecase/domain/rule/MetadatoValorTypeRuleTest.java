package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import tools.jackson.databind.node.JsonNodeFactory;

class MetadatoValorTypeRuleTest {

    @Test
    void debeAceptarObjetoYArregloParaTipoJson() {
        assertDoesNotThrow(() -> MetadatoValorTypeRule.execute("Json", JsonNodeFactory.instance.objectNode()));
        assertDoesNotThrow(() -> MetadatoValorTypeRule.execute("Json", JsonNodeFactory.instance.arrayNode()));
    }

    @Test
    void debeRechazarCadenaParaTipoJson() {
        assertThrows(ValidationException.class,
                () -> MetadatoValorTypeRule.execute("Json", JsonNodeFactory.instance.textNode("{}")));
    }

    @Test
    void debeAceptarSoloCadenaParaTipoAlfanumerico() {
        assertDoesNotThrow(() -> MetadatoValorTypeRule.execute("alfanumerico",
                JsonNodeFactory.instance.textNode("COP")));
        assertThrows(ValidationException.class,
                () -> MetadatoValorTypeRule.execute("alfanumerico", JsonNodeFactory.instance.objectNode()));
    }

    @Test
    void debeValidarFormatoDelTipoDate() {
        assertDoesNotThrow(() -> MetadatoValorTypeRule.execute("date",
                JsonNodeFactory.instance.textNode("2026-08-26")));
        assertThrows(ValidationException.class,
                () -> MetadatoValorTypeRule.execute("date", JsonNodeFactory.instance.textNode("26/08/2026")));
    }
}
