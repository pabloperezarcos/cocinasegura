package proyecto.cocinasegura.Model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Usuario usuario = new Usuario();
        Long id = 1L;
        String nombreUsuario = "testUser";
        String correo = "test@example.com";
        String contrasena = "password";
        String roles = "ROLE_USER,ROLE_ADMIN";

        // Act
        usuario.setId(id);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setRoles(roles);

        // Assert
        assertEquals(id, usuario.getId());
        assertEquals(nombreUsuario, usuario.getNombreUsuario());
        assertEquals(correo, usuario.getCorreo());
        assertEquals(contrasena, usuario.getContrasena());
        assertEquals(roles, usuario.getRoles());
    }

    @Test
    void testGetAuthorities() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setRoles("ROLE_USER,ROLE_ADMIN");

        // Act
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        // Assert
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserDetailsMethods() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        usuario.setContrasena("password");

        // Act & Assert
        assertEquals("testUser", usuario.getUsername());
        assertEquals("password", usuario.getPassword());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    void testToString() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");

        // Act
        String result = usuario.toString();

        // Assert
        assertEquals("testUser", result);
    }
}
