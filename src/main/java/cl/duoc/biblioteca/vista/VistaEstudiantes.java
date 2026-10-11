package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.controlador.ControladorEstudiante;
import cl.duoc.biblioteca.modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Permite consultar, crear, actualizar y eliminar estudiantes desde una ventana Swing.
 */
public class VistaEstudiantes extends JFrame {

    private final ControladorEstudiante controlador;

    private final JTextField txtNombre;
    private final JTextField txtRut;
    private final JTextField txtCurso;
    private final JTextField txtCorreo;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private int idSeleccionado = -1;

    public VistaEstudiantes() {

        controlador =
                new ControladorEstudiante();

        setTitle("Gestión de Estudiantes");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                5,
                                5
                        )
                );

        txtNombre = new JTextField();
        txtRut = new JTextField();
        txtCurso = new JTextField();
        txtCorreo = new JTextField();

        formulario.add(
                new JLabel("Nombre:")
        );
        formulario.add(txtNombre);

        formulario.add(
                new JLabel("RUT:")
        );
        formulario.add(txtRut);

        formulario.add(
                new JLabel("Curso:")
        );
        formulario.add(txtCurso);

        formulario.add(
                new JLabel("Correo:")
        );
        formulario.add(txtCorreo);

        JPanel botones = new JPanel();

        JButton btnCrear =
                new JButton("Crear");

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nombre",
                                "RUT",
                                "Curso",
                                "Correo"
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

        tabla = new JTable(modeloTabla);

        JPanel superior =
                new JPanel(
                        new BorderLayout()
                );

        superior.add(
                formulario,
                BorderLayout.CENTER
        );

        superior.add(
                botones,
                BorderLayout.SOUTH
        );

        add(
                superior,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        btnCrear.addActionListener(
                e -> crear()
        );

        btnActualizar.addActionListener(
                e -> actualizar()
        );

        btnEliminar.addActionListener(
                e -> eliminar()
        );

        btnLimpiar.addActionListener(
                e -> limpiar()
        );

        tabla.getSelectionModel()
                .addListSelectionListener(
                        e -> seleccionarFila()
                );

        cargarTabla();
    }

    private Estudiante obtenerFormulario(
            int id
    ) {

        return new Estudiante(
                id,
                txtNombre.getText().trim(),
                txtRut.getText().trim(),
                txtCorreo.getText().trim(),
                txtCurso.getText().trim()
        );
    }

    private void crear() {

        if (controlador.crear(
                obtenerFormulario(0)
        )) {

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Datos inválidos."
            );
        }
    }

    private void actualizar() {

        if (idSeleccionado <= 0) {
            return;
        }

        if (controlador.actualizar(
                obtenerFormulario(
                        idSeleccionado
                )
        )) {

            cargarTabla();
            limpiar();
        }
    }

    private void eliminar() {

        if (idSeleccionado <= 0) {
            return;
        }

        if (controlador.eliminar(
                idSeleccionado
        )) {

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar "
                            + "el estudiante."
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        for (Estudiante estudiante :
                controlador.listarTodos()) {

            modeloTabla.addRow(
                    new Object[]{
                            estudiante.getId(),
                            estudiante.getNombre(),
                            estudiante.getRut(),
                            estudiante.getCurso(),
                            estudiante.getCorreo()
                    }
            );
        }
    }

    private void seleccionarFila() {

        int fila =
                tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        idSeleccionado =
                (int) modeloTabla
                        .getValueAt(
                                fila,
                                0
                        );

        Estudiante estudiante =
                controlador.buscarPorId(
                        idSeleccionado
                );

        if (estudiante == null) {
            return;
        }

        txtNombre.setText(
                estudiante.getNombre()
        );

        txtRut.setText(
                estudiante.getRut()
        );

        txtCurso.setText(
                estudiante.getCurso()
        );

        txtCorreo.setText(
                estudiante.getCorreo()
        );
    }

    private void limpiar() {

        idSeleccionado = -1;

        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        tabla.clearSelection();
    }
}