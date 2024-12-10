package proyecto.cocinasegura.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecetaControllerTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetaController recetaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recetaController).build();
    }

    @Test
    void testCrearReceta_Success() throws Exception {
        // Arrange
        Receta receta = new Receta();
        receta.setTitulo("Tarta de Manzana");
        receta.setTiempoDeCoccion("30 minutos");

        // Simula el comportamiento del repositorio asignando el ID automáticamente
        when(recetaRepository.save(any(Receta.class))).thenAnswer(invocation -> {
            Receta savedReceta = invocation.getArgument(0);
            savedReceta.getClass().getDeclaredField("id").setAccessible(true);
            savedReceta.getClass().getDeclaredField("id").set(savedReceta, 1L);
            return savedReceta;
        });

        MockMultipartFile imagen = new MockMultipartFile("imagen", "imagen.jpg", "image/jpeg", "contenido".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/recetas")
                .file(imagen)
                .param("titulo", "Tarta de Manzana")
                .param("tiempoDeCoccion", "30 minutos")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/recetas?id=1"));
    }

    @Test
    void testCrearReceta_MissingTitle() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/api/recetas")
                .param("tiempoDeCoccion", "30 minutos")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El título de la receta es obligatorio."));
    }

    @Test
    void testAgregarMedia_Success() throws Exception {
        // Arrange
        Receta receta = new Receta();
        receta.setTitulo("Tarta de Manzana");
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        MockMultipartFile foto = new MockMultipartFile("fotos", "foto.jpg", "image/jpeg", "contenido".getBytes());
        MockMultipartFile video = new MockMultipartFile("videos", "video.mp4", "video/mp4", "contenido".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/recetas/1/media")
                .file(foto)
                .file(video)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Fotos y/o videos agregados correctamente a la receta."));
    }

    @Test
    void testAgregarMedia_RecipeNotFound() throws Exception {
        // Arrange
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        MockMultipartFile foto = new MockMultipartFile("fotos", "foto.jpg", "image/jpeg", "contenido".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/recetas/999/media")
                .file(foto)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Receta no encontrada."));
    }
}
