package proyecto.cocinasegura.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
import proyecto.cocinasegura.Services.JwtService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
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
        mockMvc = MockMvcBuilders.standaloneSetup()
                .addFilters(jwtAuthFilter) // Agregar el filtro a MockMvc
                .build();
    }

    @Test
    void testValidToken() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(get("/test")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidToken() throws Exception {
        // Arrange
        String token = "invalidToken";

        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        mockMvc.perform(get("/test")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()); // Aún debería pasar porque el filtro no bloquea
    }

    @Test
    void testNoToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk()); // Aún debería pasar porque el filtro no bloquea
    }
}
