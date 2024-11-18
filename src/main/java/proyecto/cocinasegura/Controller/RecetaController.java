package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.http.HttpHeaders;

import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Repository.RecetaRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    @Autowired
    private RecetaRepository recetaRepository;

    private final String RUTA_IMAGENES = "src/main/resources/static/images/";
    private final String RUTA_VIDEOS = "src/main/resources/static/videos/";

    // Endpoint para guardar recetas
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearReceta(
            @ModelAttribute Receta receta,
            @RequestParam(required = false) MultipartFile imagen) {

        // Validar campos obligatorios
        if (receta.getTitulo() == null || receta.getTitulo().isBlank()) {
            return ResponseEntity.badRequest().body("El título de la receta es obligatorio.");
        }
        if (receta.getTiempoDeCoccion() == null || receta.getTiempoDeCoccion().isBlank()) {
            return ResponseEntity.badRequest().body("El tiempo de cocción es obligatorio.");
        }

        try {
            // Guardar imagen si se proporciona
            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = guardarArchivo(imagen, RUTA_IMAGENES);
                receta.setImagenURL(rutaImagen);
            }

            // Guardar receta en la base de datos
            Receta recetaGuardada = recetaRepository.save(receta);

            // Redirigir al usuario a la página de detalles de la receta
            return ResponseEntity.status(HttpStatus.FOUND) // HTTP 302: Redirección temporal
                    .header("Location", "/recetas?id=" + recetaGuardada.getId())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar la receta o los archivos.");
        }
    }

    // Endpoint para agregar fotos y videos a recetas
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{id}/media")
    public ResponseEntity<String> agregarMedia(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile[] fotos,
            @RequestParam(required = false) MultipartFile[] videos) {

        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            try {
                // Guardar fotos
                if (fotos != null && fotos.length > 0) {
                    for (MultipartFile foto : fotos) {
                        String rutaFoto = guardarArchivo(foto, RUTA_IMAGENES);
                        receta.getFotos().add(rutaFoto);
                    }
                }

                // Guardar videos
                if (videos != null && videos.length > 0) {
                    for (MultipartFile video : videos) {
                        String rutaVideo = guardarArchivo(video, RUTA_VIDEOS);
                        receta.getVideos().add(rutaVideo);
                    }
                }

                recetaRepository.save(receta);
                return ResponseEntity.ok("Fotos y/o videos agregados correctamente a la receta.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al guardar archivos.");
            }
        }

        return ResponseEntity.badRequest().body("Receta no encontrada.");
    }

    // Método para guardar archivos en el sistema
    private String guardarArchivo(MultipartFile archivo, String subDirectorio) throws IOException {
        // Verificar que el archivo tiene una extensión válida
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo != null && !nombreArchivo.matches(".*\\.(jpg|jpeg|png|mp4|webm)$")) {
            throw new IOException("Formato de archivo no permitido: " + nombreArchivo);
        }

        // Ruta completa del subdirectorio
        Path directorioPath = Paths.get(subDirectorio);
        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath); // Crear directorio si no existe
        }

        Path archivoPath = directorioPath.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), archivoPath);

        // Retornar la ruta relativa accesible desde el frontend
        return "/images/" + nombreArchivo;
    }

    @PostMapping("/{id}/video")
    public ResponseEntity<String> agregarVideo(@PathVariable Long id, @RequestParam String videoURL) {
        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            // Validar que la URL sea de YouTube
            if (!videoURL.contains("youtube.com/watch?v=")) {
                return ResponseEntity.badRequest().body("La URL proporcionada no es válida.");
            }

            // Agregar la URL del video
            receta.setVideoURL(videoURL);
            recetaRepository.save(receta);

            return ResponseEntity.ok("Video agregado correctamente.");
        }

        return ResponseEntity.status(404).body("Receta no encontrada.");
    }

}
