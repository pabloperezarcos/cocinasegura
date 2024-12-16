package proyecto.cocinasegura.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import proyecto.cocinasegura.Services.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

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

        SecurityContextHolder.clearContext();

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUsername("token")).thenReturn("testUser");
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.validateToken("token", userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

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

        verify(response, never()).sendError(anyInt(), anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testAlreadyAuthenticatedContext() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("testUser");
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.validateToken("validToken", userDetails)).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("preAuthUser", null, userDetails.getAuthorities()));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, never()).sendError(anyInt(), anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testUsernameNullAfterExtraction() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testUsernameNullWithNonNullAuthHeader() throws Exception {
        
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        
        when(jwtService.extractUsername("validToken")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain, never()).doFilter(any(), any());
    }

}
