package proyecto.cocinasegura.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;
import proyecto.cocinasegura.Model.Receta;

@Controller
public class HomeController {

    // Mapeo para la página de inicio
    @GetMapping("/")
    public String index(Model model) {
        // Atributos al modelo para pasarlos a la vista
        model.addAttribute("recetas", obtenerRecetas());
        return "index"; // Nombre HTML a renderizar
    }

    // Método para mostrar la lista de recetas
    @GetMapping("/recetas")
    public String recetas(Model model) {
        List<Receta> listaRecetas = obtenerRecetas();
        model.addAttribute("recetas", listaRecetas);
        return "recetas";
    }

    // Método para obtener las recetas
    private List<Receta> obtenerRecetas() {
        List<Receta> recetas = new ArrayList<>();
        // Agregamos recetas
        recetas.add(new Receta("Paella", "Deliciosa paella española", "/images/paella.jpg"));
        recetas.add(new Receta("Sushi", "Sushi tradicional japonés", "/images/sushi.jpg"));
        return recetas;
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
