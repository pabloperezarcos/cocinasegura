package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.cocinasegura.Model.Comentario;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;
import proyecto.cocinasegura.Repository.ComentarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    // Mapeo para la página de inicio
    @GetMapping("/")
    public String index(Model model) {
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);
        return "index";
    }

    // Método para mostrar la lista de recetas con funcionalidad de búsqueda
    @GetMapping("/buscar")
    public String recetas(Model model,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "tipoDeCocina", required = false) String tipoDeCocina,
            @RequestParam(value = "ingredientes", required = false) String ingredientes,
            @RequestParam(value = "paisDeOrigen", required = false) String paisDeOrigen,
            @RequestParam(value = "dificultad", required = false) String dificultad) {

        // Obtiene todas las recetas
        List<Receta> listaRecetas = new ArrayList<>(recetaRepository.findAll());

        // Aplicar filtros según los parámetros de búsqueda
        if (titulo != null && !titulo.isEmpty()) {
            listaRecetas.retainAll(recetaRepository.findByTituloContainingIgnoreCase(titulo));
        }
        if (tipoDeCocina != null && !tipoDeCocina.isEmpty()) {
            listaRecetas.retainAll(recetaRepository.findByTipoDeCocinaIgnoreCase(tipoDeCocina));
        }
        if (ingredientes != null && !ingredientes.isEmpty()) {
            listaRecetas.retainAll(recetaRepository.findByIngredientesContainingIgnoreCase(ingredientes));
        }
        if (paisDeOrigen != null && !paisDeOrigen.isEmpty()) {
            listaRecetas.retainAll(recetaRepository.findByPaisDeOrigenIgnoreCase(paisDeOrigen));
        }
        if (dificultad != null && !dificultad.isEmpty()) {
            listaRecetas.retainAll(recetaRepository.findByDificultadIgnoreCase(dificultad));
        }

        model.addAttribute("recetas", listaRecetas);
        return "buscar";
    }

    // Mapeo para la página de recetas
    @GetMapping("/recetas")
    public String mostrarRecetas(Model model, @RequestParam(value = "id", required = false) Long id) {
        // Obtener todas las recetas y añadirlas al modelo
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);

        if (id != null) {
            // Buscar la receta seleccionada
            Optional<Receta> recetaSeleccionada = recetaRepository.findById(id);

            if (recetaSeleccionada.isPresent()) {
                model.addAttribute("recetaSeleccionada", recetaSeleccionada.get());

                // Obtener comentarios relacionados con la receta seleccionada
                List<Comentario> comentarios = comentarioRepository.findByRecetaId(id);
                model.addAttribute("comentarios", comentarios);
            } else {
                // Si no se encuentra la receta, añade un mensaje de error
                model.addAttribute("error", "No se encontró la receta con ID: " + id);
            }
        }

        return "recetas"; // Retornar la vista correspondiente
    }

    // Mapeo para recetas nuevas
    @GetMapping("/recetas/nueva")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String mostrarFormularioNuevaReceta(Model model) {
        model.addAttribute("receta", new Receta());
        return "nueva-receta";
    }

}
