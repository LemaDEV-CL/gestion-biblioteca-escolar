package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.modelo.Libro;

import java.util.List;

/**
 * Define las operaciones de consulta y mantenimiento de los libros.
 */
public interface LibroDAO {

    boolean crear(Libro libro);

    List<Libro> listarTodos();

    Libro buscarPorId(int id);

    boolean actualizar(Libro libro);

    boolean eliminar(int id);
}