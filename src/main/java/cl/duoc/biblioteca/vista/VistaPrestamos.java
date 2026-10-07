package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.controlador.ControladorEstudiante;
import cl.duoc.biblioteca.controlador.ControladorLibro;
import cl.duoc.biblioteca.controlador.ControladorPrestamo;
import cl.duoc.biblioteca.modelo.Estudiante;
import cl.duoc.biblioteca.modelo.Libro;
import cl.duoc.biblioteca.modelo.Prestamo;
import cl.duoc.biblioteca.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaPrestamos extends JFrame {

    private final Usuario usuario;

    private final ControladorPrestamo controladorPrestamo;
    private final ControladorLibro controladorLibro;
    private final ControladorEstudiante controladorEstudiante;

    private final JComboBox<String> comboEstudiante;
    private final JComboBox<String> comboLibro;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private final JButton btnPrestar;
    private final JButton btnDevolver;

    private int idEstudianteSesion = -1;

    public VistaPrestamos(Usuario usuario) {

        this.usuario = usuario;

        controladorPrestamo =
                new ControladorPrestamo();

        controladorLibro =
                new ControladorLibro();

        controladorEstudiante =
                new ControladorEstudiante();

        setTitle("Préstamos y Devoluciones");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        comboEstudiante =
                new JComboBox<>();

        comboLibro =
                new JComboBox<>();

        btnPrestar =
                new JButton("Registrar préstamo");

        btnDevolver =
                new JButton("Registrar devolución");

        JPanel superior =
                new JPanel();

        superior.add(
                new JLabel("Estudiante:")
        );

        superior.add(comboEstudiante);

        superior.add(
                new JLabel("Libro:")
        );

        superior.add(comboLibro);

        superior.add(btnPrestar);
        superior.add(btnDevolver);

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Vencimiento",
                                "Estado"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        tabla =
                new JTable(modeloTabla);

        add(
                superior,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        btnPrestar.addActionListener(
                e -> registrarPrestamo()
        );

        btnDevolver.addActionListener(
                e -> devolverPrestamo()
        );

        cargarCombos();
        cargarPrestamos();
    }

    private void cargarCombos() {

        comboEstudiante.removeAllItems();
        comboLibro.removeAllItems();

        boolean esEstudiante =
                "estudiante"
                        .equalsIgnoreCase(
                                usuario.getRol()
                        );

        if (esEstudiante) {

            for (Estudiante estudiante :
                    controladorEstudiante
                            .listarTodos()) {

                if (estudiante
                        .getRut()
                        .equalsIgnoreCase(
                                usuario.getRut()
                        )) {

                    idEstudianteSesion =
                            estudiante.getId();

                    comboEstudiante.addItem(
                            estudiante.getId()
                                    + " - "
                                    + estudiante.getNombre()
                    );

                    break;
                }
            }

            comboEstudiante.setEnabled(false);

        } else {

            for (Estudiante estudiante :
                    controladorEstudiante
                            .listarTodos()) {

                comboEstudiante.addItem(
                        estudiante.getId()
                                + " - "
                                + estudiante.getNombre()
                );
            }
        }

        for (Libro libro :
                controladorLibro.listarTodos()) {

            if (libro.getStock() > 0) {

                comboLibro.addItem(
                        libro.getId()
                                + " - "
                                + libro.getTitulo()
                                + " (Stock: "
                                + libro.getStock()
                                + ")"
                );
            }
        }
    }

    private void registrarPrestamo() {

        String libroSeleccionado =
                (String)
                        comboLibro
                                .getSelectedItem();

        if (libroSeleccionado == null) {
            return;
        }

        int idLibro =
                parsearId(
                        libroSeleccionado
                );

        int idEstudiante;

        if ("estudiante"
                .equalsIgnoreCase(
                        usuario.getRol()
                )) {

            idEstudiante =
                    idEstudianteSesion;

        } else {

            String estudianteSeleccionado =
                    (String)
                            comboEstudiante
                                    .getSelectedItem();

            if (estudianteSeleccionado
                    == null) {
                return;
            }

            idEstudiante =
                    parsearId(
                            estudianteSeleccionado
                    );
        }

        if (idEstudiante <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el estudiante "
                            + "asociado al usuario."
            );

            return;
        }

        btnPrestar.setEnabled(false);

        Thread hiloPrestamo =
                new Thread(() -> {

                    boolean resultado =
                            controladorPrestamo
                                    .registrarPrestamo(
                                            idEstudiante,
                                            idLibro
                                    );

                    SwingUtilities.invokeLater(
                            () -> {

                                btnPrestar
                                        .setEnabled(true);

                                if (resultado) {

                                    JOptionPane
                                            .showMessageDialog(
                                                    this,
                                                    "Préstamo registrado."
                                            );

                                    cargarCombos();
                                    cargarPrestamos();

                                } else {

                                    JOptionPane
                                            .showMessageDialog(
                                                    this,
                                                    "No fue posible "
                                                            + "registrar el préstamo."
                                            );
                                }
                            }
                    );
                });

        hiloPrestamo.start();
    }

    private void devolverPrestamo() {

        int fila =
                tabla.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un préstamo."
            );

            return;
        }

        int idPrestamo =
                (int) modeloTabla
                        .getValueAt(
                                fila,
                                0
                        );

        btnDevolver.setEnabled(false);

        Thread hiloDevolucion =
                new Thread(() -> {

                    boolean resultado =
                            controladorPrestamo
                                    .devolverPrestamo(
                                            idPrestamo
                                    );

                    SwingUtilities.invokeLater(
                            () -> {

                                btnDevolver
                                        .setEnabled(true);

                                if (resultado) {

                                    JOptionPane
                                            .showMessageDialog(
                                                    this,
                                                    "Devolución registrada."
                                            );

                                    cargarCombos();
                                    cargarPrestamos();

                                } else {

                                    JOptionPane
                                            .showMessageDialog(
                                                    this,
                                                    "No fue posible "
                                                            + "registrar la devolución."
                                            );
                                }
                            }
                    );
                });

        hiloDevolucion.start();
    }

    private void cargarPrestamos() {

        modeloTabla.setRowCount(0);

        boolean estudiante =
                "estudiante"
                        .equalsIgnoreCase(
                                usuario.getRol()
                        );

        for (Prestamo prestamo :
                controladorPrestamo
                        .listarTodos()) {

            if (estudiante
                    && prestamo
                    .getIdEstudiante()
                    != idEstudianteSesion) {

                continue;
            }

            Estudiante estudiantePrestamo =
                    controladorEstudiante
                            .buscarPorId(
                                    prestamo
                                            .getIdEstudiante()
                            );

            Libro libro =
                    controladorLibro
                            .buscarPorId(
                                    prestamo
                                            .getIdLibro()
                            );

            String estado;

            if (prestamo.isDevuelto()) {

                estado = "Devuelto";

            } else if (
                    controladorPrestamo
                            .estaAtrasado(
                                    prestamo
                            )
            ) {

                estado = "Atrasado";

            } else {

                estado = "Prestado";
            }

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),

                            estudiantePrestamo
                                    != null
                                    ? estudiantePrestamo
                                    .getNombre()
                                    : "Desconocido",

                            libro != null
                                    ? libro.getTitulo()
                                    : "Desconocido",

                            prestamo
                                    .getFechaPrestamo(),

                            prestamo
                                    .getFechaDevolucion(),

                            estado
                    }
            );
        }
    }

    private int parsearId(
            String valor
    ) {

        return Integer.parseInt(
                valor.substring(
                        0,
                        valor.indexOf(" - ")
                )
        );
    }
}