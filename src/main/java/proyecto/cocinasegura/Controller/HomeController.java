package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    // Inyecta el repositorio de Receta
    private RecetaRepository recetaRepository;

    // Mapeo para la página de inicio
    @GetMapping("/")
    public String index(Model model) {
        // Atributos al modelo para pasarlos a la vista
        // Obtiene todas las recetas de la base de datos
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);
        return "index"; // Nombre HTML a renderizar
    }

    // Método para mostrar la lista de recetas
    @GetMapping("/recetas")
    public String recetas(Model model) {
        // Obtiene todas las recetas de la base de datos
        List<Receta> listaRecetas = recetaRepository.findAll();
        model.addAttribute("recetas", listaRecetas);
        return "recetas";
    }

    // Mapeo para manejar la búsqueda de recetas
    @GetMapping("/buscar")
    public String buscar() {
        return "buscar";
    }

    // Mapeo para ingresar al login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
