package proyecto.cocinasegura.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proyecto.cocinasegura.Model.Comentario;
import proyecto.cocinasegura.Model.Receta;
import proyecto.cocinasegura.Model.Usuario;
import proyecto.cocinasegura.Repository.ComentarioRepository;
import proyecto.cocinasegura.Repository.RecetaRepository;
import proyecto.cocinasegura.Repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener comentarios de una receta
    @GetMapping("/{recetaId}")
    public ResponseEntity<List<Comentario>> obtenerComentariosPorReceta(@PathVariable Long recetaId) {
        List<Comentario> comentarios = comentarioRepository.findByRecetaId(recetaId);
        return ResponseEntity.ok(comentarios);
    }

    // Agregar un nuevo comentario
    @PostMapping("/{recetaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> agregarComentario(@PathVariable Long recetaId,
            @RequestParam String texto,
            @RequestParam Long usuarioId,
            @RequestParam(required = false) Integer valoracion) {
        Receta receta = recetaRepository.findById(recetaId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (receta == null || usuario == null) {
            return ResponseEntity.badRequest().body("Receta o Usuario no encontrado.");
        }

        Comentario comentario = new Comentario();
        comentario.setReceta(receta);
        comentario.setUsuario(usuario);
        comentario.setTexto(texto);
        comentario.setFecha(LocalDateTime.now());

        if (valoracion != null) {
            comentario.setValoracion(valoracion);
        }

        comentarioRepository.save(comentario);

        // Redirección a la misma receta con un mensaje de éxito
        return ResponseEntity.status(302)
                .header("Location", "/recetas?id=" + recetaId + "&mensaje=Comentario agregado exitosamente")
                .build();
    }

}
