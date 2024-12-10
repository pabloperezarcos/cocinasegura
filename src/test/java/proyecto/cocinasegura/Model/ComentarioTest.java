package proyecto.cocinasegura.Model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComentarioTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Comentario comentario = new Comentario();
        Long id = 1L;
        Receta receta = new Receta();
        Usuario usuario = new Usuario();
        String texto = "Delicioso plato!";
        int valoracion = 5;
        LocalDateTime fecha = LocalDateTime.now();

        // Act
        comentario.setId(id);
        comentario.setReceta(receta);
        comentario.setUsuario(usuario);
        comentario.setTexto(texto);
        comentario.setValoracion(valoracion);
        comentario.setFecha(fecha);

        // Assert
        assertEquals(id, comentario.getId());
        assertEquals(receta, comentario.getReceta());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(texto, comentario.getTexto());
        assertEquals(valoracion, comentario.getValoracion());
        assertEquals(fecha, comentario.getFecha());
    }
}
