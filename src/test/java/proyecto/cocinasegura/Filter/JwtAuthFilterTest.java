package proyecto.cocinasegura.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import proyecto.cocinasegura.Controller.RecetaController;
import proyecto.cocinasegura.Services.JwtService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new RecetaController()) // Incluye tu controlador aquí
                .addFilters(jwtAuthFilter)
                .build();
    }

    @Test
    void testValidToken() throws Exception {
        String token = "validToken";
        String username = "testUser";
        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .roles("USER")
                .build();

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        String titulo = "Tarta de Manzana";
        String tipoDeCocina = "Postre";
        String ingredientes = "Manzanas, harina, azúcar";
        String paisDeOrigen = "España";
        String dificultad = "Fácil";
        String instrucciones = "Mezclar los ingredientes y hornear.";
        String tiempoDeCoccion = "30 minutos";
        String imagenURL = "http://example.com/tarta.jpg";
        String videoURL = "http://example.com/tarta.mp4";
        String descripcion = "Deliciosa tarta de manzana.";

        mockMvc.perform(post("/api/recetas")
                .header("Authorization", "Bearer " + token)
                .param("titulo", titulo)
                .param("tipoDeCocina", tipoDeCocina)
                .param("ingredientes", ingredientes)
                .param("paisDeOrigen", paisDeOrigen)
                .param("dificultad", dificultad)
                .param("instrucciones", instrucciones)
                .param("tiempoDeCoccion", tiempoDeCoccion)
                .param("imagenURL", imagenURL)
                .param("videoURL", videoURL)
                .param("descripcion", descripcion))
                .andExpect(status().isFound());

    }

    @Test
    void testInvalidToken() throws Exception {
        String token = "invalidToken";

        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/api/recetas")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testNoToken() throws Exception {
        mockMvc.perform(post("/api/recetas"))
                .andExpect(status().isUnauthorized());
    }

}
