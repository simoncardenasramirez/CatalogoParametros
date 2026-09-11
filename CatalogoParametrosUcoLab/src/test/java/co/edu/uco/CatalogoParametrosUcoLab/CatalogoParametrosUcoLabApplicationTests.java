package co.edu.uco.CatalogoParametrosUcoLab;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.azure.security.keyvault.secrets.SecretClient;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

import static org.mockito.Mockito.verifyNoInteractions;

import co.edu.uco.CatalogoParametrosUcoLab.init.CatalogoParametrosUcoLabApplication;

@SpringBootTest(classes = CatalogoParametrosUcoLabApplication.class)
@ActiveProfiles("test")
class CatalogoParametrosUcoLabApplicationTests {

    @MockitoBean
    private SecretClient secretClient;

    @MockitoBean
    private SurrealDbClient surrealDbClient;

    @Test
    void debeCargarContextoCuandoNoHayServiciosExternos() {
        verifyNoInteractions(secretClient, surrealDbClient);
    }

}
