package proyecto.cocinasegura.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import proyecto.cocinasegura.Model.Receta;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Sql({ "/schema.sql", "/data.sql" })
public class RecetaRepositoryTest {

    @Autowired
    private RecetaRepository recetaRepository;

    private Receta receta;

    @BeforeEach
    void setUp() {
        // Crear una receta para los tests
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

        // Guardar la receta en la base de datos
        recetaRepository.save(receta);

        // Verificar que la receta se guardó correctamente
        assertNotNull(receta.getId(), "La receta debe tener un ID asignado después de guardarla");
    }

    @Test
    void testFindByTituloContainingIgnoreCase() {
        // Act
        List<Receta> recetas = recetaRepository.findByTituloContainingIgnoreCase("tarta");

        // Assert
        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("Tarta de Manzana", recetas.get(0).getTitulo(), "El título de la receta debe coincidir");
    }

    @Test
    void testFindByTipoDeCocinaIgnoreCase() {
        // Act
        List<Receta> recetas = recetaRepository.findByTipoDeCocinaIgnoreCase("postre");

        // Assert
        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("Postre", recetas.get(0).getTipoDeCocina(), "El tipo de cocina debe coincidir");
    }

    @Test
    void testFindByIngredientesContainingIgnoreCase() {
        // Act
        List<Receta> recetas = recetaRepository.findByIngredientesContainingIgnoreCase("manzana");

        // Assert
        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertTrue(recetas.get(0).getIngredientes().toLowerCase().contains("manzana"),
                "Los ingredientes deben contener 'manzana'");
    }

    @Test
    void testFindByPaisDeOrigenIgnoreCase() {
        // Act
        List<Receta> recetas = recetaRepository.findByPaisDeOrigenIgnoreCase("españa");

        // Assert
        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("España", recetas.get(0).getPaisDeOrigen(), "El país de origen debe coincidir");
    }

    @Test
    void testFindByDificultadIgnoreCase() {
        // Act
        List<Receta> recetas = recetaRepository.findByDificultadIgnoreCase("media");

        // Assert
        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("Media", recetas.get(0).getDificultad(), "La dificultad debe coincidir");
    }

    @Test
    void testFindByTituloContainingIgnoreCase_NoResults() {
        // Act
        List<Receta> recetas = recetaRepository.findByTituloContainingIgnoreCase("tarta inexistente");

        // Assert
        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertTrue(recetas.isEmpty(),
                "La lista de recetas debe estar vacía cuando no se encuentra ninguna coincidencia");
    }

}
