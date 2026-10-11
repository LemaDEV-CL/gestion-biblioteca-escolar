package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.modelo.Categoria;

import java.util.List;

/**
 * Define las operaciones de consulta y mantenimiento de las categorías.
 */
public interface CategoriaDAO {

    boolean crear(Categoria categoria);

    List<Categoria> listarTodos();

    Categoria buscarPorId(int id);

    boolean actualizar(Categoria categoria);

    boolean eliminar(int id);
}