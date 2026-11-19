package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.ValidarAdministracionAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto.AdministrarCatalogoAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.impl.AdministrarCatalogoAplicacionInteractorImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.ConsultarCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.ActualizarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.EliminarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.ActualizarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.CrearMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.primaryports.interactor.EliminarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.ActualizarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.CrearMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.primaryports.interactor.EliminarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.ActualizarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.primaryports.interactor.EliminarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.EliminarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoAplicacionEntity;
import tools.jackson.databind.ObjectMapper;

class AdministrarCatalogoAplicacionInteractorImplTest {
    private final ConsultarCatalogoAplicacion consultar = mock(ConsultarCatalogoAplicacion.class);
    private final ValidarAdministracionAplicacion validar = mock(ValidarAdministracionAplicacion.class);
    private final CrearModuloInteractor crearModulo = mock(CrearModuloInteractor.class);
    private final ActualizarModuloInteractor actualizarModulo = mock(ActualizarModuloInteractor.class);
    private final EliminarModuloInteractor eliminarModulo = mock(EliminarModuloInteractor.class);
    private final CrearFuncionalidadInteractor crearFuncionalidad = mock(CrearFuncionalidadInteractor.class);
    private final ActualizarFuncionalidadInteractor actualizarFuncionalidad = mock(ActualizarFuncionalidadInteractor.class);
    private final EliminarFuncionalidadInteractor eliminarFuncionalidad = mock(EliminarFuncionalidadInteractor.class);
    private final CrearParametroInteractor crearParametro = mock(CrearParametroInteractor.class);
    private final ActualizarParametroInteractor actualizarParametro = mock(ActualizarParametroInteractor.class);
    private final EliminarParametroInteractor eliminarParametro = mock(EliminarParametroInteractor.class);
    private final CrearMetadatoInteractor crearMetadato = mock(CrearMetadatoInteractor.class);
    private final ActualizarMetadatoInteractor actualizarMetadato = mock(ActualizarMetadatoInteractor.class);
    private final EliminarMetadatoInteractor eliminarMetadato = mock(EliminarMetadatoInteractor.class);
    private final CrearMetadatoAmbienteInteractor crearMetadatoAmbiente = mock(CrearMetadatoAmbienteInteractor.class);
    private final ActualizarMetadatoAmbienteInteractor actualizarMetadatoAmbiente = mock(ActualizarMetadatoAmbienteInteractor.class);
    private final EliminarMetadatoAmbienteInteractor eliminarMetadatoAmbiente = mock(EliminarMetadatoAmbienteInteractor.class);
    private final AdministrarCatalogoAplicacionInteractorImpl interactor = new AdministrarCatalogoAplicacionInteractorImpl(
            consultar, validar, crearModulo, actualizarModulo, eliminarModulo, crearFuncionalidad,
            actualizarFuncionalidad, eliminarFuncionalidad, crearParametro, actualizarParametro,
            eliminarParametro, crearMetadato, actualizarMetadato, eliminarMetadato, crearMetadatoAmbiente,
            actualizarMetadatoAmbiente, eliminarMetadatoAmbiente);

    @Test
    void debeDelegarCadaOperacionAlaInteraccionCorrespondienteCuandoAdministraTodosLosRecursos() {
        var aplicacion = UUID.randomUUID();
        var id = UUID.randomUUID();
        var datos = datosValidos();

        for (var recurso : new String[] { "modulos", "funcionalidades", "parametros", "metadatos", "metadatos-ambiente" }) {
            interactor.crear(aplicacion, recurso, datos);
            interactor.actualizar(aplicacion, recurso, id, datos);
            interactor.eliminar(aplicacion, recurso, id);
        }

        verify(crearModulo).execute(org.mockito.ArgumentMatchers.any());
        verify(actualizarModulo).execute(org.mockito.ArgumentMatchers.eq(id), org.mockito.ArgumentMatchers.any());
        verify(eliminarModulo).execute(id);
        verify(crearFuncionalidad).execute(org.mockito.ArgumentMatchers.any());
        verify(actualizarFuncionalidad).execute(org.mockito.ArgumentMatchers.eq(id), org.mockito.ArgumentMatchers.any());
        verify(eliminarFuncionalidad).execute(id);
        verify(crearParametro).execute(org.mockito.ArgumentMatchers.any());
        verify(actualizarParametro).execute(org.mockito.ArgumentMatchers.eq(id), org.mockito.ArgumentMatchers.any());
        verify(eliminarParametro).execute(id);
        verify(crearMetadato).execute(org.mockito.ArgumentMatchers.any());
        verify(actualizarMetadato).execute(org.mockito.ArgumentMatchers.eq(id), org.mockito.ArgumentMatchers.any());
        verify(eliminarMetadato).execute(id);
        verify(crearMetadatoAmbiente).execute(org.mockito.ArgumentMatchers.any());
        verify(actualizarMetadatoAmbiente).execute(org.mockito.ArgumentMatchers.eq(id), org.mockito.ArgumentMatchers.any());
        verify(eliminarMetadatoAmbiente).execute(id);
    }

    @Test
    void debeConsultarElCatalogoCuandoRecibeLaAplicacion() {
        var aplicacion = UUID.randomUUID();
        var esperado = mock(CatalogoAplicacionEntity.class);
        when(consultar.execute(aplicacion)).thenReturn(esperado);

        assertSame(esperado, interactor.consultar(aplicacion));
    }

    private AdministrarCatalogoAplicacionDtoRequest datosValidos() {
        var modulo = UUID.randomUUID().toString();
        var funcionalidad = UUID.randomUUID().toString();
        var parametro = UUID.randomUUID().toString();
        var tipoParametro = UUID.randomUUID().toString();
        var tipoMetadato = UUID.randomUUID().toString();
        var ambiente = UUID.randomUUID().toString();
        var estado = UUID.randomUUID().toString();
        return new AdministrarCatalogoAplicacionDtoRequest("Nombre", modulo, funcionalidad, tipoParametro, parametro,
                tipoMetadato, ambiente, estado, true, "2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z",
                new ObjectMapper().createObjectNode().put("valor", "1"));
    }
}
