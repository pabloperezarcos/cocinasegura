package proyecto.cocinasegura.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "tipo_de_cocina", nullable = false, length = 50)
    private String tipoDeCocina;

    @Column(name = "ingredientes", nullable = false)
    private String ingredientes;

    @Column(name = "pais_de_origen", nullable = false, length = 50)
    private String paisDeOrigen;

    @Column(name = "dificultad", nullable = false, length = 20)
    private String dificultad;

    @Lob
    @Column(name = "instrucciones", nullable = false)
    private String instrucciones;

    @Column(name = "tiempo_de_coccion", nullable = false, length = 20)
    private String tiempoDeCoccion;

    @Column(name = "imagen_url", length = 255)
    private String imagenURL;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    // Constructor vacío
    public Receta() {
    }

    // Campo adicional para las fotos
    @ElementCollection
    @CollectionTable(name = "receta_fotos", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "foto_url")
    private List<String> fotos = new ArrayList<>();

    // Campo adicional para los videos
    @ElementCollection
    @CollectionTable(name = "receta_videos", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "video_url")
    private List<String> videos = new ArrayList<>();

    // Constructor
    public Receta(String titulo, String tipoDeCocina, String ingredientes, String paisDeOrigen,
            String dificultad, String instrucciones, String tiempoDeCoccion, String imagenURL, String descripcion) {
        this.titulo = titulo;
        this.tipoDeCocina = tipoDeCocina;
        this.ingredientes = ingredientes;
        this.paisDeOrigen = paisDeOrigen;
        this.dificultad = dificultad;
        this.instrucciones = instrucciones;
        this.tiempoDeCoccion = tiempoDeCoccion;
        this.imagenURL = imagenURL;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipoDeCocina() {
        return tipoDeCocina;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public String getPaisDeOrigen() {
        return paisDeOrigen;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public String getTiempoDeCoccion() {
        return tiempoDeCoccion;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public List<String> getVideos() {
        return videos;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTipoDeCocina(String tipoDeCocina) {
        this.tipoDeCocina = tipoDeCocina;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setPaisDeOrigen(String paisDeOrigen) {
        this.paisDeOrigen = paisDeOrigen;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void setTiempoDeCoccion(String tiempoDeCoccion) {
        this.tiempoDeCoccion = tiempoDeCoccion;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

}
