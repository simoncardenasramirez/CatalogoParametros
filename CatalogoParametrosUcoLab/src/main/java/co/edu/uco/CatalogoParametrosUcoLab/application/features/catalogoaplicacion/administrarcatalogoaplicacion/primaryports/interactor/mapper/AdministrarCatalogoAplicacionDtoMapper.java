package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.interactor.mapper;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto.AdministrarCatalogoAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.*;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoRequest;

public enum AdministrarCatalogoAplicacionDtoMapper {
    INSTANCE;
    public AdministrarCatalogoAplicacionDomain toDomain(final UUID app, final String recurso, final UUID id,
            final AdministrarCatalogoAplicacionDtoRequest data, final AdministrarCatalogoAplicacionDomain.Operacion operacion) {
        var tipo = RecursoCatalogoAplicacion.desde(recurso);
        if (data == null && operacion != AdministrarCatalogoAplicacionDomain.Operacion.ELIMINAR)
            throw ValidationException.build("Los datos de la operacion son obligatorios.");
        var padre = operacion == AdministrarCatalogoAplicacionDomain.Operacion.ELIMINAR ? null : switch (tipo) {
            case MODULO -> app;
            case FUNCIONALIDAD -> uuid(data.idModulo());
            case PARAMETRO -> uuid(data.idFuncionalidad());
            case METADATO, METADATO_AMBIENTE -> uuid(data.idParametro());
        };
        return AdministrarCatalogoAplicacionDomain.create(id, app, padre, tipo, operacion);
    }
    private UUID uuid(final String valor) {
        try { return UUID.fromString(valor); }
        catch (final IllegalArgumentException | NullPointerException exception) {
            throw ValidationException.build("El identificador del registro padre no es valido.");
        }
    }
    private String fecha(final String valor) {
        if (valor == null || valor.isBlank()) throw ValidationException.build("Las fechas de inicio y fin son obligatorias para este recurso.");
        co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.ValidateHelper.validateFecha(valor, "fecha de vigencia");
        return valor.trim();
    }
    private String activo(final AdministrarCatalogoAplicacionDtoRequest data) {
        return String.valueOf(data.activo() == null || data.activo());
    }
    public CrearModuloDtoRequest crearModulo(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return CrearModuloDtoRequest.create(data.nombre(), app.toString(), activo(data), fecha(data.fechaInicio()), fecha(data.fechaFinal()));
    }
    public ActualizarModuloDtoRequest actualizarModulo(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return ActualizarModuloDtoRequest.create(data.nombre(), app.toString(), activo(data), fecha(data.fechaInicio()), fecha(data.fechaFinal()));
    }
    public CrearFuncionalidadDtoRequest crearFuncionalidad(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return CrearFuncionalidadDtoRequest.create(data.nombre(), data.idModulo(), activo(data), fecha(data.fechaInicio()), fecha(data.fechaFinal()));
    }
    public ActualizarFuncionalidadDtoRequest actualizarFuncionalidad(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return ActualizarFuncionalidadDtoRequest.create(data.nombre(), data.idModulo(), activo(data), fecha(data.fechaInicio()), fecha(data.fechaFinal()));
    }
    public CrearParametroDtoRequest crearParametro(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return CrearParametroDtoRequest.create(data.nombre(), data.idFuncionalidad(), data.idTipoParametro(), activo(data));
    }
    public ActualizarParametroDtoRequest actualizarParametro(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return ActualizarParametroDtoRequest.create(data.nombre(), data.idFuncionalidad(), data.idTipoParametro(), activo(data));
    }
    public CrearMetadatoDtoRequest crearMetadato(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return CrearMetadatoDtoRequest.create(data.idParametro(), data.idTipoMetadato(), data.idAmbiente(), data.valor());
    }
    public ActualizarMetadatoDtoRequest actualizarMetadato(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return ActualizarMetadatoDtoRequest.create(data.idParametro(), data.idTipoMetadato(), data.idAmbiente(), data.valor());
    }
    public CrearMetadatoAmbienteDtoRequest crearMetadatoAmbiente(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return CrearMetadatoAmbienteDtoRequest.create(data.idParametro(), data.idAmbiente(), data.idEstadoMetadatoAmbiente());
    }
    public ActualizarMetadatoAmbienteDtoRequest actualizarMetadatoAmbiente(final UUID app, final AdministrarCatalogoAplicacionDtoRequest data) {
        return ActualizarMetadatoAmbienteDtoRequest.create(data.idParametro(), data.idAmbiente(), data.idEstadoMetadatoAmbiente());
    }
}
