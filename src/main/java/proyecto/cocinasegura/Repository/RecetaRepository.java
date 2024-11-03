package proyecto.cocinasegura.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import proyecto.cocinasegura.Model.Receta;
import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long>, JpaSpecificationExecutor<Receta> {

    // Métodos de consultas que permite buscar recetas cuyos atributos contiene una cadena específica, sin importar si está en mayúsculas o minúsculas.
    List<Receta> findByTituloContainingIgnoreCase(String titulo);

    List<Receta> findByTipoDeCocinaIgnoreCase(String tipoDeCocina);

    List<Receta> findByIngredientesContainingIgnoreCase(String ingredientes);

    List<Receta> findByPaisDeOrigenIgnoreCase(String paisDeOrigen);

    List<Receta> findByDificultadIgnoreCase(String dificultad);
}
