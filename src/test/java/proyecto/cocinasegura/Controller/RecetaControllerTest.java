package proyecto.cocinasegura.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

//import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class RecetaControllerTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetaController recetaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Setup MockMvc with error handling
        mockMvc = MockMvcBuilders
                .standaloneSetup(recetaController)
                .setControllerAdvice(new Object()) // Add a global exception handler
                .build();

        // Ensure test directories exist for file uploads
        try {
            Files.createDirectories(Paths.get("src/main/resources/static/images"));
        } catch (Exception e) {
            fail("Could not create test directories: " + e.getMessage());
        }
    }

    @Test
    void testCrearReceta_Success() throws Exception {
        // Arrange
        Receta recetaMock = new Receta();
        recetaMock.setId(1L);
        recetaMock.setTitulo("Tarta de Manzana");
        recetaMock.setTiempoDeCoccion("30 minutos");
        recetaMock.setTipoDeCocina("Postre");
        recetaMock.setIngredientes("Manzanas, harina, azúcar");
        recetaMock.setPaisDeOrigen("España");
        recetaMock.setDificultad("Fácil");
        recetaMock.setInstrucciones("Mezclar los ingredientes y hornear.");
        recetaMock.setDescripcion("Deliciosa tarta de manzana.");

        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaMock);

        MockMultipartFile imagen = new MockMultipartFile("imagen", "imagen.jpg", "image/jpeg",
                "contenido_imagen".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/recetas")
                .file(imagen)
                .with(user("testuser").roles("USER")) // Añadir contexto de seguridad que proporcione ROLE_USER
                .param("titulo", "Tarta de Manzana")
                .param("tiempoDeCoccion", "30 minutos")
                .param("tipoDeCocina", "Postre")
                .param("ingredientes", "Manzanas, harina, azúcar")
                .param("paisDeOrigen", "España")
                .param("dificultad", "Fácil")
                .param("instrucciones", "Mezclar los ingredientes y hornear.")
                .param("descripcion", "Deliciosa tarta de manzana.")
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
        RecetaController recetaControllerSpy = spy(recetaController); // Crear un espía del controlador

        // Mockear el método guardarArchivo para evitar dependencia del sistema de
        // archivos
        doReturn("/images/foto.jpg").when(recetaControllerSpy).guardarArchivo(any(MultipartFile.class),
                eq("src/main/resources/static/images/"));
        doReturn("/videos/video.mp4").when(recetaControllerSpy).guardarArchivo(any(MultipartFile.class),
                eq("src/main/resources/static/videos/"));

        // Configurar MockMvc con el controlador espía
        mockMvc = MockMvcBuilders.standaloneSetup(recetaControllerSpy).build();

        // Crear una receta simulada
        Receta receta = new Receta();
        receta.setTitulo("Tarta de Manzana");
        receta.setFotos(new ArrayList<>());
        receta.setVideos(new ArrayList<>());

        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        // Crear archivos simulados
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
