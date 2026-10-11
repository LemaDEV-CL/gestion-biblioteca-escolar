package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.controlador.ControladorEstudiante;
import cl.duoc.biblioteca.controlador.ControladorLibro;
import cl.duoc.biblioteca.controlador.ControladorPrestamo;
import cl.duoc.biblioteca.modelo.Estudiante;
import cl.duoc.biblioteca.modelo.Libro;
import cl.duoc.biblioteca.modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Muestra los libros más prestados, el historial por estudiante y los préstamos pendientes.
 */
public class VistaReportes extends JFrame {

    private final ControladorPrestamo controladorPrestamo;
    private final ControladorLibro controladorLibro;
    private final ControladorEstudiante controladorEstudiante;

    private final DefaultTableModel modeloMasPrestados;
    private final DefaultTableModel modeloHistorial;
    private final DefaultTableModel modeloActuales;

    private final JComboBox<String> comboEstudiante;

    public VistaReportes() {

        controladorPrestamo =
                new ControladorPrestamo();

        controladorLibro =
                new ControladorLibro();

        controladorEstudiante =
                new ControladorEstudiante();

        setTitle("Reportes");
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane pestanas =
                new JTabbedPane();

        modeloMasPrestados =
                crearModelo(
                        new String[]{
                                "Libro",
                                "Cantidad de préstamos"
                        }
                );

        JTable tablaMasPrestados =
                new JTable(
                        modeloMasPrestados
                );

        pestanas.addTab(
                "Más prestados",
                new JScrollPane(
                        tablaMasPrestados
                )
        );

        JPanel panelHistorial =
                new JPanel(
                        new BorderLayout()
                );

        comboEstudiante =
                new JComboBox<>();

        JButton btnHistorial =
                new JButton(
                        "Consultar historial"
                );

        JPanel filtro =
                new JPanel();

        filtro.add(
                new JLabel("Estudiante:")
        );

        filtro.add(comboEstudiante);
        filtro.add(btnHistorial);

        modeloHistorial =
                crearModelo(
                        new String[]{
                                "Libro",
                                "Fecha préstamo",
                                "Vencimiento",
                                "Estado"
                        }
                );

        JTable tablaHistorial =
                new JTable(
                        modeloHistorial
                );

        panelHistorial.add(
                filtro,
                BorderLayout.NORTH
        );

        panelHistorial.add(
                new JScrollPane(
                        tablaHistorial
                ),
                BorderLayout.CENTER
        );

        pestanas.addTab(
                "Historial estudiante",
                panelHistorial
        );

        modeloActuales =
                crearModelo(
                        new String[]{
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Vencimiento"
                        }
                );

        JTable tablaActuales =
                new JTable(
                        modeloActuales
                );

        pestanas.addTab(
                "Actualmente prestados",
                new JScrollPane(
                        tablaActuales
                )
        );

        add(pestanas);

        btnHistorial.addActionListener(
                e -> cargarHistorial()
        );

        cargarEstudiantes();
        cargarMasPrestados();
        cargarPrestamosActuales();
    }

    private DefaultTableModel crearModelo(
            String[] columnas
    ) {

        return new DefaultTableModel(
                columnas,
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
    }

    private void cargarEstudiantes() {

        comboEstudiante.removeAllItems();

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

    private void cargarMasPrestados() {

        modeloMasPrestados
                .setRowCount(0);

        Map<Integer, Integer> conteo =
                new HashMap<>();

        for (Prestamo prestamo :
                controladorPrestamo
                        .listarTodos()) {

            int idLibro =
                    prestamo.getIdLibro();

            conteo.put(
                    idLibro,
                    conteo.getOrDefault(
                            idLibro,
                            0
                    ) + 1
            );
        }

        List<Map.Entry<Integer, Integer>>
                ranking =
                new ArrayList<>(
                        conteo.entrySet()
                );

        ranking.sort(
                (a, b) ->
                        Integer.compare(
                                b.getValue(),
                                a.getValue()
                        )
        );

        for (Map.Entry<Integer, Integer>
                entrada : ranking) {

            Libro libro =
                    controladorLibro
                            .buscarPorId(
                                    entrada.getKey()
                            );

            modeloMasPrestados.addRow(
                    new Object[]{
                            libro != null
                                    ? libro.getTitulo()
                                    : "Libro "
                                    + entrada.getKey(),

                            entrada.getValue()
                    }
            );
        }
    }

    private void cargarHistorial() {

        modeloHistorial
                .setRowCount(0);

        String seleccionado =
                (String)
                        comboEstudiante
                                .getSelectedItem();

        if (seleccionado == null) {
            return;
        }

        int idEstudiante =
                parsearId(
                        seleccionado
                );

        for (Prestamo prestamo :
                controladorPrestamo
                        .listarTodos()) {

            if (prestamo
                    .getIdEstudiante()
                    != idEstudiante) {

                continue;
            }

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

            modeloHistorial.addRow(
                    new Object[]{
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

    private void cargarPrestamosActuales() {

        modeloActuales
                .setRowCount(0);

        for (Prestamo prestamo :
                controladorPrestamo
                        .listarTodos()) {

            if (prestamo.isDevuelto()) {
                continue;
            }

            Estudiante estudiante =
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

            modeloActuales.addRow(
                    new Object[]{
                            estudiante != null
                                    ? estudiante.getNombre()
                                    : "Desconocido",

                            libro != null
                                    ? libro.getTitulo()
                                    : "Desconocido",

                            prestamo
                                    .getFechaPrestamo(),

                            prestamo
                                    .getFechaDevolucion()
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