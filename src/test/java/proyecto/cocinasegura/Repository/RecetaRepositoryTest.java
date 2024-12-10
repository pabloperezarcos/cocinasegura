package proyecto.cocinasegura.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
import proyecto.cocinasegura.Model.Receta;

import java.util.List;

@DataJpaTest
public class RecetaRepositoryTest {

    @Autowired
    private RecetaRepository recetaRepository;

    private Receta receta;

    @BeforeEach
    void setUp() {
        // Crear una receta para los tests
        receta = new Receta();
        receta.setTitulo("Tarta de Manzana");
        receta.setTipoDeCocina("Postre");
        receta.setIngredientes("Manzana, harina, azúcar, mantequilla");
        receta.setPaisDeOrigen("España");
        receta.setDificultad("Media");

        // Guardar la receta en la base de datos
        recetaRepository.save(receta);
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
}
