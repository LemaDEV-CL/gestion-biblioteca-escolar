package cl.duoc.biblioteca.controlador;

import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.dao.impl.CategoriaDAOImpl;
import cl.duoc.biblioteca.modelo.Categoria;

import java.util.List;

public class ControladorCategoria {

    private final CategoriaDAO categoriaDAO;

    public ControladorCategoria() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    public boolean crear(Categoria categoria) {

        if (categoria == null
                || categoria.getNombre() == null
                || categoria.getNombre().isBlank()) {

            return false;
        }

        return categoriaDAO.crear(categoria);
    }

    public List<Categoria> listarTodos() {
        return categoriaDAO.listarTodos();
    }

    public Categoria buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return categoriaDAO.buscarPorId(id);
    }

    public boolean actualizar(Categoria categoria) {

        if (categoria == null
                || categoria.getId() <= 0
                || categoria.getNombre() == null
                || categoria.getNombre().isBlank()) {

            return false;
        }

        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminar(int id) {

        if (id <= 0) {
            return false;
        }

        return categoriaDAO.eliminar(id);
    }
}