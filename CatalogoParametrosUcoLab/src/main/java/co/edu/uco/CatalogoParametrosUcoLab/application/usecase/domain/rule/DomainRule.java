package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule;

public interface DomainRule<T> {

    void execute(T data);
}
