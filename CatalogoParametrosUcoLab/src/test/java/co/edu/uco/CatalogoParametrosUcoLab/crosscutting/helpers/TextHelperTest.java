package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TextHelperTest {

    @Test
    void debeDevolverCadenaVaciaCuandoElValorEsNulo() {
        assertEquals(TextHelper.EMPTY, TextHelper.applyTrim(null));
    }

    @Test
    void debeDevolverCadenaVaciaCuandoElValorSoloTieneEspacios() {
        assertEquals(TextHelper.EMPTY, TextHelper.applyTrim("   "));
    }

    @Test
    void debeRecortarEspaciosAlInicioYAlFinalCuandoElValorTieneEspacios() {
        assertEquals("parametro", TextHelper.applyTrim("  parametro  "));
    }

    @Test
    void debeDevolverElMismoValorCuandoNoTieneEspacios() {
        assertEquals("parametro", TextHelper.applyTrim("parametro"));
    }

    @Test
    void debeDevolverVerdaderoCuandoElValorEsNulo() {
        assertTrue(TextHelper.isBlank(null));
    }

    @Test
    void debeDevolverVerdaderoCuandoElValorSoloTieneEspacios() {
        assertTrue(TextHelper.isBlank("   "));
    }

    @Test
    void debeDevolverFalsoCuandoElValorNoEstaVacio() {
        assertFalse(TextHelper.isBlank("parametro"));
    }

    @Test
    void debeDevolverLaConstanteEmptyComoCadenaVacia() {
        assertSame(TextHelper.EMPTY, TextHelper.applyTrim(null));
        assertEquals("", TextHelper.EMPTY);
    }
}