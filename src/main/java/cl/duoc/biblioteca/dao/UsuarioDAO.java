package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.modelo.Usuario;

import java.util.List;

public interface UsuarioDAO {

    Usuario autenticar(String correo, String contrasena);

    boolean crear(Usuario usuario);

    List<Usuario> listarTodos();

    Usuario buscarPorId(int id);

    boolean actualizar(Usuario usuario);

    boolean eliminar(int id);
}