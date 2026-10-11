package cl.duoc.biblioteca.dao.impl;

import cl.duoc.biblioteca.conexion.DatabaseConnection;
import cl.duoc.biblioteca.dao.UsuarioDAO;
import cl.duoc.biblioteca.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa la autenticación y las operaciones de usuarios en MySQL mediante JDBC.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario autenticar(String correo, String contrasena) {

        String sql = """
                SELECT id, nombre, rut, correo, contraseña, rol
                FROM usuarios
                WHERE correo = ?
                AND contraseña = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(1, correo);
            statement.setString(2, contrasena);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                if (resultado.next()) {

                    return crearUsuarioDesdeResultado(
                            resultado
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al autenticar usuario: "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public boolean crear(Usuario usuario) {

        String sql = """
                INSERT INTO usuarios
                (nombre, rut, correo, contraseña, rol)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getRut());
            statement.setString(3, usuario.getCorreo());
            statement.setString(4, usuario.getContrasena());
            statement.setString(5, usuario.getRol());

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves =
                             statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        usuario.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Usuario> listarTodos() {

        List<Usuario> usuarios =
                new ArrayList<>();

        String sql = """
                SELECT id, nombre, rut, correo, contraseña, rol
                FROM usuarios
                ORDER BY id
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                usuarios.add(
                        crearUsuarioDesdeResultado(
                                resultado
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar usuarios: "
                            + e.getMessage()
            );
        }

        return usuarios;
    }

    @Override
    public Usuario buscarPorId(int id) {

        String sql = """
                SELECT id, nombre, rut, correo, contraseña, rol
                FROM usuarios
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                if (resultado.next()) {

                    return crearUsuarioDesdeResultado(
                            resultado
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar usuario: "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public boolean actualizar(Usuario usuario) {

        String sql = """
                UPDATE usuarios
                SET nombre = ?,
                    rut = ?,
                    correo = ?,
                    contraseña = ?,
                    rol = ?
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getRut());
            statement.setString(3, usuario.getCorreo());
            statement.setString(4, usuario.getContrasena());
            statement.setString(5, usuario.getRol());
            statement.setInt(6, usuario.getId());

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM usuarios
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection
                                .getInstance()
                                .getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar usuario: "
                            + e.getMessage()
            );
        }

        return false;
    }

    private Usuario crearUsuarioDesdeResultado(
            ResultSet resultado
    ) throws SQLException {

        return new Usuario(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("rut"),
                resultado.getString("correo"),
                resultado.getString("contraseña"),
                resultado.getString("rol")
        );
    }
}