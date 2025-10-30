import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    private CSVDataManager dataManager;
    private JTextField txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnLogin;

    public LoginFrame(CSVDataManager dataManager) {
        this.dataManager = dataManager;
        setTitle("IMDb made in china - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        txtCorreo = new JTextField(20);
        txtContraseña = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");

        txtCorreo.setText("");
        txtContraseña.setText("");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Bienvenido al IMDb del chino", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtCorreo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtContraseña, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnLogin);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 0, 5);
        formPanel.add(buttonPanel, gbc);


        add(formPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> attemptLogin());

        txtCorreo.addActionListener(e -> attemptLogin());
        txtContraseña.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContraseña.getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce tu correo y contraseña.", "Error de Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<Usuario> optionalUser = dataManager.authenticate(correo, contrasena);

        if (optionalUser.isPresent()) {
            Usuario loggedInUser = optionalUser.get();
            JOptionPane.showMessageDialog(this, "Credenciales correctas, entrando en la colección de "+correo, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            MainFrame mainFrame = new MainFrame(dataManager, loggedInUser);
            mainFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Inténtalo de nuevo.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            txtCorreo.setText("");
            txtContraseña.setText("");
        }
    }
}