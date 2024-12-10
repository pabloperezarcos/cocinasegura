package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // Método para crear receta
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearReceta(
            @ModelAttribute Receta receta,
            @RequestParam(required = false) MultipartFile imagen) {
        try {
            // Validar campos obligatorios
            if (receta.getTitulo() == null || receta.getTitulo().isBlank()) {
                return ResponseEntity.badRequest().body("El título de la receta es obligatorio.");
            }
            if (receta.getTiempoDeCoccion() == null || receta.getTiempoDeCoccion().isBlank()) {
                return ResponseEntity.badRequest().body("El tiempo de cocción es obligatorio.");
            }

            // Guardar imagen si se proporciona
            if (imagen != null && !imagen.isEmpty()) {
                String rutaImagen = guardarArchivo(imagen, RUTA_IMAGENES);
                receta.setImagenURL(rutaImagen);
            }

            // Guardar receta en la base de datos
            Receta recetaGuardada = recetaRepository.save(receta);

            // Verificar si el ID fue asignado correctamente
            if (recetaGuardada.getId() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al guardar la receta. No se generó el ID.");
            }

            // Redirigir al usuario a la página de detalles de la receta
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/recetas?id=" + recetaGuardada.getId())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al crear la receta.");
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
        if (recetaOpt.isEmpty()) {
            System.out.println("Receta no encontrada para ID: " + id);
            return ResponseEntity.badRequest().body("Receta no encontrada.");
        }

        Receta receta = recetaOpt.get();

        try {
            if (fotos != null) {
                for (MultipartFile foto : fotos) {
                    if (!foto.isEmpty()) {
                        String rutaFoto = guardarArchivo(foto, RUTA_IMAGENES);
                        receta.getFotos().add(rutaFoto);
                    }
                }
            }
            if (videos != null) {
                for (MultipartFile video : videos) {
                    if (!video.isEmpty()) {
                        String rutaVideo = guardarArchivo(video, RUTA_VIDEOS);
                        receta.getVideos().add(rutaVideo);
                    }
                }
            }
            recetaRepository.save(receta);
            return ResponseEntity.ok("Fotos y/o videos agregados correctamente a la receta.");
        } catch (Exception e) {
            System.out.println("Error al guardar media: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar archivos.");
        }
    }

    // Método para guardar archivos en el sistema
    public String guardarArchivo(MultipartFile archivo, String subDirectorio) throws IOException {
        // Verifica que el archivo tenga una extensión válida
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.matches(".*\\.(jpg|jpeg|png|mp4|webm)$")) {
            throw new IOException("Formato de archivo no permitido: " + nombreArchivo);
        }

        // Asegúrate de que el directorio existe
        Path directorioPath = Paths.get(subDirectorio);
        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath); // Crear directorio si no existe
        }

        // Guardar el archivo en la ruta especificada
        Path archivoPath = directorioPath.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), archivoPath);

        // Devuelve la ruta relativa para usarla en el frontend
        return "/static/images/" + nombreArchivo;
    }

    @PostMapping("/{id}/video")
    public ResponseEntity<?> agregarVideo(@PathVariable Long id, @RequestParam String videoURL,
            RedirectAttributes redirectAttributes) {
        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            // Validar que la URL sea de YouTube
            if (!videoURL.contains("youtube.com/watch?v=") && !videoURL.contains("youtu.be/")) {
                redirectAttributes.addFlashAttribute("error", "La URL proporcionada no es válida.");
                return ResponseEntity.status(302)
                        .header("Location", "/recetas?id=" + id)
                        .build();
            }

            // Agregar la URL del video
            receta.setVideoURL(videoURL);
            recetaRepository.save(receta);

            // Redirigir a la misma página con un mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Video agregado correctamente.");
            return ResponseEntity.status(302)
                    .header("Location", "/recetas?id=" + id)
                    .build();
        }

        // Si la receta no se encuentra
        redirectAttributes.addFlashAttribute("error", "Receta no encontrada.");
        return ResponseEntity.status(302)
                .header("Location", "/recetas")
                .build();
    }

}
