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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(post("/api/recetas")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
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
        mockMvc.perform(get("/api/recetas"))
                .andExpect(status().isUnauthorized());
    }
}
