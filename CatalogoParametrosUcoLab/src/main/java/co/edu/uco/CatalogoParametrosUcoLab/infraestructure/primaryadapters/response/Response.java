package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private final List<String> mensajes = new ArrayList<>();

    public List<String> getMensajes() {
        return mensajes;
    }
}
