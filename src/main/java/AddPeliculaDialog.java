import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddPeliculaDialog extends JDialog {
    private JPanel contentPane;
    private JButton BtnGuardar;
    private JButton BtnCancelar;
    private JTextField Titulo;
    private JTextField Año;
    private JTextField Director;
    private JTextField Genero;
    private JTextField URLImagen;
    private JTextArea Descripcion;

    private CSVDataManager dataManager;
    private int idUsuario;
    private boolean peliculaAdded = false;

    public AddPeliculaDialog(Frame owner, CSVDataManager dataManager, int idUsuario) {
        super(owner, "Añadir Nueva Película", true);
        this.dataManager = dataManager;
        this.idUsuario = idUsuario;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(BtnGuardar);

        setupListeners();

        setSize(400, 450);
        setLocationRelativeTo(owner);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setupListeners() {
        BtnGuardar.addActionListener(e -> {
            if (validateInput()) {
                saveMovie();
            }
        });

        BtnCancelar.addActionListener(e -> {
            onCancel();
        });
    }

    private boolean validateInput() {
        if (Titulo.getText().trim().isEmpty() ||
            Año.getText().trim().isEmpty() ||
            Director.getText().trim().isEmpty() ||
            Genero.getText().trim().isEmpty() ||
            Descripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(Año.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número válido.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveMovie() {
        try {
            int newId = dataManager.generateNextPeliculaId();
            String titulo = Titulo.getText().trim();
            int ano = Integer.parseInt(Año.getText().trim());
            String director = Director.getText().trim();
            String genero = Genero.getText().trim();
            String descripcion = Descripcion.getText().trim();
            String imagenUrl = URLImagen.getText().trim();

            Pelicula nuevaPelicula = new Pelicula(newId, titulo, ano, director, descripcion, genero, imagenUrl, idUsuario);
            dataManager.addPelicula(nuevaPelicula);
            peliculaAdded = true;
            JOptionPane.showMessageDialog(this, "Película añadida con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la película: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void onCancel() {
        dispose();
    }

    public boolean isPeliculaAdded() {
        return peliculaAdded;
    }
}
