package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor;

public interface InteractorWithReturn<T, R> {

    R execute(T data);
}