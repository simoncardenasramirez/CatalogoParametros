package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.vigencia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import org.springframework.stereotype.Component;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.RegistroVigencia;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosEstadoCatalogoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event.ActualizarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event.ActualizarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.event.ActualizarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;

@Component
public final class CambiosEstadoCatalogoPublisherImpl implements CambiosEstadoCatalogoPublisher {
    private final BaseDatosAplicacionRepository aplicacion;
    private final ActualizarAplicacionPublisher publicarAplicacion;
    private final BaseDatosModuloRepository modulo;
    private final ActualizarModuloPublisher publicarModulo;
    private final BaseDatosFuncionalidadRepository funcionalidad;
    private final ActualizarFuncionalidadPublisher publicarFuncionalidad;
    private final BaseDatosParametroRepository parametro;
    private final ActualizarParametroPublisher publicarParametro;
    private final BaseDatosMetadatoAmbienteRepository metadatoambiente;
    private final ActualizarMetadatoAmbientePublisher publicarMetadatoAmbiente;

    public CambiosEstadoCatalogoPublisherImpl(
            final BaseDatosAplicacionRepository aplicacion, final ActualizarAplicacionPublisher publicarAplicacion,
            final BaseDatosModuloRepository modulo, final ActualizarModuloPublisher publicarModulo,
            final BaseDatosFuncionalidadRepository funcionalidad, final ActualizarFuncionalidadPublisher publicarFuncionalidad,
            final BaseDatosParametroRepository parametro, final ActualizarParametroPublisher publicarParametro,
            final BaseDatosMetadatoAmbienteRepository metadatoambiente, final ActualizarMetadatoAmbientePublisher publicarMetadatoAmbiente) {
        this.aplicacion = aplicacion;
        this.publicarAplicacion = publicarAplicacion;
        this.modulo = modulo;
        this.publicarModulo = publicarModulo;
        this.funcionalidad = funcionalidad;
        this.publicarFuncionalidad = publicarFuncionalidad;
        this.parametro = parametro;
        this.publicarParametro = publicarParametro;
        this.metadatoambiente = metadatoambiente;
        this.publicarMetadatoAmbiente = publicarMetadatoAmbiente;
    }
    @Override
    public void publicar(final List<RegistroVigencia> registros) {
        publicarGrupo(registros, TipoRegistroVigencia.APLICACION, aplicacion::findById, aplicacion::findAll,
                AplicacionEntity::getId, e -> publicarAplicacion.sendEvent(ActualizarAplicacionEvent.updated(e)));
        publicarGrupo(registros, TipoRegistroVigencia.MODULO, modulo::findById, modulo::findAll,
                ModuloEntity::getId, e -> publicarModulo.sendEvent(ActualizarModuloEvent.updated(e)));
        publicarGrupo(registros, TipoRegistroVigencia.FUNCIONALIDAD, funcionalidad::findById, funcionalidad::findAll,
                FuncionalidadEntity::getId, e -> publicarFuncionalidad.sendEvent(ActualizarFuncionalidadEvent.updated(e)));
        publicarGrupo(registros, TipoRegistroVigencia.PARAMETRO, parametro::findById, parametro::findAll,
                ParametroEntity::getId, e -> publicarParametro.sendEvent(ActualizarParametroEvent.updated(e)));
        publicarGrupo(registros, TipoRegistroVigencia.METADATO_AMBIENTE, metadatoambiente::findById,
                metadatoambiente::findAll, MetadatoAmbienteEntity::getId,
                e -> publicarMetadatoAmbiente.sendEvent(ActualizarMetadatoAmbienteEvent.updated(e)));
    }

    private <E> void publicarGrupo(final List<RegistroVigencia> registros, final TipoRegistroVigencia tipo,
            final Function<UUID, Optional<E>> consultarUno, final Supplier<List<E>> consultarTodos,
            final Function<E, UUID> identificador, final Consumer<E> emitir) {
        var ids = registros.stream().filter(r -> r.tipo() == tipo).map(RegistroVigencia::id).collect(Collectors.toSet());
        if (ids.isEmpty()) return;
        if (ids.size() == 1) consultarUno.apply(ids.iterator().next()).ifPresent(emitir);
        else consultarTodos.get().stream().filter(e -> ids.contains(identificador.apply(e))).forEach(emitir);
    }
}
