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

    // Método para obtener las recetas
    private List<Receta> obtenerRecetas() {
        List<Receta> recipes = new ArrayList<>();
        // Agregamos recetas
        recipes.add(new Receta("Paella", "Deliciosa paella española", "/images/paella.jpg"));
        recipes.add(new Receta("Sushi", "Sushi tradicional japonés", "/images/sushi.jpg"));
        return recipes;
    }

    // Método para manejar la búsqueda de recetas
    @GetMapping("/buscar")
    public String buscar() {
        return "buscar";
    }

    // Método para ingresar al login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
