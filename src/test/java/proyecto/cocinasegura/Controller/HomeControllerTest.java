package proyecto.cocinasegura.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import proyecto.cocinasegura.Model.Comentario;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;
import proyecto.cocinasegura.Repository.ComentarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HomeControllerTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testIndex() throws Exception {
        // Arrange
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta("Tarta de Manzana", "Postre", "Manzana, harina", "España", "Fácil", "Instrucciones",
                "30 min", null, null, "Deliciosa receta"));
        when(recetaRepository.findAll()).thenReturn(recetas);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("recetas", recetas));
    }

    @Test
    void testBuscarRecetas() throws Exception {
        // Arrange
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta("Tarta de Manzana", "Postre", "Manzana, harina", "España", "Fácil", "Instrucciones",
                "30 min", null, null, "Deliciosa receta"));
        when(recetaRepository.findAll()).thenReturn(recetas);
        when(recetaRepository.findByTituloContainingIgnoreCase("Tarta")).thenReturn(recetas);

        // Act & Assert
        mockMvc.perform(get("/buscar").param("titulo", "Tarta"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attribute("recetas", recetas));
    }

    @Test
    void testMostrarRecetas() throws Exception {
        // Arrange
        List<Receta> recetas = new ArrayList<>();
        Receta receta = new Receta("Tarta de Manzana", "Postre", "Manzana, harina", "España", "Fácil", "Instrucciones",
                "30 min", null, null, "Deliciosa receta");
        recetas.add(receta);
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(new Comentario());

        when(recetaRepository.findAll()).thenReturn(recetas);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));
        when(comentarioRepository.findByRecetaId(1L)).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/recetas").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recetas"))
                .andExpect(model().attribute("recetas", recetas))
                .andExpect(model().attribute("recetaSeleccionada", receta))
                .andExpect(model().attribute("comentarios", comentarios));
    }

    @Test
    void testMostrarFormularioNuevaReceta() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/recetas/nueva"))
                .andExpect(status().isOk())
                .andExpect(view().name("nueva-receta"))
                .andExpect(model().attributeExists("receta"));
    }
}
