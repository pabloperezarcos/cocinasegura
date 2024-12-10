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
        // Crear un usuario para los tests
        usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        usuario.setContrasena("testContrasena");
        usuario.setCorreo("testuser" + System.currentTimeMillis() + "@example.com"); // Correo único
        usuario.setRoles("USER"); // Asignar un valor al campo roles
        usuarioRepository.save(usuario); // Guarda el usuario en la base de datos
    }

    @Test
    void testFindByNombreUsuario_UserFound() {
        // Act
        Optional<Usuario> foundUsuario = usuarioRepository.findByNombreUsuario("testUser");

        // Assert
        assertTrue(foundUsuario.isPresent(), "Usuario debería ser encontrado");
        assertEquals("testUser", foundUsuario.get().getNombreUsuario(), "El nombre de usuario debería coincidir");
    }

    @Test
    void testFindByNombreUsuario_UserNotFound() {
        // Act
        Optional<Usuario> foundUsuario = usuarioRepository.findByNombreUsuario("nonexistentUser");

        // Assert
        assertFalse(foundUsuario.isPresent(), "Usuario no debería ser encontrado");
    }
}
