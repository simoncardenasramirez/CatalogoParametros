package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.catalogoaplicacion;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosCatalogoAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.publisher.CrearAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.publisher.ActualizarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.publisher.EliminarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.publisher.CrearModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.publisher.ActualizarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.secondaryports.publisher.EliminarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.publisher.CrearFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.publisher.EliminarFuncionalidadPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.publisher.ActualizarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.publisher.EliminarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.publisher.EliminarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.publisher.CrearAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.publisher.ActualizarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.publisher.EliminarAmbientePublisher;

@Component
public final class CambiosCatalogoAplicacionPublisherImpl implements CambiosCatalogoAplicacionPublisher {
    private final Sinks.Many<CambioCatalogoAplicacion> sink = Sinks.many().multicast().directBestEffort();
    private Disposable subscription;
    private final CrearAplicacionPublisher crearAplicacion;
    private final ActualizarAplicacionPublisher actualizarAplicacion;
    private final EliminarAplicacionPublisher eliminarAplicacion;
    private final CrearModuloPublisher crearModulo;
    private final ActualizarModuloPublisher actualizarModulo;
    private final EliminarModuloPublisher eliminarModulo;
    private final CrearFuncionalidadPublisher crearFuncionalidad;
    private final ActualizarFuncionalidadPublisher actualizarFuncionalidad;
    private final EliminarFuncionalidadPublisher eliminarFuncionalidad;
    private final CrearParametroPublisher crearParametro;
    private final ActualizarParametroPublisher actualizarParametro;
    private final EliminarParametroPublisher eliminarParametro;
    private final CrearMetadatoPublisher crearMetadato;
    private final ActualizarMetadatoPublisher actualizarMetadato;
    private final EliminarMetadatoPublisher eliminarMetadato;
    private final CrearMetadatoAmbientePublisher crearMetadatoAmbiente;
    private final ActualizarMetadatoAmbientePublisher actualizarMetadatoAmbiente;
    private final EliminarMetadatoAmbientePublisher eliminarMetadatoAmbiente;
    private final CrearAmbientePublisher crearAmbiente;
    private final ActualizarAmbientePublisher actualizarAmbiente;
    private final EliminarAmbientePublisher eliminarAmbiente;
    public CambiosCatalogoAplicacionPublisherImpl(final CrearAplicacionPublisher crearAplicacion,
            final ActualizarAplicacionPublisher actualizarAplicacion,
            final EliminarAplicacionPublisher eliminarAplicacion,
            final CrearModuloPublisher crearModulo,
            final ActualizarModuloPublisher actualizarModulo,
            final EliminarModuloPublisher eliminarModulo,
            final CrearFuncionalidadPublisher crearFuncionalidad,
            final ActualizarFuncionalidadPublisher actualizarFuncionalidad,
            final EliminarFuncionalidadPublisher eliminarFuncionalidad,
            final CrearParametroPublisher crearParametro,
            final ActualizarParametroPublisher actualizarParametro,
            final EliminarParametroPublisher eliminarParametro,
            final CrearMetadatoPublisher crearMetadato,
            final ActualizarMetadatoPublisher actualizarMetadato,
            final EliminarMetadatoPublisher eliminarMetadato,
            final CrearMetadatoAmbientePublisher crearMetadatoAmbiente,
            final ActualizarMetadatoAmbientePublisher actualizarMetadatoAmbiente,
            final EliminarMetadatoAmbientePublisher eliminarMetadatoAmbiente,
            final CrearAmbientePublisher crearAmbiente,
            final ActualizarAmbientePublisher actualizarAmbiente,
            final EliminarAmbientePublisher eliminarAmbiente) {
        this.crearAplicacion = crearAplicacion;
        this.actualizarAplicacion = actualizarAplicacion;
        this.eliminarAplicacion = eliminarAplicacion;
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
        this.crearAmbiente = crearAmbiente;
        this.actualizarAmbiente = actualizarAmbiente;
        this.eliminarAmbiente = eliminarAmbiente;
    }
    @PostConstruct void iniciar() {
        // Los historicos replay se consumen al arrancar; las conexiones nuevas reciben cambios vivos.
        subscription = Flux.merge(
                crearAplicacion.getStream().map(e -> new CambioCatalogoAplicacion("aplicaciones", e.getEvent().name(), e.getAplicacion().getId(), e.getAplicacion().getIdOrganizacion())),
                actualizarAplicacion.getStream().map(e -> new CambioCatalogoAplicacion("aplicaciones", e.getEvent().name(), e.getAplicacion().getId(), e.getAplicacion().getIdOrganizacion())),
                eliminarAplicacion.getStream().map(e -> new CambioCatalogoAplicacion("aplicaciones", e.getEvent().name(), e.getAplicacion().getId(), e.getAplicacion().getIdOrganizacion())),
                crearModulo.getStream().map(e -> new CambioCatalogoAplicacion("modulos", e.getEvent().name(), e.getModulo().getId(), e.getModulo().getIdAplicacion())),
                actualizarModulo.getStream().map(e -> new CambioCatalogoAplicacion("modulos", e.getEvent().name(), e.getModulo().getId(), e.getModulo().getIdAplicacion())),
                eliminarModulo.getStream().map(e -> new CambioCatalogoAplicacion("modulos", e.getEvent().name(), e.getModulo().getId(), e.getModulo().getIdAplicacion())),
                crearFuncionalidad.getStream().map(e -> new CambioCatalogoAplicacion("funcionalidades", e.getEvent().name(), e.getFuncionalidad().getId(), e.getFuncionalidad().getIdModulo())),
                actualizarFuncionalidad.getStream().map(e -> new CambioCatalogoAplicacion("funcionalidades", e.getEvent().name(), e.getFuncionalidad().getId(), e.getFuncionalidad().getIdModulo())),
                eliminarFuncionalidad.getStream().map(e -> new CambioCatalogoAplicacion("funcionalidades", e.getEvent().name(), e.getFuncionalidad().getId(), e.getFuncionalidad().getIdModulo())),
                crearParametro.getStream().map(e -> new CambioCatalogoAplicacion("parametros", e.getEvent().name(), e.getParametro().getId(), e.getParametro().getIdFuncionalidad())),
                actualizarParametro.getStream().map(e -> new CambioCatalogoAplicacion("parametros", e.getEvent().name(), e.getParametro().getId(), e.getParametro().getIdFuncionalidad())),
                eliminarParametro.getStream().map(e -> new CambioCatalogoAplicacion("parametros", e.getEvent().name(), e.getParametro().getId(), e.getParametro().getIdFuncionalidad())),
                crearMetadato.getStream().map(e -> new CambioCatalogoAplicacion("metadatos", e.getEvent().name(), e.getMetadato().getId(), e.getMetadato().getIdParametro())),
                actualizarMetadato.getStream().map(e -> new CambioCatalogoAplicacion("metadatos", e.getEvent().name(), e.getMetadato().getId(), e.getMetadato().getIdParametro())),
                eliminarMetadato.getStream().map(e -> new CambioCatalogoAplicacion("metadatos", e.getEvent().name(), e.getMetadato().getId(), e.getMetadato().getIdParametro())),
                crearMetadatoAmbiente.getStream().map(e -> new CambioCatalogoAplicacion("metadatos-ambiente", e.getEvent().name(), e.getMetadatoAmbiente().getId(), e.getMetadatoAmbiente().getIdParametro())),
                actualizarMetadatoAmbiente.getStream().map(e -> new CambioCatalogoAplicacion("metadatos-ambiente", e.getEvent().name(), e.getMetadatoAmbiente().getId(), e.getMetadatoAmbiente().getIdParametro())),
                eliminarMetadatoAmbiente.getStream().map(e -> new CambioCatalogoAplicacion("metadatos-ambiente", e.getEvent().name(), e.getMetadatoAmbiente().getId(), e.getMetadatoAmbiente().getIdParametro())),
                crearAmbiente.getStream().map(e -> new CambioCatalogoAplicacion("ambientes", e.getEvent().name(), e.getAmbiente().getId(), null)),
                actualizarAmbiente.getStream().map(e -> new CambioCatalogoAplicacion("ambientes", e.getEvent().name(), e.getAmbiente().getId(), null)),
                eliminarAmbiente.getStream().map(e -> new CambioCatalogoAplicacion("ambientes", e.getEvent().name(), e.getAmbiente().getId(), null))
        ).subscribe(evento -> sink.tryEmitNext(evento));
    }
    @Override public Flux<CambioCatalogoAplicacion> getStream() {
        // Resolver pertenencia puede consultar repositorios sin ocupar los hilos HTTP reactivos.
        return sink.asFlux().publishOn(Schedulers.boundedElastic());
    }
    @PreDestroy void cerrar() { if (subscription != null) subscription.dispose(); }
}
