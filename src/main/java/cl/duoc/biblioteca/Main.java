package cl.duoc.biblioteca;

import cl.duoc.biblioteca.vista.VistaLogin;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new VistaLogin()
                        .setVisible(true)
        );
    }
}