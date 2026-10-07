package cl.duoc.biblioteca.dao.impl;

import cl.duoc.biblioteca.conexion.DatabaseConnection;
import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.modelo.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    @Override
    public boolean crear(Estudiante estudiante) {

        String sql = """
                INSERT INTO estudiantes
                (nombre, rut, curso, correo)
                VALUES (?, ?, ?, ?)
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

            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves =
                             statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        estudiante.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear estudiante: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Estudiante> listarTodos() {

        List<Estudiante> estudiantes =
                new ArrayList<>();

        String sql = """
                SELECT id, nombre, rut, curso, correo
                FROM estudiantes
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

                Estudiante estudiante =
                        new Estudiante(
                                resultado.getInt("id"),
                                resultado.getString("nombre"),
                                resultado.getString("rut"),
                                resultado.getString("correo"),
                                resultado.getString("curso")
                        );

                estudiantes.add(estudiante);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar estudiantes: "
                            + e.getMessage()
            );
        }

        return estudiantes;
    }

    @Override
    public Estudiante buscarPorId(int id) {

        String sql = """
                SELECT id, nombre, rut, curso, correo
                FROM estudiantes
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

                    return new Estudiante(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("rut"),
                            resultado.getString("correo"),
                            resultado.getString("curso")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar estudiante: "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {

        String sql = """
                UPDATE estudiantes
                SET nombre = ?,
                    rut = ?,
                    curso = ?,
                    correo = ?
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

            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());
            statement.setInt(5, estudiante.getId());

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar estudiante: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM estudiantes
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
                    "Error al eliminar estudiante: "
                            + e.getMessage()
            );
        }

        return false;
    }
}