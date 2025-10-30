public class Pelicula {
    private int id;
    private String titulo;
    private int ano;
    private String director;
    private String descripcion;
    private String genero;
    private String ImagenUrl;
    private int idUsuario;

    public Pelicula(int id, String titulo, int ano, String director, String descripcion, String genero, String imagenUrl, int idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.director = director;
        this.descripcion = descripcion;
        this.genero = genero;
        this.ImagenUrl = imagenUrl;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagenUrl() {
        return ImagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        ImagenUrl = imagenUrl;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + " (" + ano + ")" + System.lineSeparator() +
                "  - Director: " + director + System.lineSeparator() +
                "  - Género: " + genero + System.lineSeparator() +
                "  - ID Usuario: " + idUsuario + System.lineSeparator() +
                "  - Descripción: " + descripcion.substring(0, Math.min(descripcion.length(), 60)) + "...";
    }

    public Object[] toTableRow() {
        return new Object[] {
                this.titulo,
                this.ano,
                this.director,
                this.genero,
        };
    }

}
