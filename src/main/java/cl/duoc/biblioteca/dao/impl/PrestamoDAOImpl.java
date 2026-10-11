package cl.duoc.biblioteca.dao.impl;

import cl.duoc.biblioteca.conexion.DatabaseConnection;
import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.modelo.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa las operaciones de préstamos en MySQL mediante JDBC.
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    @Override
    public boolean crear(Prestamo prestamo) {

        String sql = """
                INSERT INTO prestamos
                (id_estudiante, id_libro, fecha_prestamo,
                 fecha_devolucion, devuelto)
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

            statement.setInt(
                    1,
                    prestamo.getIdEstudiante()
            );

            statement.setInt(
                    2,
                    prestamo.getIdLibro()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            prestamo.getFechaPrestamo()
                    )
            );

            if (prestamo.getFechaDevolucion() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(
                                prestamo.getFechaDevolucion()
                        )
                );

            } else {

                statement.setNull(
                        4,
                        Types.DATE
                );
            }

            statement.setBoolean(
                    5,
                    prestamo.isDevuelto()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves =
                             statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        prestamo.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear préstamo: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Prestamo> listarTodos() {

        List<Prestamo> prestamos =
                new ArrayList<>();

        String sql = """
                SELECT id, id_estudiante, id_libro,
                       fecha_prestamo, fecha_devolucion,
                       devuelto
                FROM prestamos
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

                prestamos.add(
                        crearPrestamoDesdeResultado(
                                resultado
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar préstamos: "
                            + e.getMessage()
            );
        }

        return prestamos;
    }

    @Override
    public Prestamo buscarPorId(int id) {

        String sql = """
                SELECT id, id_estudiante, id_libro,
                       fecha_prestamo, fecha_devolucion,
                       devuelto
                FROM prestamos
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

                    return crearPrestamoDesdeResultado(
                            resultado
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar préstamo: "
                            + e.getMessage()
            );
        }

        return null;
    }

    @Override
    public boolean actualizar(Prestamo prestamo) {

        String sql = """
                UPDATE prestamos
                SET id_estudiante = ?,
                    id_libro = ?,
                    fecha_prestamo = ?,
                    fecha_devolucion = ?,
                    devuelto = ?
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

            statement.setInt(
                    1,
                    prestamo.getIdEstudiante()
            );

            statement.setInt(
                    2,
                    prestamo.getIdLibro()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            prestamo.getFechaPrestamo()
                    )
            );

            if (prestamo.getFechaDevolucion() != null) {

                statement.setDate(
                        4,
                        Date.valueOf(
                                prestamo.getFechaDevolucion()
                        )
                );

            } else {

                statement.setNull(
                        4,
                        Types.DATE
                );
            }

            statement.setBoolean(
                    5,
                    prestamo.isDevuelto()
            );

            statement.setInt(
                    6,
                    prestamo.getId()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar préstamo: "
                            + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM prestamos
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
                    "Error al eliminar préstamo: "
                            + e.getMessage()
            );
        }

        return false;
    }

    private Prestamo crearPrestamoDesdeResultado(
            ResultSet resultado
    ) throws SQLException {

        Date fechaDevolucionSQL =
                resultado.getDate(
                        "fecha_devolucion"
                );

        LocalDate fechaDevolucion = null;

        if (fechaDevolucionSQL != null) {
            fechaDevolucion =
                    fechaDevolucionSQL.toLocalDate();
        }

        return new Prestamo(
                resultado.getInt("id"),
                resultado.getInt("id_estudiante"),
                resultado.getInt("id_libro"),
                resultado.getDate(
                        "fecha_prestamo"
                ).toLocalDate(),
                fechaDevolucion,
                resultado.getBoolean("devuelto")
        );
    }
}