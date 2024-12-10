package proyecto.cocinasegura.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
import proyecto.cocinasegura.Model.Usuario;

import java.util.Optional;

@DataJpaTest
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
        usuarioRepository.save(usuario);
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
