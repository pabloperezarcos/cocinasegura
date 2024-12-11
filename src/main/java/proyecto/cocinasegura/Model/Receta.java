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

    @Column(name = "imagen_url", length = 2083) // Longitud máxima de una URL
    private String imagenURL;

    @Column(name = "video_url", length = 2083) // Longitud máxima de una URL
    private String videoURL;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    // Lista de fotos asociadas
    @ElementCollection
    @CollectionTable(name = "receta_fotos", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "foto_url", length = 2083) // Almacena URLs de fotos
    private List<String> fotos = new ArrayList<>();

    // Lista de videos asociados
    @ElementCollection
    @CollectionTable(name = "receta_videos", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "video_url", length = 2083) // Almacena URLs de videos
    private List<String> videos = new ArrayList<>();

    // Constructor vacío
    public Receta() {
    }

    // Constructor con parámetros
    public Receta(String titulo, String tipoDeCocina, String ingredientes, String paisDeOrigen,
            String dificultad, String instrucciones, String tiempoDeCoccion, String imagenURL, String videoURL,
            String descripcion) {
        this.titulo = titulo;
        this.tipoDeCocina = tipoDeCocina;
        this.ingredientes = ingredientes;
        this.paisDeOrigen = paisDeOrigen;
        this.dificultad = dificultad;
        this.instrucciones = instrucciones;
        this.tiempoDeCoccion = tiempoDeCoccion;
        this.imagenURL = imagenURL;
        this.descripcion = descripcion;
        this.videoURL = videoURL;
    }

    // Getters y Setters para todos los campos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipoDeCocina() {
        return tipoDeCocina;
    }

    public void setTipoDeCocina(String tipoDeCocina) {
        this.tipoDeCocina = tipoDeCocina;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPaisDeOrigen() {
        return paisDeOrigen;
    }

    public void setPaisDeOrigen(String paisDeOrigen) {
        this.paisDeOrigen = paisDeOrigen;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getTiempoDeCoccion() {
        return tiempoDeCoccion;
    }

    public void setTiempoDeCoccion(String tiempoDeCoccion) {
        this.tiempoDeCoccion = tiempoDeCoccion;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

}
