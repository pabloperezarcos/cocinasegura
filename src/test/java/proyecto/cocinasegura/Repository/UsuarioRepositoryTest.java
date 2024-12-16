package proyecto.cocinasegura.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import proyecto.cocinasegura.Model.Usuario;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Sql({ "/schema.sql", "/data.sql" })
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombreUsuario("testUser" + System.currentTimeMillis());
        usuario.setContrasena("testContrasena");
        usuario.setCorreo("testuser" + System.currentTimeMillis() + "@example.com");
        usuario.setRoles("USER");
        usuarioRepository.save(usuario);
    }

    @Test
    void testFindByNombreUsuario_UserFound() {

        Optional<Usuario> foundUsuario = usuarioRepository.findByNombreUsuario("testUser");

        assertTrue(foundUsuario.isPresent(), "Usuario debería ser encontrado");
        assertEquals("testUser", foundUsuario.get().getNombreUsuario(), "El nombre de usuario debería coincidir");
    }

    @Test
    void testFindByNombreUsuario_UserNotFound() {
        Optional<Usuario> foundUsuario = usuarioRepository.findByNombreUsuario("nonexistentUser");

        assertFalse(foundUsuario.isPresent(), "Usuario no debería ser encontrado");
    }
}
