package proyecto.cocinasegura.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import proyecto.cocinasegura.Model.Receta;
import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long>, JpaSpecificationExecutor<Receta> {

    // Métodos de consultas
    List<Receta> findByTituloContainingIgnoreCase(String titulo);

    List<Receta> findByTipoDeCocina(String tipoDeCocina);

    List<Receta> findByIngredientesContainingIgnoreCase(String ingredientes);

    List<Receta> findByPaisDeOrigen(String paisDeOrigen);

    List<Receta> findByDificultad(String dificultad);
}
