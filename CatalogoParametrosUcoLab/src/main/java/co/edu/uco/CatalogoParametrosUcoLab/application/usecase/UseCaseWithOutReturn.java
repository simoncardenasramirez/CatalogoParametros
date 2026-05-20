package co.edu.uco.CatalogoParametrosUcoLab.application.usecase;

public interface UseCaseWithOutReturn<T> {

    void execute(T data);
}
