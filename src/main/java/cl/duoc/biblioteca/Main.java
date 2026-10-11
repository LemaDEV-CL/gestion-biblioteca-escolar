package cl.duoc.biblioteca;

import cl.duoc.biblioteca.vista.VistaLogin;

import javax.swing.*;

/**
 * Inicia la aplicación y abre la ventana de acceso en el hilo de Swing.
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new VistaLogin()
                        .setVisible(true)
        );
    }
}