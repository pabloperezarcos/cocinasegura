package proyecto.cocinasegura.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(SecurityConfig.class) // Importar la clase SecurityConfig para probarla
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPublicRoutesAreAccessible() throws Exception {
        // Verificar que las rutas públicas no requieren autenticación
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/buscar"))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedRoutesRequireAuthentication() throws Exception {
        // Verificar que una ruta protegida requiere autenticación
        mockMvc.perform(get("/protected"))
                .andExpect(status().is3xxRedirection()) // Redirección a /login
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void testPasswordEncoderIsBCrypt() {
        // Verificar que el PasswordEncoder es BCryptPasswordEncoder
        assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder,
                "El PasswordEncoder debe ser una instancia de BCryptPasswordEncoder");
    }
}
