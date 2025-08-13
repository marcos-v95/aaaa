package Config;


import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    // Esta clase sirve para manejar transacciones de forma manual.
    // Una transacción es un bloque de operaciones que se hacen juntas o no se hacen en absoluto.

    private Connection conn;    // Guarda la conexión actual

    public Connection getConn() {
        // Inicia una transacción. Evita que los cambios se guarden automáticamente (AutoCommit = false).
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void starTransaction() throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
            System.out.println("Transacción iniciada. AutoCommit deshabilitado");
        } else {
            throw new SQLException("No se puede iniciar la transacción");
        }
    }

    public void commit() throws SQLException {
        // Confirma los cambios y los guarda en la base de datos.
        if (conn != null) {
            conn.commit();
            System.out.println("Transacción realizada, cambios commiteados");
        } else {
            throw new SQLException("Error al commitear cambios, no hay conexión establecida");
        }
    }

    public void rollback() throws SQLException {
        // Revierte los cambios si ocurrió un error.
        if (conn != null) {
            conn.rollback();
            System.out.println("Transacción cancelada, Rollback");
        } else {
            throw new SQLException("Error de Rollback, no hay conexión establecida");
        }
    }

    public void close() throws SQLException {
        // Cierra la conexión y restablece el AutoCommit.
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
            System.out.println("🔒 Conexión cerrada (desde TransactionManager).");
        }
    }
}
