package cl.duoc.biblioteca;

import cl.duoc.biblioteca.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (
                Connection conexion =
                        ConexionDB
                                .obtenerInstancia()
                                .obtenerConexion()
        ) {

            System.out.println("Conexión exitosa a la base de datos.");

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión: " + e.getMessage()
            );
        }
    }
}