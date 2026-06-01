package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports;

public interface InteractorWithReturn<T, R> {

    R execute(T data);
}