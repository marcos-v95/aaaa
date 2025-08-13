package Main;

import java.sql.Connection;
import java.sql.SQLException;
import Config.DatabaseConnection;

public class TestConexion {

    // Esta clase sirve para probar si la conexión a la base de datos funciona correctamente.

    public static void main(String[] args) {
        /**
         * 🔹 Se prueba únicamente la conexión a la base de datos.
         * 🔹 No se ejecuta ninguna consulta.
         */
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión establecida con éxito.");
            } else {
                System.out.println("❌ No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}