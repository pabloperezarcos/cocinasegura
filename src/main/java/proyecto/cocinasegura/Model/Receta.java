package proyecto.cocinasegura.Model;

import jakarta.persistence.*;

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

    @Lob
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

    // Constructor vacío
    public Receta() {
    }

    // Constructor
    public Receta(String titulo, String tipoDeCocina, String ingredientes, String paisDeOrigen,
            String dificultad, String instrucciones, String tiempoDeCoccion, String imagenURL) {
        this.titulo = titulo;
        this.tipoDeCocina = tipoDeCocina;
        this.ingredientes = ingredientes;
        this.paisDeOrigen = paisDeOrigen;
        this.dificultad = dificultad;
        this.instrucciones = instrucciones;
        this.tiempoDeCoccion = tiempoDeCoccion;
        this.imagenURL = imagenURL;
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

}
