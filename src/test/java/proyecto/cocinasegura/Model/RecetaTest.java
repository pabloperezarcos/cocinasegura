package proyecto.cocinasegura.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RecetaTest {

    @Test
    void testConstructorWithParameters() {
        String titulo = "Tarta de Manzana";
        String tipoDeCocina = "Postre";
        String ingredientes = "Manzanas, harina, azúcar";
        String paisDeOrigen = "España";
        String dificultad = "Fácil";
        String instrucciones = "Mezclar los ingredientes y hornear.";
        String tiempoDeCoccion = "30 minutos";
        String imagenURL = "http://example.com/tarta.jpg";
        String videoURL = "http://example.com/tarta.mp4";
        String descripcion = "Deliciosa tarta de manzana.";

        Receta receta = new Receta(titulo, tipoDeCocina, ingredientes, paisDeOrigen, dificultad, instrucciones,
                tiempoDeCoccion, imagenURL, videoURL, descripcion);

        assertEquals(titulo, receta.getTitulo());
        assertEquals(tipoDeCocina, receta.getTipoDeCocina());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(paisDeOrigen, receta.getPaisDeOrigen());
        assertEquals(dificultad, receta.getDificultad());
        assertEquals(instrucciones, receta.getInstrucciones());
        assertEquals(tiempoDeCoccion, receta.getTiempoDeCoccion());
        assertEquals(imagenURL, receta.getImagenURL());
        assertEquals(videoURL, receta.getVideoURL());
        assertEquals(descripcion, receta.getDescripcion());
    }

    @Test
    void testSettersAndGetters() {
        Receta receta = new Receta();
        String titulo = "Tarta de Manzana";
        String tipoDeCocina = "Postre";

        receta.setTitulo(titulo);
        receta.setTipoDeCocina(tipoDeCocina);

        assertEquals(titulo, receta.getTitulo());
        assertEquals(tipoDeCocina, receta.getTipoDeCocina());
    }

    @Test
    void testFotosCollection() {
        Receta receta = new Receta();
        List<String> fotos = new ArrayList<>();
        fotos.add("http://example.com/foto1.jpg");
        fotos.add("http://example.com/foto2.jpg");

        receta.setFotos(fotos);
        List<String> actualFotos = receta.getFotos();

        assertEquals(2, actualFotos.size());
        assertTrue(actualFotos.contains("http://example.com/foto1.jpg"));
        assertTrue(actualFotos.contains("http://example.com/foto2.jpg"));
    }

    @Test
    void testVideosCollection() {
        Receta receta = new Receta();
        List<String> videos = new ArrayList<>();
        videos.add("http://example.com/video1.mp4");
        videos.add("http://example.com/video2.mp4");

        receta.setVideos(videos);
        List<String> actualVideos = receta.getVideos();

        assertEquals(2, actualVideos.size());
        assertTrue(actualVideos.contains("http://example.com/video1.mp4"));
        assertTrue(actualVideos.contains("http://example.com/video2.mp4"));
    }
}
