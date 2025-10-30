import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CSVDataManager dataManager = new CSVDataManager();

                LoginFrame loginFrame = new LoginFrame(dataManager);
                loginFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error fatal: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al iniciar la aplicación. Asegúrese de que 'usuarios.csv' y 'peliculas.csv' están presentes.",
                        "Error al cargar los datos",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
