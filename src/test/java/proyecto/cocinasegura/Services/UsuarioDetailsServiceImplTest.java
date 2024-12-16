package proyecto.cocinasegura.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import proyecto.cocinasegura.Model.Usuario;
import proyecto.cocinasegura.Repository.UsuarioRepository;
import proyecto.cocinasegura.Services.UsuarioDetailsServiceImpl;

import java.util.Optional;

public class UsuarioDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        usuario.setContrasena("testContrasena");

        when(usuarioRepository.findByNombreUsuario("testUser")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals("testUser", userDetails.getUsername(), "Username should match");
        assertEquals("testContrasena", userDetails.getPassword(), "Contrasena should match");
        verify(usuarioRepository, times(1)).findByNombreUsuario("testUser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(usuarioRepository.findByNombreUsuario("nonexistentUser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername("nonexistentUser"),
                "Expected UsernameNotFoundException to be thrown");

        assertEquals("Usuario no encontrado: nonexistentUser", exception.getMessage());
        verify(usuarioRepository, times(1)).findByNombreUsuario("nonexistentUser");
    }
}
