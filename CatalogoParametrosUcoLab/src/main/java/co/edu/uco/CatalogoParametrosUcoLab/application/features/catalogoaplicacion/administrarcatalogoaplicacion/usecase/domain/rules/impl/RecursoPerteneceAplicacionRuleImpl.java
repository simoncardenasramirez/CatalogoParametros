package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.rules.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.rules.RecursoPerteneceAplicacionRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class RecursoPerteneceAplicacionRuleImpl implements RecursoPerteneceAplicacionRule {
    private final BaseDatosAplicacionRepository aplicaciones;
    private final BaseDatosModuloRepository modulos;
    private final BaseDatosFuncionalidadRepository funcionalidades;
    private final BaseDatosParametroRepository parametros;
    private final BaseDatosMetadatoRepository metadatos;
    private final BaseDatosMetadatoAmbienteRepository asociaciones;
    public RecursoPerteneceAplicacionRuleImpl(final BaseDatosAplicacionRepository aplicaciones,
            final BaseDatosModuloRepository modulos, final BaseDatosFuncionalidadRepository funcionalidades,
            final BaseDatosParametroRepository parametros, final BaseDatosMetadatoRepository metadatos,
            final BaseDatosMetadatoAmbienteRepository asociaciones) {
        this.aplicaciones = aplicaciones; this.modulos = modulos; this.funcionalidades = funcionalidades;
        this.parametros = parametros; this.metadatos = metadatos; this.asociaciones = asociaciones;
    }
    @Override public void execute(final AdministrarCatalogoAplicacionDomain datos) {
        if (datos == null || datos.getRecurso() == null || datos.getOperacion() == null)
            throw ValidationException.build("La operacion de administracion es obligatoria.");
        var app = datos.getIdAplicacion();
        if (aplicaciones.findById(app).isEmpty()) throw noExiste();
        if (datos.getOperacion() != AdministrarCatalogoAplicacionDomain.Operacion.CREAR
                && !pertenece(datos.getRecurso(), datos.getId(), app)) throw noExiste();
        if (datos.getOperacion() != AdministrarCatalogoAplicacionDomain.Operacion.ELIMINAR) {
            var valido = switch (datos.getRecurso()) {
                case MODULO -> app.equals(datos.getIdPadre());
                case FUNCIONALIDAD -> modulo(datos.getIdPadre(), app);
                case PARAMETRO -> funcionalidad(datos.getIdPadre(), app);
                case METADATO, METADATO_AMBIENTE -> parametro(datos.getIdPadre(), app);
            };
            if (!valido) throw noExiste();
        }
    }
    private boolean pertenece(final RecursoCatalogoAplicacion tipo, final UUID id, final UUID app) {
        return switch (tipo) {
            case MODULO -> modulo(id, app);
            case FUNCIONALIDAD -> funcionalidad(id, app);
            case PARAMETRO -> parametro(id, app);
            case METADATO -> metadatos.findById(id).map(e -> parametro(e.getIdParametro(), app)).orElse(false);
            case METADATO_AMBIENTE -> asociaciones.findById(id).map(e -> parametro(e.getIdParametro(), app)).orElse(false);
        };
    }
    private boolean modulo(final UUID id, final UUID app) {
        return modulos.findById(id).map(e -> app.equals(e.getIdAplicacion())).orElse(false);
    }
    private boolean funcionalidad(final UUID id, final UUID app) {
        return funcionalidades.findById(id).map(e -> modulo(e.getIdModulo(), app)).orElse(false);
    }
    private boolean parametro(final UUID id, final UUID app) {
        return parametros.findById(id).map(e -> funcionalidad(e.getIdFuncionalidad(), app)).orElse(false);
    }
    private NotFoundException noExiste() { return NotFoundException.build("El recurso no existe en esta aplicacion."); }
}
