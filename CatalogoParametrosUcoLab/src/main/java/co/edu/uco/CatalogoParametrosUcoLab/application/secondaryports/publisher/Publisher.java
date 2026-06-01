package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher;

import reactor.core.publisher.Flux;

public interface Publisher<T> {

    void sendEvent(T event);

    Flux<T> getStream();
}
