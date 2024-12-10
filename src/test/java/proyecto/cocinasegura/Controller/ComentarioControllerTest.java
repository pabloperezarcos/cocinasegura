package proyecto.cocinasegura.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import proyecto.cocinasegura.Model.Comentario;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Model.Usuario;
import proyecto.cocinasegura.Repository.ComentarioRepository;
import proyecto.cocinasegura.Repository.RecetaRepository;
import proyecto.cocinasegura.Repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ComentarioControllerTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComentarioController comentarioController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(comentarioController).build();
    }

    @Test
    void testObtenerComentariosPorReceta() throws Exception {
        // Arrange
        Long recetaId = 1L;
        List<Comentario> comentarios = new ArrayList<>();
        Comentario comentario = new Comentario();
        comentario.setTexto("Deliciosa receta");
        comentario.setFecha(LocalDateTime.now());
        comentario.setValoracion(5); // Un valor entre 1 y 5

        comentarios.add(comentario);

        // Simula la respuesta del repositorio
        when(comentarioRepository.findByRecetaId(recetaId)).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/api/comentarios/{recetaId}", recetaId))
                .andExpect(status().isOk()) // Verifica que el estado HTTP es 200
                .andExpect(jsonPath("$[0].texto").value("Deliciosa receta")); // Verifica el JSON
    }

    @Test
    void testAgregarComentario_Success() throws Exception {
        // Arrange
        Long recetaId = 1L;
        Long usuarioId = 1L;

        Receta receta = new Receta();
        receta.setTitulo("Tarta de Manzana");
        when(recetaRepository.findById(recetaId)).thenReturn(Optional.of(receta));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        Comentario comentario = new Comentario();
        comentario.setTexto("Excelente receta");

        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);

        // Act & Assert
        mockMvc.perform(post("/api/comentarios/{recetaId}", recetaId)
                .param("texto", "Excelente receta")
                .param("usuarioId", usuarioId.toString())
                .param("valoracion", "5")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/recetas?id=1&mensaje=Comentario agregado exitosamente"));
    }

    @Test
    void testAgregarComentario_RecetaOrUsuarioNotFound() throws Exception {
        // Arrange
        Long recetaId = 1L;
        Long usuarioId = 1L;

        when(recetaRepository.findById(recetaId)).thenReturn(Optional.empty());
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/comentarios/{recetaId}", recetaId)
                .param("texto", "Excelente receta")
                .param("usuarioId", usuarioId.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Receta o Usuario no encontrado."));
    }
}
