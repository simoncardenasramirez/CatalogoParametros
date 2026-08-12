package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearParametroDomainTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String NOMBRE = "ParametroTest";
    private static final UUID ID_FUNCIONALIDAD = UUID.randomUUID();
    private static final UUID ID_TIPO_PARAMETRO = UUID.randomUUID();
    private static final boolean ACTIVO = true;

    // ==================== TESTS DE CREACION ====================

    @Test
    void shouldCreateDomainWithValidData() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertEquals(ID, domain.getId());
        assertEquals(NOMBRE, domain.getNombre());
        assertEquals(ID_FUNCIONALIDAD, domain.getIdFuncionalidad());
        assertEquals(ID_TIPO_PARAMETRO, domain.getIdTipoParametro());
        assertTrue(domain.isActivo());
    }

    @Test
    void shouldCreateDomainWithDefaultId() {
        var domain = CrearParametroDomain.create(null, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertNotNull(domain.getId());
    }

    @Test
    void shouldCreateDomainWithInactiveStatus() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, false);

        assertFalse(domain.isActivo());
    }

    // ==================== TESTS DE GENERATE ID ====================

    @Test
    void shouldGenerateNewId() {
        var domain = CrearParametroDomain.create(null, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);
        var initialId = domain.getId();

        domain.generateId();

        assertNotNull(domain.getId());
        assertNotEquals(initialId, domain.getId());
    }

    @Test
    void shouldGenerateUniqueIds() {
        var domain1 = CrearParametroDomain.create(null, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);
        var domain2 = CrearParametroDomain.create(null, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        domain1.generateId();
        domain2.generateId();

        assertNotEquals(domain1.getId(), domain2.getId());
    }

    // ==================== TESTS DE SETTERS Y GETTERS ====================

    @Test
    void shouldGetNombre() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertEquals(NOMBRE, domain.getNombre());
    }

    @Test
    void shouldGetIdFuncionalidad() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertEquals(ID_FUNCIONALIDAD, domain.getIdFuncionalidad());
    }

    @Test
    void shouldGetIdTipoParametro() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertEquals(ID_TIPO_PARAMETRO, domain.getIdTipoParametro());
    }

    @Test
    void shouldGetActivo() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertTrue(domain.isActivo());
    }

    // ==================== TESTS DE VALORES POR DEFECTO ====================

    @Test
    void shouldHandleNullNombre() {
        var domain = CrearParametroDomain.create(ID, null, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        // TextHelper.applyTrim(null) devuelve cadena vacia, no null
        assertEquals("", domain.getNombre());
    }

    @Test
    void shouldHandleNullIdFuncionalidad() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, null, ID_TIPO_PARAMETRO, ACTIVO);

        assertNotNull(domain.getIdFuncionalidad());
    }

    @Test
    void shouldHandleNullIdTipoParametro() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, null, ACTIVO);

        assertNotNull(domain.getIdTipoParametro());
    }

    // ==================== TESTS DE HERENCIA ====================

    @Test
    void shouldExtendDomainClass() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertInstanceOf(Domain.class, domain);
    }

    @Test
    void shouldHaveIdFromDomain() {
        var domain = CrearParametroDomain.create(ID, NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);

        assertEquals(ID, domain.getId());
    }
}
