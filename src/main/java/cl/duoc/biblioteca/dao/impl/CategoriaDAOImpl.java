package cl.duoc.biblioteca.dao.impl;

import cl.duoc.biblioteca.conexion.DatabaseConnection;
import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public boolean crear(Categoria categoria) {

        String sql = """
                INSERT INTO categorias (nombre)
                VALUES (?)
                """;

        try (
                Connection conexion =
                        DatabaseConnection.getInstance().getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    categoria.getNombre()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves =
                             statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        categoria.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear categoría: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Categoria> listarTodos() {

        List<Categoria> categorias =
                new ArrayList<>();

        String sql = """
                SELECT id, nombre
                FROM categorias
                ORDER BY id
                """;

        try (
                Connection conexion =
                        DatabaseConnection.getInstance().getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                Categoria categoria =
                        new Categoria(
                                resultado.getInt("id"),
                                resultado.getString("nombre")
                        );

                categorias.add(categoria);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar categorías: "
                            + e.getMessage()
            );
        }

        return categorias;
    }

    @Override
    public Categoria buscarPorId(int id) {

        String sql = """
                SELECT id, nombre
                FROM categorias
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection.getInstance().getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                if (resultado.next()) {

                    return new Categoria(
                            resultado.getInt("id"),
                            resultado.getString("nombre")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar categoría: "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public boolean actualizar(Categoria categoria) {

        String sql = """
                UPDATE categorias
                SET nombre = ?
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection.getInstance().getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    categoria.getNombre()
            );

            statement.setInt(
                    2,
                    categoria.getId()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar categoría: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM categorias
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        DatabaseConnection.getInstance().getConnection();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar categoría: "
                            + e.getMessage()
            );
        }

        return false;
    }
}