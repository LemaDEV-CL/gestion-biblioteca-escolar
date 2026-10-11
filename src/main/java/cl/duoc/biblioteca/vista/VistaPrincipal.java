package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Muestra el menú de la biblioteca con las opciones disponibles según el rol.
 */
public class VistaPrincipal extends JFrame {

    private final Usuario usuario;

    public VistaPrincipal(Usuario usuario) {

        this.usuario = usuario;

        setTitle(
                "Biblioteca Escolar - "
                        + usuario.getNombre()
        );

        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                7,
                                1,
                                10,
                                10
                        )
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 40, 20, 40
                )
        );

        JLabel lblUsuario =
                new JLabel(
                        "Usuario: "
                                + usuario.getNombre()
                                + " | Rol: "
                                + usuario.getRol(),
                        SwingConstants.CENTER
                );

        JButton btnCategorias =
                new JButton("Categorías");

        JButton btnLibros =
                new JButton("Libros");

        JButton btnEstudiantes =
                new JButton("Estudiantes");

        JButton btnPrestamos =
                new JButton("Préstamos y devoluciones");

        JButton btnReportes =
                new JButton("Reportes");

        JButton btnCerrarSesion =
                new JButton("Cerrar sesión");

        panel.add(lblUsuario);
        panel.add(btnCategorias);
        panel.add(btnLibros);
        panel.add(btnEstudiantes);
        panel.add(btnPrestamos);
        panel.add(btnReportes);
        panel.add(btnCerrarSesion);

        add(panel);

        boolean bibliotecario =
                "bibliotecario"
                        .equalsIgnoreCase(
                                usuario.getRol()
                        );

        btnCategorias.setVisible(
                bibliotecario
        );

        btnEstudiantes.setVisible(
                bibliotecario
        );

        btnReportes.setVisible(
                bibliotecario
        );

        btnCategorias.addActionListener(
                e -> new VistaCategorias()
                        .setVisible(true)
        );

        btnLibros.addActionListener(
                e -> new VistaLibros(
                        !bibliotecario
                ).setVisible(true)
        );

        btnEstudiantes.addActionListener(
                e -> new VistaEstudiantes()
                        .setVisible(true)
        );

        btnPrestamos.addActionListener(
                e -> new VistaPrestamos(usuario)
                        .setVisible(true)
        );

        btnReportes.addActionListener(
                e -> new VistaReportes()
                        .setVisible(true)
        );

        btnCerrarSesion.addActionListener(
                e -> {

                    new VistaLogin()
                            .setVisible(true);

                    dispose();
                }
        );
    }
}