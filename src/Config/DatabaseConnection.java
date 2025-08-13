package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Esta clase se encarga de establecer una conexión con la base de datos MySQL.
    // La conexión es necesaria para poder enviar o recibir información (como insertar, actualizar o leer personas y domicilios).

    private static final String URL = "jdbc:mysql://localhost:3306/ejemplo_jdbc";  // Dirección del servidor y nombre de la base de datos.
    private static final String USER = "root";  // Usuario para acceder a la base de datos
    private static final String PASSWORD = "";  // Contraseña del usuario (vacía en este caso)

    static {
        // Este bloque se ejecuta una única vez al cargar la clase.
        // Sirve para asegurarse de que el "driver" (puente entre Java y MySQL) esté cargado.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: No se encontró el driver JDBC.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Este metodo devuelve un objeto de tipo Connection, que representa la conexión con la base de datos.
        // Si los datos están mal, lanza una excepción para avisar que hay un problema.
        if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty() || PASSWORD == null) {
            throw new SQLException("Configuración de la base de datos incompleta o inválida.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}