import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final CSVDataManager dataManager;
    private final Usuario loggedInUser;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private List<Pelicula> userPeliculas;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnLogout;
    private JLabel lblHeader;

    public MainFrame(CSVDataManager dataManager, Usuario loggedInUser) {
        this.dataManager = dataManager;
        this.loggedInUser = loggedInUser;

        setTitle("IMDb del chino - Películas de"+loggedInUser.getEmail());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centrar la ventana

        initComponents();
        loadMovies();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        lblHeader = new JLabel("Películas de " + loggedInUser.getEmail(), SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblHeader, BorderLayout.NORTH);

        String[] columnNames = {"Título", "Año", "Director", "Género"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        movieTable = new JTable(tableModel);
        movieTable.setFillsViewportHeight(true);
        movieTable.setRowHeight(30);
        movieTable.setFont(new Font("Arial", Font.PLAIN, 14));
        movieTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = movieTable.getSelectedRow();
                    if (selectedRow != -1) {
                        showMovieDetail(selectedRow);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(movieTable);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd = new JButton("Añadir");
        btnDelete = new JButton("Eliminar");
        btnLogout = new JButton("Cerrar Sesión");

        btnAdd.addActionListener(e -> addMovie());
        btnDelete.addActionListener(e -> deleteMovie());
        btnLogout.addActionListener(e -> logout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnLogout);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadMovies() {
        // Limpiar la tabla antes de cargar
        tableModel.setRowCount(0);

        userPeliculas = dataManager.getPeliculasByUserId(loggedInUser.getId());

        if (userPeliculas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes películas asignadas. Verifica el archivo peliculas.csv.", "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
        }

        for (Pelicula p : userPeliculas) {
            tableModel.addRow(p.toTableRow());
        }
    }

    private void showMovieDetail(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < userPeliculas.size()) {
            Pelicula selectedMovie = userPeliculas.get(selectedRow);
            DetallePeliculaDialog dialog = new DetallePeliculaDialog(this, selectedMovie);
            dialog.setVisible(true);
        }
    }

    private void addMovie() {
        AddPeliculaDialog dialog = new AddPeliculaDialog(this, dataManager, loggedInUser.getId());
        dialog.setVisible(true);
        if (dialog.isPeliculaAdded()) {
            loadMovies();
        }
    }

    private void deleteMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow != -1) {
            Pelicula selectedMovie = userPeliculas.get(selectedRow);
            int choice = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres eliminar la película '" + selectedMovie.getTitulo() + "'?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dataManager.deletePelicula(selectedMovie.getId());
                loadMovies();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una película para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void logout() {
        dispose();
        LoginFrame loginFrame = new LoginFrame(dataManager);
        loginFrame.setVisible(true);
    }
}