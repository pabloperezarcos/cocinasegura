package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    @Autowired
    private RecetaRepository recetaRepository;

    //Endpoint para guardar recetas
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearReceta(@ModelAttribute Receta receta) {
        recetaRepository.save(receta);
        return ResponseEntity.ok("Receta creada exitosamente.");
    }

    // Endpoint para agregar fotos y videos a recetas
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{id}/media")
    public ResponseEntity<String> agregarMedia(
            @PathVariable Long id,
            @RequestParam(required = false) List<String> fotos,
            @RequestParam(required = false) List<String> videos) {

        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            // Agregar fotos
            if (fotos != null && !fotos.isEmpty()) {
                receta.getFotos().addAll(fotos);
            }

            // Agregar videos
            if (videos != null && !videos.isEmpty()) {
                receta.getVideos().addAll(videos);
            }

            recetaRepository.save(receta);
            return ResponseEntity.ok("Fotos y/o videos agregados correctamente a la receta.");
        }

        return ResponseEntity.badRequest().body("Receta no encontrada.");
    }
}
