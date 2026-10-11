package cl.duoc.biblioteca.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza el acceso a las conexiones MySQL mediante una instancia compartida.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;

    private static final String URL =
            "jdbc:mysql://localhost:3307/biblioteca?createDatabaseIfNotExist=true";

    private static final String USER = "bibliotecaadmin";
    private static final String PASSWORD = "admin1234";

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {

        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}