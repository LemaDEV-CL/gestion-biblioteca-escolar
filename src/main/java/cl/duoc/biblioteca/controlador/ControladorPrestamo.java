package cl.duoc.biblioteca.controlador;

import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.dao.impl.EstudianteDAOImpl;
import cl.duoc.biblioteca.dao.impl.LibroDAOImpl;
import cl.duoc.biblioteca.dao.impl.PrestamoDAOImpl;
import cl.duoc.biblioteca.modelo.Estudiante;
import cl.duoc.biblioteca.modelo.Libro;
import cl.duoc.biblioteca.modelo.Prestamo;

import java.time.LocalDate;
import java.util.List;

public class ControladorPrestamo {

    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;
    private final EstudianteDAO estudianteDAO;
    private static final Object LOCK_STOCK = new Object();

    public ControladorPrestamo() {

        this.prestamoDAO =
                new PrestamoDAOImpl();

        this.libroDAO =
                new LibroDAOImpl();

        this.estudianteDAO =
                new EstudianteDAOImpl();
    }

    public boolean registrarPrestamo(
            int idEstudiante,
            int idLibro
    ) {

        synchronized (LOCK_STOCK) {

            if (idEstudiante <= 0 || idLibro <= 0) {
                return false;
            }

            Estudiante estudiante =
                    estudianteDAO.buscarPorId(
                            idEstudiante
                    );

            if (estudiante == null) {
                return false;
            }

            Libro libro =
                    libroDAO.buscarPorId(
                            idLibro
                    );

            if (libro == null
                    || libro.getStock() <= 0) {

                return false;
            }

            int stockOriginal =
                    libro.getStock();

            libro.setStock(
                    stockOriginal - 1
            );

            if (!libroDAO.actualizar(libro)) {
                return false;
            }

            Prestamo prestamo =
                    new Prestamo(
                            0,
                            idEstudiante,
                            idLibro,
                            LocalDate.now(),
                            LocalDate.now().plusDays(7),
                            false
                    );

            if (!prestamoDAO.crear(prestamo)) {

                libro.setStock(
                        stockOriginal
                );

                libroDAO.actualizar(libro);

                return false;
            }

            return true;
        }
    }

    public boolean devolverPrestamo(
            int idPrestamo
    ) {

        synchronized (LOCK_STOCK) {

            Prestamo prestamo =
                    prestamoDAO.buscarPorId(
                            idPrestamo
                    );

            if (prestamo == null
                    || prestamo.isDevuelto()) {

                return false;
            }

            Libro libro =
                    libroDAO.buscarPorId(
                            prestamo.getIdLibro()
                    );

            if (libro == null) {
                return false;
            }

            int stockOriginal =
                    libro.getStock();

            libro.setStock(
                    stockOriginal + 1
            );

            if (!libroDAO.actualizar(libro)) {
                return false;
            }

            prestamo.setDevuelto(true);

            if (!prestamoDAO.actualizar(prestamo)) {

                libro.setStock(
                        stockOriginal
                );

                libroDAO.actualizar(libro);

                return false;
            }

            return true;
        }
    }

    public boolean estaAtrasado(
            Prestamo prestamo
    ) {

        if (prestamo == null
                || prestamo.isDevuelto()
                || prestamo.getFechaDevolucion() == null) {

            return false;
        }

        return prestamo
                .getFechaDevolucion()
                .isBefore(LocalDate.now());
    }

    public List<Prestamo> listarTodos() {
        return prestamoDAO.listarTodos();
    }

    public Prestamo buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return prestamoDAO.buscarPorId(id);
    }
}