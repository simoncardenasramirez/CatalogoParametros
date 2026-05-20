package co.edu.uco.CatalogoParametrosUcoLab.domain;

public interface DomainRule<T> {

    void execute(T data);
}
