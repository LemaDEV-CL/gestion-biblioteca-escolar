package cl.duoc.biblioteca.vista;

import cl.duoc.biblioteca.controlador.ControladorCategoria;
import cl.duoc.biblioteca.controlador.ControladorLibro;
import cl.duoc.biblioteca.modelo.Categoria;
import cl.duoc.biblioteca.modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaLibros extends JFrame {

    private final ControladorLibro controladorLibro;
    private final ControladorCategoria controladorCategoria;

    private final JTextField txtTitulo;
    private final JTextField txtAutor;
    private final JTextField txtIsbn;
    private final JTextField txtEditorial;

    private final JSpinner spinnerStock;
    private final JComboBox<String> comboCategoria;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private int idSeleccionado = -1;

    public VistaLibros(boolean soloLectura) {

        controladorLibro =
                new ControladorLibro();

        controladorCategoria =
                new ControladorCategoria();

        setTitle("Gestión de Libros");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                5,
                                5
                        )
                );

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtEditorial = new JTextField();

        spinnerStock =
                new JSpinner(
                        new SpinnerNumberModel(
                                0,
                                0,
                                9999,
                                1
                        )
                );

        comboCategoria =
                new JComboBox<>();

        formulario.add(
                new JLabel("Título:")
        );
        formulario.add(txtTitulo);

        formulario.add(
                new JLabel("Autor:")
        );
        formulario.add(txtAutor);

        formulario.add(
                new JLabel("ISBN:")
        );
        formulario.add(txtIsbn);

        formulario.add(
                new JLabel("Editorial:")
        );
        formulario.add(txtEditorial);

        formulario.add(
                new JLabel("Stock:")
        );
        formulario.add(spinnerStock);

        formulario.add(
                new JLabel("Categoría:")
        );
        formulario.add(comboCategoria);

        JPanel botones =
                new JPanel();

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
                                "Título",
                                "Autor",
                                "ISBN",
                                "Editorial",
                                "Stock",
                                "Categoría"
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

        if (soloLectura) {

            txtTitulo.setEditable(false);
            txtAutor.setEditable(false);
            txtIsbn.setEditable(false);
            txtEditorial.setEditable(false);

            spinnerStock.setEnabled(false);
            comboCategoria.setEnabled(false);

            btnCrear.setEnabled(false);
            btnActualizar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnLimpiar.setEnabled(false);
        }

        cargarCategorias();
        cargarTabla();
    }

    private void cargarCategorias() {

        comboCategoria.removeAllItems();

        for (Categoria categoria :
                controladorCategoria
                        .listarTodos()) {

            comboCategoria.addItem(
                    categoria.getId()
                            + " - "
                            + categoria.getNombre()
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        for (Libro libro :
                controladorLibro.listarTodos()) {

            Categoria categoria =
                    controladorCategoria
                            .buscarPorId(
                                    libro.getIdCategoria()
                            );

            String nombreCategoria =
                    categoria != null
                            ? categoria.getNombre()
                            : "Sin categoría";

            modeloTabla.addRow(
                    new Object[]{
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getIsbn(),
                            libro.getEditorial(),
                            libro.getStock(),
                            nombreCategoria
                    }
            );
        }
    }

    private Libro obtenerLibroFormulario(
            int id
    ) {

        String categoria =
                (String)
                        comboCategoria
                                .getSelectedItem();

        if (categoria == null) {
            return null;
        }

        int idCategoria =
                parsearId(categoria);

        return new Libro(
                id,
                txtTitulo.getText().trim(),
                txtAutor.getText().trim(),
                txtIsbn.getText().trim(),
                txtEditorial.getText().trim(),
                (int) spinnerStock.getValue(),
                idCategoria
        );
    }

    private void crear() {

        Libro libro =
                obtenerLibroFormulario(0);

        if (controladorLibro.crear(libro)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro creado."
            );

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

        Libro libro =
                obtenerLibroFormulario(
                        idSeleccionado
                );

        if (controladorLibro
                .actualizar(libro)) {

            cargarTabla();
            limpiar();
        }
    }

    private void eliminar() {

        if (idSeleccionado <= 0) {
            return;
        }

        if (controladorLibro.eliminar(
                idSeleccionado
        )) {

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar "
                            + "el libro."
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
                        .getValueAt(fila, 0);

        Libro libro =
                controladorLibro
                        .buscarPorId(
                                idSeleccionado
                        );

        if (libro == null) {
            return;
        }

        txtTitulo.setText(
                libro.getTitulo()
        );

        txtAutor.setText(
                libro.getAutor()
        );

        txtIsbn.setText(
                libro.getIsbn()
        );

        txtEditorial.setText(
                libro.getEditorial()
        );

        spinnerStock.setValue(
                libro.getStock()
        );

        seleccionarCategoria(
                libro.getIdCategoria()
        );
    }

    private void seleccionarCategoria(
            int idCategoria
    ) {

        for (int i = 0;
             i < comboCategoria
                     .getItemCount();
             i++) {

            String item =
                    comboCategoria
                            .getItemAt(i);

            if (parsearId(item)
                    == idCategoria) {

                comboCategoria
                        .setSelectedIndex(i);

                break;
            }
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

    private void limpiar() {

        idSeleccionado = -1;

        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");

        spinnerStock.setValue(0);

        if (comboCategoria
                .getItemCount() > 0) {

            comboCategoria
                    .setSelectedIndex(0);
        }

        tabla.clearSelection();
    }
}