package cl.duoc.biblioteca.controlador;

import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.dao.impl.LibroDAOImpl;
import cl.duoc.biblioteca.modelo.Libro;

import java.util.List;

public class ControladorLibro {

    private final LibroDAO libroDAO;

    public ControladorLibro() {
        this.libroDAO = new LibroDAOImpl();
    }

    public boolean crear(Libro libro) {

        if (!datosValidos(libro)) {
            return false;
        }

        return libroDAO.crear(libro);
    }

    public List<Libro> listarTodos() {
        return libroDAO.listarTodos();
    }

    public Libro buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return libroDAO.buscarPorId(id);
    }

    public boolean actualizar(Libro libro) {

        if (libro == null
                || libro.getId() <= 0
                || !datosValidos(libro)) {

            return false;
        }

        return libroDAO.actualizar(libro);
    }

    public boolean eliminar(int id) {

        if (id <= 0) {
            return false;
        }

        return libroDAO.eliminar(id);
    }

    private boolean datosValidos(Libro libro) {

        return libro != null
                && libro.getTitulo() != null
                && !libro.getTitulo().isBlank()
                && libro.getAutor() != null
                && !libro.getAutor().isBlank()
                && libro.getIsbn() != null
                && !libro.getIsbn().isBlank()
                && libro.getEditorial() != null
                && !libro.getEditorial().isBlank()
                && libro.getStock() >= 0
                && libro.getIdCategoria() > 0;
    }
}