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

        recetaRepository.save(receta);

        assertNotNull(receta.getId(), "La receta debe tener un ID asignado después de guardarla");
    }

    @Test
    void testFindByTituloContainingIgnoreCase() {

        List<Receta> recetas = recetaRepository.findByTituloContainingIgnoreCase("tarta");

        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("Tarta de Manzana", recetas.get(0).getTitulo(), "El título de la receta debe coincidir");
    }

    @Test
    void testFindByTipoDeCocinaIgnoreCase() {

        List<Receta> recetas = recetaRepository.findByTipoDeCocinaIgnoreCase("postre");

        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("Postre", recetas.get(0).getTipoDeCocina(), "El tipo de cocina debe coincidir");
    }

    @Test
    void testFindByIngredientesContainingIgnoreCase() {

        List<Receta> recetas = recetaRepository.findByIngredientesContainingIgnoreCase("manzana");

        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertTrue(recetas.get(0).getIngredientes().toLowerCase().contains("manzana"),
                "Los ingredientes deben contener 'manzana'");
    }

    @Test
    void testFindByPaisDeOrigenIgnoreCase() {

        List<Receta> recetas = recetaRepository.findByPaisDeOrigenIgnoreCase("españa");

        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("España", recetas.get(0).getPaisDeOrigen(), "El país de origen debe coincidir");
    }

    @Test
    void testFindByDificultadIgnoreCase() {

        List<Receta> recetas = recetaRepository.findByDificultadIgnoreCase("media");

        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertFalse(recetas.isEmpty(), "La lista de recetas no debe estar vacía");
        assertEquals("Media", recetas.get(0).getDificultad(), "La dificultad debe coincidir");
    }

    @Test
    void testFindByTituloContainingIgnoreCase_NoResults() {

        List<Receta> recetas = recetaRepository.findByTituloContainingIgnoreCase("tarta inexistente");

        assertNotNull(recetas, "La lista de recetas no debe ser nula");
        assertTrue(recetas.isEmpty(),
                "La lista de recetas debe estar vacía cuando no se encuentra ninguna coincidencia");
    }

}
