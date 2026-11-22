package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoAplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.ambiente.AmbienteResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.aplicacion.AplicacionResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.catalogoaplicacion.CatalogoAplicacionResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.estadometadatoambiente.EstadoMetadatoAmbienteResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.funcionalidad.FuncionalidadResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.identidadservicio.IdentidadServicioResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.metadato.MetadatoResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.metadatoambiente.MetadatoAmbienteResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.modulo.ModuloResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.organizacion.OrganizacionResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametrosConsumidorResponse;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro.ParametroResponse;

class ResponsesBasicasTest {

    @Test
    void debeCrearListasVaciasEnLasRespuestasDeCatalogo() {
        assertEmpty(new AmbienteResponse().getAmbientes());
        assertEmpty(new AplicacionResponse().getAplicaciones());
        assertEmpty(new EstadoMetadatoAmbienteResponse().getEstadosMetadatoAmbiente());
        assertEmpty(new FuncionalidadResponse().getFuncionalidades());
        assertEmpty(new IdentidadServicioResponse().getIdentidadesServicio());
        assertEmpty(new MetadatoResponse().getMetadatos());
        assertEmpty(new MetadatoAmbienteResponse().getMetadatosAmbiente());
        assertEmpty(new ModuloResponse().getModulos());
        assertEmpty(new OrganizacionResponse().getOrganizaciones());
        assertEmpty(new ParametroResponse().getParametros());
    }

    @Test
    void debeConservarElCatalogoEnSuRespuesta() {
        final CatalogoAplicacionEntity catalogo = null;
        final var response = new CatalogoAplicacionResponse(catalogo);

        assertSame(catalogo, response.getCatalogo());
        assertNotNull(response.getMensajes());
    }

    @Test
    void debeConservarLaAplicacionElAmbienteYLosParametrosDelConsumidor() {
        final var response = new ParametrosConsumidorResponse(null, null, java.util.List.of());

        assertTrue(response.getParametros().isEmpty());
        assertTrue(response.getMensajes().isEmpty());
    }

    private static void assertEmpty(final java.util.List<?> values) {
        assertNotNull(values);
        assertTrue(values.isEmpty());
    }
}
