package proyecto.cocinasegura.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import proyecto.cocinasegura.Model.Comentario;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Model.Usuario;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Sql({ "/schema.sql", "/data.sql" })
public class ComentarioRepositoryTest {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Receta receta;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Crear y guardar una receta
        receta = new Receta();
        receta.setTitulo("Receta de prueba");
        receta.setTipoDeCocina("Postre");
        receta.setDescripcion("Descripción de prueba");
        receta.setDificultad("Fácil");
        receta.setIngredientes("Harina, Azúcar, Huevos");
        receta.setInstrucciones("Paso 1: Mezclar los ingredientes. Paso 2: Hornear.");
        receta.setPaisDeOrigen("Chile");
        receta.setTiempoDeCoccion("30");
        receta.setImagenURL("/imagen");
        receta.setVideoURL("video");
        recetaRepository.save(receta);

        // Crear y guardar un usuario
        usuario = new Usuario();
        usuario.setNombreUsuario("testUser" + System.currentTimeMillis());
        usuario.setContrasena("testContrasena");
        usuario.setCorreo("testuser3@example.com");
        usuario.setRoles("cliente");
        usuarioRepository.save(usuario);

        // Crear y guardar comentarios
        Comentario comentario1 = new Comentario();
        comentario1.setReceta(receta);
        comentario1.setUsuario(usuario);
        comentario1.setTexto("Excelente receta");
        comentario1.setValoracion(5);
        comentario1.setFecha(LocalDateTime.now());
        comentarioRepository.save(comentario1);

        Comentario comentario2 = new Comentario();
        comentario2.setReceta(receta);
        comentario2.setUsuario(usuario);
        comentario2.setTexto("Muy buena receta");
        comentario2.setValoracion(4);
        comentario2.setFecha(LocalDateTime.now());
        comentarioRepository.save(comentario2);
    }

    @Test
    void testFindByRecetaId_ComentariosFound() {
        // Act
        List<Comentario> comentarios = comentarioRepository.findByRecetaId(receta.getId());

        // Assert
        assertNotNull(comentarios, "La lista de comentarios no debe ser nula");
        assertFalse(comentarios.isEmpty(), "La lista de comentarios no debe estar vacía");
        assertEquals(2, comentarios.size(), "Debe haber 2 comentarios para la receta");
        assertEquals("Excelente receta", comentarios.get(0).getTexto());
        assertEquals("Muy buena receta", comentarios.get(1).getTexto());
    }

    @Test
    void testFindByRecetaId_ComentariosNotFound() {
        // Act
        List<Comentario> comentarios = comentarioRepository.findByRecetaId(999L); // ID inexistente

        // Assert
        assertNotNull(comentarios, "La lista de comentarios no debe ser nula");
        assertTrue(comentarios.isEmpty(), "La lista de comentarios debe estar vacía para un ID inexistente");
    }
}
