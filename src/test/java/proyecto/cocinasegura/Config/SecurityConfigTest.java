package proyecto.cocinasegura.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import proyecto.cocinasegura.Filter.JwtAuthFilter;
import proyecto.cocinasegura.Repository.ComentarioRepository;
import proyecto.cocinasegura.Repository.RecetaRepository;
import proyecto.cocinasegura.Repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class SecurityConfigTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ComentarioRepository comentarioRepository;

        @MockBean
        private RecetaRepository recetaRepository;

        @MockBean
        private UsuarioRepository usuarioRepository;

        @MockBean
        private JwtAuthFilter jwtAuthFilter;

        @MockBean
        private UserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Test
        void testPublicRoutesAreAccessible() throws Exception {
                mockMvc.perform(get("/")).andExpect(status().isOk());
                mockMvc.perform(get("/login")).andExpect(status().isOk());
                mockMvc.perform(get("/buscar")).andExpect(status().isOk());
        }

        @Test
        void testProtectedRoutesRequireAuthentication() throws Exception {
                mockMvc.perform(get("/protected"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrlPattern("**/login"));
        }

        @Test
        void testPasswordEncoderIsBCrypt() {
                assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder,
                                "El PasswordEncoder debe ser una instancia de BCryptPasswordEncoder");
        }
}
