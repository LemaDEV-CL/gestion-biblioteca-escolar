package cl.duoc.biblioteca.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static ConexionDB instance;

    private static final String URL =
            "jdbc:mysql://localhost:3307/biblioteca?createDatabaseIfNotExist=true";

    private static final String USER = "bibliotecaadmin";
    private static final String PASSWORD = "admin1234";

    private ConexionDB() {
    }

    public static ConexionDB obtenerInstancia() {

        if (instance == null) {
            instance = new ConexionDB();
        }

        return instance;
    }

    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}