package cl.duoc.biblioteca.controlador;

import cl.duoc.biblioteca.dao.UsuarioDAO;
import cl.duoc.biblioteca.dao.impl.UsuarioDAOImpl;
import cl.duoc.biblioteca.modelo.Usuario;

import java.util.List;

public class ControladorUsuario {

    private final UsuarioDAO usuarioDAO;

    public ControladorUsuario() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario autenticar(String correo, String contrasena) {

        if (correo == null
                || correo.isBlank()
                || contrasena == null
                || contrasena.isBlank()) {

            return null;
        }

        return usuarioDAO.autenticar(
                correo,
                contrasena
        );
    }

    public boolean crear(Usuario usuario) {

        if (!datosValidos(usuario)) {
            return false;
        }

        return usuarioDAO.crear(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(int id) {

        if (id <= 0) {
            return null;
        }

        return usuarioDAO.buscarPorId(id);
    }

    public boolean actualizar(Usuario usuario) {

        if (usuario == null
                || usuario.getId() <= 0
                || !datosValidos(usuario)) {

            return false;
        }

        return usuarioDAO.actualizar(usuario);
    }

    public boolean eliminar(int id) {

        if (id <= 0) {
            return false;
        }

        return usuarioDAO.eliminar(id);
    }

    private boolean datosValidos(Usuario usuario) {

        return usuario != null
                && usuario.getNombre() != null
                && !usuario.getNombre().isBlank()
                && usuario.getRut() != null
                && !usuario.getRut().isBlank()
                && usuario.getCorreo() != null
                && !usuario.getCorreo().isBlank()
                && usuario.getContrasena() != null
                && !usuario.getContrasena().isBlank()
                && usuario.getRol() != null
                && !usuario.getRol().isBlank();
    }
}