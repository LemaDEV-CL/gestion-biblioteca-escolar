package cl.duoc.biblioteca.controlador;

import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.dao.impl.EstudianteDAOImpl;
import cl.duoc.biblioteca.modelo.Estudiante;

import java.util.List;

/**
 * Valida los datos de los estudiantes y coordina su gestión con el DAO.
 */
public class ControladorEstudiante {

    private final EstudianteDAO estudianteDAO;

    public ControladorEstudiante() {
        this.estudianteDAO = new EstudianteDAOImpl();
    }

    public boolean crear(Estudiante estudiante) {

        if (!datosValidos(estudiante)) {
            return false;
        }

        return estudianteDAO.crear(estudiante);
    }

    public List<Estudiante> listarTodos() {
        return estudianteDAO.listarTodos();
    }

    public Estudiante buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return estudianteDAO.buscarPorId(id);
    }

    public boolean actualizar(Estudiante estudiante) {

        if (estudiante == null
                || estudiante.getId() <= 0
                || !datosValidos(estudiante)) {

            return false;
        }

        return estudianteDAO.actualizar(estudiante);
    }

    public boolean eliminar(int id) {

        if (id <= 0) {
            return false;
        }

        return estudianteDAO.eliminar(id);
    }

    private boolean datosValidos(Estudiante estudiante) {

        return estudiante != null
                && estudiante.getNombre() != null
                && !estudiante.getNombre().isBlank()
                && estudiante.getRut() != null
                && !estudiante.getRut().isBlank()
                && estudiante.getCorreo() != null
                && !estudiante.getCorreo().isBlank()
                && estudiante.getCurso() != null
                && !estudiante.getCurso().isBlank();
    }
}
