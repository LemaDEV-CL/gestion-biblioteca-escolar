package cl.duoc.biblioteca.dao.impl;

import cl.duoc.biblioteca.conexion.DatabaseConnection;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.modelo.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa las operaciones de libros en MySQL mediante JDBC.
 */
public class LibroDAOImpl implements LibroDAO {

    @Override
    public boolean crear(Libro libro) {

        String sql = """
                INSERT INTO libros
                (titulo, autor, isbn, editorial, stock, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?)
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

            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getIdCategoria());

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves =
                             statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        libro.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear libro: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Libro> listarTodos() {

        List<Libro> libros =
                new ArrayList<>();

        String sql = """
                SELECT id, titulo, autor, isbn,
                       editorial, stock, id_categoria
                FROM libros
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

                Libro libro =
                        new Libro(
                                resultado.getInt("id"),
                                resultado.getString("titulo"),
                                resultado.getString("autor"),
                                resultado.getString("isbn"),
                                resultado.getString("editorial"),
                                resultado.getInt("stock"),
                                resultado.getInt("id_categoria")
                        );

                libros.add(libro);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar libros: "
                            + e.getMessage()
            );
        }

        return libros;
    }

    @Override
    public Libro buscarPorId(int id) {

        String sql = """
                SELECT id, titulo, autor, isbn,
                       editorial, stock, id_categoria
                FROM libros
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

                    return new Libro(
                            resultado.getInt("id"),
                            resultado.getString("titulo"),
                            resultado.getString("autor"),
                            resultado.getString("isbn"),
                            resultado.getString("editorial"),
                            resultado.getInt("stock"),
                            resultado.getInt("id_categoria")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar libro: "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public boolean actualizar(Libro libro) {

        String sql = """
                UPDATE libros
                SET titulo = ?,
                    autor = ?,
                    isbn = ?,
                    editorial = ?,
                    stock = ?,
                    id_categoria = ?
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

            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getIdCategoria());
            statement.setInt(7, libro.getId());

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar libro: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM libros
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
                    "Error al eliminar libro: "
                            + e.getMessage()
            );
        }

        return false;
    }
}