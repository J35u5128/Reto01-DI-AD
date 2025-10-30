import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSVDataManager {

    private static final String USUARIOS_FILE = "usuarios.csv";
    private static final String PELICULAS_FILE = "peliculas.csv";
    private static final String DELIMITER = ",";

    private final List<Usuario> usuarios;
    private final List<Pelicula> peliculas;

    public CSVDataManager() {
        this.usuarios = loadUsuarios();
        this.peliculas = loadPeliculas();
        System.out.printf("Cargados %d usuarios y %d películas.%n", usuarios.size(), peliculas.size());
    }

    private List<Usuario> loadUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (data.length == 3) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String correo = data[1].trim();
                        String contrasena = data[2].trim();
                        lista.add(new Usuario(id, correo, contrasena));
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en usuarios.csv: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo leer el archivo de usuarios (" + USUARIOS_FILE + "). Asegúrese de que existe.");
            e.printStackTrace();
        }
        return lista;
    }

    private List<Pelicula> loadPeliculas() {
        List<Pelicula> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PELICULAS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(DELIMITER, 8);
                if (data.length == 8) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String titulo = data[1].trim();
                        int anio = Integer.parseInt(data[2].trim());
                        String director = data[3].trim();
                        String sinopsis = data[4].trim();
                        String genero = data[5].trim();
                        String imagenUrl = data[6].trim();
                        int usuarioId = Integer.parseInt(data[7].trim());

                        lista.add(new Pelicula(id, titulo, anio, director, sinopsis, genero, imagenUrl, usuarioId));
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en peliculas.csv: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo leer el archivo de películas (" + PELICULAS_FILE + "). Asegúrese de que existe.");
            e.printStackTrace();
        }
        return lista;
    }

    public Optional<Usuario> authenticate(String correo, String contrasena) {
        return usuarios.stream().filter(u -> u.getEmail().equalsIgnoreCase(correo) && u.getPassword().equals(contrasena)).findFirst();
    }

    public List<Pelicula> getPeliculasByUserId(int userId) {
        return peliculas.stream().filter(p -> p.getIdUsuario() == userId).collect(Collectors.toList());
    }

    public int generateNextPeliculaId() {
        return peliculas.stream().mapToInt(Pelicula::getId).max().orElse(0) + 1;
    }

    public void addPelicula(Pelicula pelicula) {
        peliculas.add(pelicula);
        try (FileWriter fw = new FileWriter(PELICULAS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            String csvLine = String.join(DELIMITER,
                    String.valueOf(pelicula.getId()),
                    pelicula.getTitulo(),
                    String.valueOf(pelicula.getAno()),
                    pelicula.getDirector(),
                    pelicula.getDescripcion(),
                    pelicula.getGenero(),
                    pelicula.getImagenUrl(),
                    String.valueOf(pelicula.getIdUsuario())
            );
            out.println(csvLine);

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de películas: " + e.getMessage());
            e.printStackTrace();
            peliculas.remove(pelicula);
        }
    }

    public void deletePelicula(int peliculaId) {
        Optional<Pelicula> peliculaToRemove = peliculas.stream().filter(p -> p.getId() == peliculaId).findFirst();
        if (peliculaToRemove.isPresent()) {
            peliculas.remove(peliculaToRemove.get());
            rewritePeliculasCSV();
        } else {
            System.err.println("No se encontró la película con ID: " + peliculaId);
        }
    }

    private void rewritePeliculasCSV() {
        try (FileWriter fw = new FileWriter(PELICULAS_FILE, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (Pelicula pelicula : peliculas) {
                String csvLine = String.join(DELIMITER,
                        String.valueOf(pelicula.getId()),
                        pelicula.getTitulo(),
                        String.valueOf(pelicula.getAno()),
                        pelicula.getDirector(),
                        pelicula.getDescripcion(),
                        pelicula.getGenero(),
                        pelicula.getImagenUrl(),
                        String.valueOf(pelicula.getIdUsuario())
                );
                out.println(csvLine);
            }

        } catch (IOException e) {
            System.err.println("Error al reescribir el archivo de películas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
