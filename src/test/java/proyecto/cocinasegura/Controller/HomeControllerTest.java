package proyecto.cocinasegura.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.security.test.context.support.WithMockUser;

import proyecto.cocinasegura.Model.Comentario;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.ComentarioRepository;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HomeControllerTest {

        @Mock
        private RecetaRepository recetaRepository;

        @Mock
        private ComentarioRepository comentarioRepository;

        @Mock
        private Model model;

        @InjectMocks
        private HomeController homeController;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        // Test para el método index
        @Test
        public void testIndex() {
                // Arrange
                List<Receta> recetas = Arrays.asList(new Receta(), new Receta());
                when(recetaRepository.findAll()).thenReturn(recetas);

                // Act
                String viewName = homeController.index(model);

                // Assert
                assertEquals("index", viewName);
                verify(model).addAttribute("recetas", recetas);
        }

        // Test para el método recetas (búsqueda)
        @Test
        public void testBuscarRecetas_TituloFiltro() {
                // Arrange
                List<Receta> recetasFiltradas = Arrays.asList(new Receta());
                when(recetaRepository.findAll()).thenReturn(recetasFiltradas);
                when(recetaRepository.findByTituloContainingIgnoreCase("Pasta")).thenReturn(recetasFiltradas);

                // Act
                String viewName = homeController.recetas(model, "Pasta", null, null, null, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetasFiltradas);
        }

        @Test
        public void testBuscarRecetas_SinFiltros() {
                // Arrange
                List<Receta> todasLasRecetas = Arrays.asList(new Receta(), new Receta());
                when(recetaRepository.findAll()).thenReturn(todasLasRecetas);

                // Act
                String viewName = homeController.recetas(model, null, null, null, null, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", todasLasRecetas);
        }

        // Test para el método mostrarRecetas (detalle de receta)
        @Test
        public void testMostrarRecetas_ConIdExistente() {
                // Arrange
                Long recetaId = 1L;
                Receta receta = new Receta();
                receta.setId(recetaId);
                List<Comentario> comentarios = Arrays.asList(new Comentario());

                when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
                when(recetaRepository.findById(recetaId)).thenReturn(Optional.of(receta));
                when(comentarioRepository.findByRecetaId(recetaId)).thenReturn(comentarios);

                // Act
                String viewName = homeController.mostrarRecetas(model, recetaId);

                // Assert
                assertEquals("detalle-receta", viewName);
                verify(model).addAttribute("recetas", Arrays.asList(receta));
                verify(model).addAttribute("recetaSeleccionada", receta);
                verify(model).addAttribute("comentarios", comentarios);
        }

        @Test
        public void testMostrarRecetas_ConIdNoExistente() {
                // Arrange
                Long recetaId = 1L;
                when(recetaRepository.findAll()).thenReturn(Arrays.asList());
                when(recetaRepository.findById(recetaId)).thenReturn(Optional.empty());

                // Act
                String viewName = homeController.mostrarRecetas(model, recetaId);

                // Assert
                assertEquals("detalle-receta", viewName);
                verify(model).addAttribute("error", "No se encontró la receta con ID: " + recetaId);
        }

        // Test para el método mostrarFormularioNuevaReceta
        @Test
        @WithMockUser(roles = { "USER" })
        public void testMostrarFormularioNuevaReceta() {
                // Act
                String viewName = homeController.mostrarFormularioNuevaReceta(model);

                // Assert
                assertEquals("nueva-receta", viewName);
                verify(model).addAttribute(eq("receta"), any(Receta.class));
        }

        @Test
        public void testBuscarRecetas_FiltroTitulo() {
                // Arrange
                String titulo = "Pasta";
                Receta receta = new Receta();
                receta.setTitulo("Pasta Italiana");
                List<Receta> recetas = Arrays.asList(receta);

                when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
                when(recetaRepository.findByTituloContainingIgnoreCase(titulo)).thenReturn(recetas);

                // Act
                String viewName = homeController.recetas(model, titulo, null, null, null, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetas);
        }

        @Test
        public void testBuscarRecetas_FiltroTipoDeCocina() {
                // Arrange
                String tipoDeCocina = "Italiana";
                Receta receta = new Receta();
                receta.setTipoDeCocina(tipoDeCocina);
                List<Receta> recetas = Arrays.asList(receta);

                when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
                when(recetaRepository.findByTipoDeCocinaIgnoreCase(tipoDeCocina)).thenReturn(recetas);

                // Act
                String viewName = homeController.recetas(model, null, tipoDeCocina, null, null, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetas);
        }

        @Test
        public void testBuscarRecetas_FiltroIngredientes() {
                // Arrange
                String ingredientes = "Tomate";
                Receta receta = new Receta();
                receta.setIngredientes("Tomate, Queso");
                List<Receta> recetas = Arrays.asList(receta);

                when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
                when(recetaRepository.findByIngredientesContainingIgnoreCase(ingredientes)).thenReturn(recetas);

                // Act
                String viewName = homeController.recetas(model, null, null, ingredientes, null, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetas);
        }

        @Test
        public void testBuscarRecetas_FiltroPaisDeOrigen() {
                // Arrange
                String paisDeOrigen = "Italia";
                Receta receta = new Receta();
                receta.setPaisDeOrigen(paisDeOrigen);
                List<Receta> recetas = Arrays.asList(receta);

                when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
                when(recetaRepository.findByPaisDeOrigenIgnoreCase(paisDeOrigen)).thenReturn(recetas);

                // Act
                String viewName = homeController.recetas(model, null, null, null, paisDeOrigen, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetas);
        }

        @Test
        public void testBuscarRecetas_FiltroDificultad() {
                // Arrange
                String dificultad = "Facil";
                Receta receta = new Receta();
                receta.setDificultad(dificultad);
                List<Receta> recetas = Arrays.asList(receta);

                when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
                when(recetaRepository.findByDificultadIgnoreCase(dificultad)).thenReturn(recetas);

                // Act
                String viewName = homeController.recetas(model, null, null, null, null, dificultad);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetas);
        }

        @Test
        public void testBuscarRecetas_ParametrosNulos() {
                // Arrange
                List<Receta> recetas = Arrays.asList(new Receta(), new Receta());
                when(recetaRepository.findAll()).thenReturn(recetas);

                // Act
                String viewName = homeController.recetas(model, null, null, null, null, null);

                // Assert
                assertEquals("resultados-buscar", viewName);
                verify(model).addAttribute("recetas", recetas);
                verifyNoMoreInteractions(recetaRepository);
        }
}