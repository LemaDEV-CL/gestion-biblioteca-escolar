package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.modelo.Prestamo;

import java.util.List;

/**
 * Define las operaciones de consulta y mantenimiento de los préstamos.
 */
public interface PrestamoDAO {

    boolean crear(Prestamo prestamo);

    List<Prestamo> listarTodos();

    Prestamo buscarPorId(int id);

    boolean actualizar(Prestamo prestamo);

    boolean eliminar(int id);
}