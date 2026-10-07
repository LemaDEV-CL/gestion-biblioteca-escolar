package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.controlador.ControladorCategoria;
import cl.duoc.biblioteca.modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaCategorias extends JFrame {

    private final ControladorCategoria controlador;

    private final JTextField txtNombre;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private int idSeleccionado = -1;

    public VistaCategorias() {

        controlador =
                new ControladorCategoria();

        setTitle("Gestión de Categorías");
        setSize(550, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulario =
                new JPanel(new FlowLayout());

        txtNombre =
                new JTextField(20);

        JButton btnCrear =
                new JButton("Crear");

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        panelFormulario.add(
                new JLabel("Nombre:")
        );

        panelFormulario.add(txtNombre);
        panelFormulario.add(btnCrear);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{"ID", "Nombre"},
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

        add(
                panelFormulario,
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

    private void crear() {

        Categoria categoria =
                new Categoria(
                        0,
                        txtNombre
                                .getText()
                                .trim()
                );

        if (controlador.crear(categoria)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría creada."
            );

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible crear la categoría."
            );
        }
    }

    private void actualizar() {

        if (idSeleccionado <= 0) {
            return;
        }

        Categoria categoria =
                new Categoria(
                        idSeleccionado,
                        txtNombre
                                .getText()
                                .trim()
                );

        if (controlador.actualizar(categoria)) {

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
                    "No se pudo eliminar. "
                            + "La categoría puede estar "
                            + "asociada a libros."
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        for (Categoria categoria :
                controlador.listarTodos()) {

            modeloTabla.addRow(
                    new Object[]{
                            categoria.getId(),
                            categoria.getNombre()
                    }
            );
        }
    }

    private void seleccionarFila() {

        int fila =
                tabla.getSelectedRow();

        if (fila >= 0) {

            idSeleccionado =
                    (int) modeloTabla
                            .getValueAt(
                                    fila,
                                    0
                            );

            txtNombre.setText(
                    modeloTabla
                            .getValueAt(
                                    fila,
                                    1
                            )
                            .toString()
            );
        }
    }

    private void limpiar() {

        idSeleccionado = -1;
        txtNombre.setText("");
        tabla.clearSelection();
    }
}