import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DetallePeliculaDialog extends JDialog {
    private JPanel contentPane;
    private JButton BtnCerrar;
    private JTextArea SinopsisPelicula;
    private JLabel TituloPelicula;
    private JLabel AñoPelicula;
    private JLabel DirectorPelicula;
    private JLabel GeneroPelicula;
    private JLabel UrlImagenPelicula;

    public DetallePeliculaDialog(Frame parent, Pelicula pelicula) {
        super(parent, pelicula.getTitulo(), true); // Modal

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(BtnCerrar);
        setSize(600, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        TituloPelicula.setText(pelicula.getTitulo());
        AñoPelicula.setText(String.valueOf(pelicula.getAno()));
        DirectorPelicula.setText(pelicula.getDirector());
        GeneroPelicula.setText(pelicula.getGenero());
        UrlImagenPelicula.setText(pelicula.getImagenUrl());
        SinopsisPelicula.setText(pelicula.getDescripcion());

        SinopsisPelicula.setEditable(false);
        SinopsisPelicula.setLineWrap(true);
        SinopsisPelicula.setWrapStyleWord(true);

        BtnCerrar.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }
}
