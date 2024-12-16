package proyecto.cocinasegura.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RecetaControllerTest {

        @Mock
        private RecetaRepository recetaRepository;

        @InjectMocks
        private RecetaController recetaController;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders
                                .standaloneSetup(recetaController)
                                .build();
        }

        @Test
        void testagregarMedia_InvalidImageFormat() throws Exception {
                MockMultipartFile invalidImage = new MockMultipartFile("imagen", "imagen.txt", "text/plain",
                                "contenido".getBytes());

                mockMvc.perform(multipart("/api/recetas")
                                .file(invalidImage)
                                .with(user("testuser").roles("USER"))
                                .param("titulo", "Tarta de Manzana")
                                .param("tiempoDeCoccion", "30 minutos")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isInternalServerError())
                                .andExpect(content().string("Error al crear la receta."));
        }

        @Test
        void testagregarMedia_NoUserAuthentication() throws Exception {
                mockMvc.perform(multipart("/api/recetas")
                                .param("titulo", "Tarta de Manzana")
                                .param("tiempoDeCoccion", "30 minutos")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        void testagregarMedia_Success() throws Exception {
                MockMultipartFile validImage = new MockMultipartFile("imagen", "imagen.jpg", "image/jpeg",
                                "contenido".getBytes());

                when(recetaRepository.save(any(Receta.class))).thenReturn(new Receta());

                mockMvc.perform(multipart("/api/recetas")
                                .file(validImage)
                                .with(user("testuser").roles("ADMIN"))
                                .param("titulo", "Tarta de Manzana")
                                .param("tiempoDeCoccion", "30 minutos")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Receta creada exitosamente."));
        }

        @Test
        void testAgregarMedia_InvalidFileFormat() throws Exception {
                Receta receta = new Receta();
                receta.setTitulo("Tarta de Manzana");
                receta.setFotos(new ArrayList<>());
                receta.setVideos(new ArrayList<>());

                when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

                MockMultipartFile invalidFile = new MockMultipartFile("fotos", "archivo.exe",
                                "application/octet-stream",
                                "contenido".getBytes());

                mockMvc.perform(multipart("/api/recetas/1/media")
                                .file(invalidFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isInternalServerError())
                                .andExpect(content().string("Error al guardar archivos."));
        }

        @Test
        void testAgregarMedia_RecipeNotFound() throws Exception {
                when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

                MockMultipartFile validFile = new MockMultipartFile("fotos", "imagen.jpg", "image/jpeg",
                                "contenido".getBytes());

                mockMvc.perform(multipart("/api/recetas/999/media")
                                .file(validFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testAgregarVideo_InvalidYouTubeURL() throws Exception {
                Receta receta = new Receta();
                receta.setId(1L);
                when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

                mockMvc.perform(post("/api/recetas/1/video")
                                .param("videoURL", "https://invalidurl.com")
                                .with(user("testuser").roles("USER"))
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                                .andExpect(status().isFound())
                                .andExpect(header().string("Location", "/recetas?id=1"));
        }

        @Test
        void testAgregarVideo_Success() throws Exception {
                Receta receta = new Receta();
                receta.setId(1L);
                when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

                mockMvc.perform(post("/api/recetas/1/video")
                                .param("videoURL", "https://youtube.com/watch?v=example")
                                .with(user("testuser").roles("USER"))
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                                .andExpect(status().isFound())
                                .andExpect(header().string("Location", "/recetas?id=1"));
        }

        @Test
        void testAgregarVideo_RecipeNotFound() throws Exception {
                when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

                mockMvc.perform(post("/api/recetas/999/video")
                                .param("videoURL", "https://youtube.com/watch?v=example")
                                .with(user("testuser").roles("USER"))
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                                .andExpect(status().isFound())
                                .andExpect(header().string("Location", "/recetas"));
        }

        @Test
        void testEliminarReceta_NotFound() throws Exception {
                when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/recetas/999")
                                .with(user("testuser").roles("ADMIN")))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testCrearReceta_TituloObligatorio() {
                // Arrange
                Receta receta = new Receta();
                receta.setTiempoDeCoccion("30 minutos");

                // Act
                ResponseEntity<String> response = recetaController.crearReceta(receta, null);

                // Assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("El título de la receta es obligatorio.", response.getBody());
                verifyNoInteractions(recetaRepository);
        }

        @Test
        public void testCrearReceta_TiempoDeCoccionObligatorio() {
                // Arrange
                Receta receta = new Receta();
                receta.setTitulo("Pasta");

                // Act
                ResponseEntity<String> response = recetaController.crearReceta(receta, null);

                // Assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("El tiempo de cocción es obligatorio.", response.getBody());
                verifyNoInteractions(recetaRepository);
        }

        @Test
        public void testCrearReceta_ConImagen() throws Exception {
                // Arrange
                Receta receta = new Receta();
                receta.setTitulo("Pasta");
                receta.setTiempoDeCoccion("30 minutos");

                MockMultipartFile imagen = new MockMultipartFile("imagen", "imagen.jpg", "image/jpeg",
                                "data".getBytes());
                Receta savedReceta = new Receta();
                savedReceta.setImagenURL("ruta/imagen.jpg");

                when(recetaRepository.save(any(Receta.class))).thenReturn(savedReceta);

                // Act
                ResponseEntity<String> response = recetaController.crearReceta(receta, imagen);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Receta creada exitosamente.", response.getBody());
                verify(recetaRepository, times(1)).save(any(Receta.class));
        }

        @Test
        public void testCrearReceta_Excepcion() {
                // Arrange
                Receta receta = new Receta();
                receta.setTitulo("Pasta");
                receta.setTiempoDeCoccion("30 minutos");

                when(recetaRepository.save(any(Receta.class)))
                                .thenThrow(new RuntimeException("Error en la base de datos"));

                // Act
                ResponseEntity<String> response = recetaController.crearReceta(receta, null);

                // Assert
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                assertEquals("Error al crear la receta.", response.getBody());
                verify(recetaRepository, times(1)).save(any(Receta.class));
        }

        @Test
        public void testAgregarMedia_RecetaNoEncontrada() {
                // Arrange
                Long id = 1L;
                when(recetaRepository.findById(id)).thenReturn(Optional.empty());

                // Act
                ResponseEntity<String> response = recetaController.agregarMedia(id, null, null);

                // Assert
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                assertEquals("Receta no encontrada.", response.getBody());
                verify(recetaRepository, never()).save(any());
        }

        @Test
        public void testAgregarMedia_ConFotosYVideos() throws Exception {
                // Arrange
                Long id = 1L;
                Receta receta = new Receta();

                MockMultipartFile foto1 = new MockMultipartFile("fotos", "foto1.jpg", "image/jpeg",
                                "foto1 data".getBytes());
                MockMultipartFile video1 = new MockMultipartFile("videos", "video1.mp4", "video/mp4",
                                "video1 data".getBytes());

                when(recetaRepository.findById(id)).thenReturn(Optional.of(receta));
                when(recetaRepository.save(any(Receta.class))).thenReturn(receta);

                // Act
                ResponseEntity<String> response = recetaController.agregarMedia(id,
                                new MultipartFile[] { foto1 }, new MultipartFile[] { video1 });

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Fotos y/o videos agregados correctamente a la receta.", response.getBody());
                verify(recetaRepository, times(1)).save(any(Receta.class));
        }

        @Test
        public void testAgregarMedia_Excepcion() throws Exception {
                // Arrange
                Long id = 1L;
                Receta receta = new Receta();
                MockMultipartFile foto = new MockMultipartFile("fotos", "foto.jpg", "image/jpeg",
                                "data".getBytes());

                when(recetaRepository.findById(id)).thenReturn(Optional.of(receta));
                when(recetaRepository.save(any(Receta.class)))
                                .thenThrow(new RuntimeException("Error en la base de datos"));

                // Act
                ResponseEntity<String> response = recetaController.agregarMedia(id,
                                new MultipartFile[] { foto }, null);

                // Assert
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                assertEquals("Error al guardar archivos.", response.getBody());
                verify(recetaRepository, times(1)).save(any(Receta.class));
        }
}
