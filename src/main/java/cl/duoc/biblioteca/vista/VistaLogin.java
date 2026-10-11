package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.controlador.ControladorUsuario;
import cl.duoc.biblioteca.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Muestra el formulario de acceso y solicita la autenticación del usuario.
 */
public class VistaLogin extends JFrame {

    private final JTextField txtCorreo;
    private final JPasswordField txtContrasena;
    private final ControladorUsuario controladorUsuario;

    public VistaLogin() {

        controladorUsuario = new ControladorUsuario();

        setTitle("Biblioteca Escolar - Inicio de sesión");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 30, 30, 30
                )
        );

        txtCorreo = new JTextField();
        txtContrasena = new JPasswordField();

        JButton btnIngresar =
                new JButton("Ingresar");

        JButton btnSalir =
                new JButton("Salir");

        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);

        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);

        panel.add(btnIngresar);
        panel.add(btnSalir);

        add(panel);

        btnIngresar.addActionListener(
                e -> autenticar()
        );

        btnSalir.addActionListener(
                e -> System.exit(0)
        );
    }

    private void autenticar() {

        String correo =
                txtCorreo.getText().trim();

        String contrasena =
                new String(
                        txtContrasena.getPassword()
                );

        Usuario usuario =
                controladorUsuario.autenticar(
                        correo,
                        contrasena
                );

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido " + usuario.getNombre()
            );

            new VistaPrincipal(usuario)
                    .setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}