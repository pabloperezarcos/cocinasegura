package proyecto.cocinasegura.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthRequestTest {

    @Test
    void testGettersAndSetters() {
        AuthRequest authRequest = new AuthRequest();
        String nombreUsuario = "testUser";
        String contrasena = "testPassword";

        authRequest.setNombreUsuario(nombreUsuario);
        authRequest.setContrasena(contrasena);

        assertEquals(nombreUsuario, authRequest.getNombreUsuario());
        assertEquals(contrasena, authRequest.getContrasena());
    }
}
