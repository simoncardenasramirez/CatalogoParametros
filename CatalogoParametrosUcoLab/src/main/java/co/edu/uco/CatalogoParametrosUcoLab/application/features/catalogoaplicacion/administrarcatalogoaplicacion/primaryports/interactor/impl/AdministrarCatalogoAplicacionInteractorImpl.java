package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.impl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.ValidarAdministracionAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.ConsultarCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.AdministrarCatalogoAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.mapper.AdministrarCatalogoAplicacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto.AdministrarCatalogoAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoAplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.ActualizarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.primaryports.interactor.EliminarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.ActualizarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.EliminarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.EliminarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.CrearMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.ActualizarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.primaryports.interactor.EliminarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.CrearMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.ActualizarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.primaryports.interactor.EliminarMetadatoAmbienteInteractor;

@Service
public final class AdministrarCatalogoAplicacionInteractorImpl implements AdministrarCatalogoAplicacionInteractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrarCatalogoAplicacionInteractorImpl.class);
    private final ConsultarCatalogoAplicacion consultar;
    private final ValidarAdministracionAplicacion validar;
    private final CrearModuloInteractor crearModulo;
    private final ActualizarModuloInteractor actualizarModulo;
    private final EliminarModuloInteractor eliminarModulo;
    private final CrearFuncionalidadInteractor crearFuncionalidad;
    private final ActualizarFuncionalidadInteractor actualizarFuncionalidad;
    private final EliminarFuncionalidadInteractor eliminarFuncionalidad;
    private final CrearParametroInteractor crearParametro;
    private final ActualizarParametroInteractor actualizarParametro;
    private final EliminarParametroInteractor eliminarParametro;
    private final CrearMetadatoInteractor crearMetadato;
    private final ActualizarMetadatoInteractor actualizarMetadato;
    private final EliminarMetadatoInteractor eliminarMetadato;
    private final CrearMetadatoAmbienteInteractor crearMetadatoAmbiente;
    private final ActualizarMetadatoAmbienteInteractor actualizarMetadatoAmbiente;
    private final EliminarMetadatoAmbienteInteractor eliminarMetadatoAmbiente;
    public AdministrarCatalogoAplicacionInteractorImpl(final ConsultarCatalogoAplicacion consultar,
            final ValidarAdministracionAplicacion validar,
            final CrearModuloInteractor crearModulo,
            final ActualizarModuloInteractor actualizarModulo,
            final EliminarModuloInteractor eliminarModulo,
            final CrearFuncionalidadInteractor crearFuncionalidad,
            final ActualizarFuncionalidadInteractor actualizarFuncionalidad,
            final EliminarFuncionalidadInteractor eliminarFuncionalidad,
            final CrearParametroInteractor crearParametro,
            final ActualizarParametroInteractor actualizarParametro,
            final EliminarParametroInteractor eliminarParametro,
            final CrearMetadatoInteractor crearMetadato,
            final ActualizarMetadatoInteractor actualizarMetadato,
            final EliminarMetadatoInteractor eliminarMetadato,
            final CrearMetadatoAmbienteInteractor crearMetadatoAmbiente,
            final ActualizarMetadatoAmbienteInteractor actualizarMetadatoAmbiente,
            final EliminarMetadatoAmbienteInteractor eliminarMetadatoAmbiente) {
        this.consultar = consultar; this.validar = validar;
        this.crearModulo = crearModulo;
        this.actualizarModulo = actualizarModulo;
        this.eliminarModulo = eliminarModulo;
        this.crearFuncionalidad = crearFuncionalidad;
        this.actualizarFuncionalidad = actualizarFuncionalidad;
        this.eliminarFuncionalidad = eliminarFuncionalidad;
        this.crearParametro = crearParametro;
        this.actualizarParametro = actualizarParametro;
        this.eliminarParametro = eliminarParametro;
        this.crearMetadato = crearMetadato;
        this.actualizarMetadato = actualizarMetadato;
        this.eliminarMetadato = eliminarMetadato;
        this.crearMetadatoAmbiente = crearMetadatoAmbiente;
        this.actualizarMetadatoAmbiente = actualizarMetadatoAmbiente;
        this.eliminarMetadatoAmbiente = eliminarMetadatoAmbiente;
    }
    @Override public CatalogoAplicacionEntity consultar(final UUID aplicacion) { return consultar.execute(aplicacion); }
    @Override public void crear(final UUID aplicacion, final String recurso, final AdministrarCatalogoAplicacionDtoRequest datos) {
        var dominio = AdministrarCatalogoAplicacionDtoMapper.INSTANCE.toDomain(aplicacion, recurso, null, datos,
                AdministrarCatalogoAplicacionDomain.Operacion.CREAR);
        validar.execute(dominio);
        LOGGER.info("[ADMINISTRAR-CATALOGO-APLICACION] Crear {} en {}", recurso, aplicacion);
        switch (dominio.getRecurso()) {
            case MODULO -> crearModulo.execute(AdministrarCatalogoAplicacionDtoMapper.INSTANCE.crearModulo(aplicacion, datos));
            case FUNCIONALIDAD -> crearFuncionalidad.execute(AdministrarCatalogoAplicacionDtoMapper.INSTANCE.crearFuncionalidad(aplicacion, datos));
            case PARAMETRO -> crearParametro.execute(AdministrarCatalogoAplicacionDtoMapper.INSTANCE.crearParametro(aplicacion, datos));
            case METADATO -> crearMetadato.execute(AdministrarCatalogoAplicacionDtoMapper.INSTANCE.crearMetadato(aplicacion, datos));
            case METADATO_AMBIENTE -> crearMetadatoAmbiente.execute(AdministrarCatalogoAplicacionDtoMapper.INSTANCE.crearMetadatoAmbiente(aplicacion, datos));
        }
    }
    @Override public void actualizar(final UUID aplicacion, final String recurso, final UUID id,
            final AdministrarCatalogoAplicacionDtoRequest datos) {
        var dominio = AdministrarCatalogoAplicacionDtoMapper.INSTANCE.toDomain(aplicacion, recurso, id, datos,
                AdministrarCatalogoAplicacionDomain.Operacion.ACTUALIZAR);
        validar.execute(dominio);
        LOGGER.info("[ADMINISTRAR-CATALOGO-APLICACION] Actualizar {} {} en {}", recurso, id, aplicacion);
        switch (dominio.getRecurso()) {
            case MODULO -> actualizarModulo.execute(id, AdministrarCatalogoAplicacionDtoMapper.INSTANCE.actualizarModulo(aplicacion, datos));
            case FUNCIONALIDAD -> actualizarFuncionalidad.execute(id, AdministrarCatalogoAplicacionDtoMapper.INSTANCE.actualizarFuncionalidad(aplicacion, datos));
            case PARAMETRO -> actualizarParametro.execute(id, AdministrarCatalogoAplicacionDtoMapper.INSTANCE.actualizarParametro(aplicacion, datos));
            case METADATO -> actualizarMetadato.execute(id, AdministrarCatalogoAplicacionDtoMapper.INSTANCE.actualizarMetadato(aplicacion, datos));
            case METADATO_AMBIENTE -> actualizarMetadatoAmbiente.execute(id, AdministrarCatalogoAplicacionDtoMapper.INSTANCE.actualizarMetadatoAmbiente(aplicacion, datos));
        }
    }
    @Override public void eliminar(final UUID aplicacion, final String recurso, final UUID id) {
        var dominio = AdministrarCatalogoAplicacionDtoMapper.INSTANCE.toDomain(aplicacion, recurso, id, null,
                AdministrarCatalogoAplicacionDomain.Operacion.ELIMINAR);
        validar.execute(dominio);
        LOGGER.info("[ADMINISTRAR-CATALOGO-APLICACION] Eliminar {} {} en {}", recurso, id, aplicacion);
        switch (dominio.getRecurso()) {
            case MODULO -> eliminarModulo.execute(id);
            case FUNCIONALIDAD -> eliminarFuncionalidad.execute(id);
            case PARAMETRO -> eliminarParametro.execute(id);
            case METADATO -> eliminarMetadato.execute(id);
            case METADATO_AMBIENTE -> eliminarMetadatoAmbiente.execute(id);
        }
    }
}
