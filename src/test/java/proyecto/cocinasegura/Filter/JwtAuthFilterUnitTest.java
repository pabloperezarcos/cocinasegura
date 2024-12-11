package proyecto.cocinasegura.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import proyecto.cocinasegura.Services.JwtService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtAuthFilterUnitTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testInvalidAuthorizationHeader_NoBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("SomeTokenWithoutBearer");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testExtractUsernameThrowsException() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testValidateTokenFalse() throws Exception {
        // Asegurar que no haya autenticación previa
        SecurityContextHolder.clearContext();

        // Configurar mock del request y de los servicios para simular un token inválido
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUsername("token")).thenReturn("testUser");
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.validateToken("token", userDetails)).thenReturn(false);

        // Ejecutar el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verificar que se retorne 401 Unauthorized y no se continúe con la cadena de
        // filtros
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testValidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("testUser");
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.validateToken("validToken", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Con token válido, no se produce error y se continúa con la cadena
        verify(response, never()).sendError(anyInt(), anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
