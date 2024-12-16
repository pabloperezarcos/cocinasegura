package proyecto.cocinasegura.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioTest {

    @Test
    void testGettersAndSetters() {
        Usuario usuario = new Usuario();
        Long id = 1L;
        String nombreUsuario = "testUser";
        String correo = "test@example.com";
        String contrasena = "password";
        String roles = "ROLE_USER,ROLE_ADMIN";

        usuario.setId(id);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setRoles(roles);

        assertEquals(id, usuario.getId());
        assertEquals(nombreUsuario, usuario.getNombreUsuario());
        assertEquals(correo, usuario.getCorreo());
        assertEquals(contrasena, usuario.getContrasena());
        assertEquals(roles, usuario.getRoles());
    }

    @Test
    void testGetAuthorities() {
        Usuario usuario = new Usuario();
        usuario.setRoles("ROLE_USER,ROLE_ADMIN");

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserDetailsMethods() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        usuario.setContrasena("password");

        assertEquals("testUser", usuario.getUsername());
        assertEquals("password", usuario.getPassword());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    void testToString() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");

        String result = usuario.toString();

        assertEquals("testUser", result);
    }
}
