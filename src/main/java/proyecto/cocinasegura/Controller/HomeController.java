package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RecetaRepository recetaRepository;

    // Mapeo para la página de inicio
    @GetMapping("/")
    public String index(Model model) {
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);
        return "index";
    }

    // Método para mostrar la lista de recetas con funcionalidad de búsqueda
    @GetMapping("/recetas")
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
        return "recetas";
    }

    // Mapeo para manejar la búsqueda de recetas
    @GetMapping("/buscar")
    public String buscar(Model model,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "tipoDeCocina", required = false) String tipoDeCocina,
            @RequestParam(value = "ingredientes", required = false) String ingredientes,
            @RequestParam(value = "paisDeOrigen", required = false) String paisDeOrigen,
            @RequestParam(value = "dificultad", required = false) String dificultad) {
        // Redirige a /recetas con los parámetros de búsqueda
        return recetas(model, titulo, tipoDeCocina, ingredientes, paisDeOrigen, dificultad);
    }

    // Mapeo para ingresar al login
    @GetMapping("/login")
    public String login() {
        return "login"; // Nombre de la plantilla HTML para login
    }
}
