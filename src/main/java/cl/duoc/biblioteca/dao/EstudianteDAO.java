package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.modelo.Estudiante;

import java.util.List;

public interface EstudianteDAO {

    boolean crear(Estudiante estudiante);

    List<Estudiante> listarTodos();

    Estudiante buscarPorId(int id);

    boolean actualizar(Estudiante estudiante);

    boolean eliminar(int id);
}