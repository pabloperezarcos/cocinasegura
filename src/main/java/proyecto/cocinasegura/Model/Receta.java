package proyecto.cocinasegura.Model;

public class Receta {

    private String titulo;
    private String descripcion;
    private String imagenURL;

    // Constructor
    public Receta(String titulo, String descripcion, String imagenURL) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenURL = imagenURL;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }
}
