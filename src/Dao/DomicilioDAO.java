package Dao;


import Config.DatabaseConnection;
import Models.Domicilio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioDAO implements GenericDAO<Domicilio>{
// Implementa las operaciones necesarias para guardar, actualizar, eliminar y obtener domicilios desde la base de datos.


    @Override
    public void insertar(Domicilio domicilio) throws SQLException {
        // Inserta un nuevo domicilio usando una conexión simple.
        // Prepara la consulta con valores "?" que se llenan con los datos del objeto.
        // Luego recupera el ID generado por la base de datos para asignarlo al objeto.
        String sql = "INSERT INTO domicilios (calle, numero) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumero());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    domicilio.setId(generatedKeys.getInt(1)); // asignamos el id generado al objeto Domicilio
                    System.out.println("Domicilio insertado con ID: " + domicilio.getId());
                } else {
                    throw new SQLException("La inserción del domicilio falló, no se obtuvo ID generado.");
                }
            }
        } 
    }


    @Override
    public void actualizar(Domicilio domicilio) throws SQLException {
        // Modifica los datos de un domicilio ya existente en base a su ID.
        String sql="UPDATE domicilios SET calle=?, numero=? WHERE id=?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumero());
            stmt.setInt(3, domicilio.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id_dom)throws SQLException {
        // Elimina un domicilio por ID.
        String sqlDomicilio = "UPDATE domicilios SET eliminado = TRUE WHERE id = ?;\n";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlDomicilio)){
            stmt.setInt(1, id_dom);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void recuperar(int id)throws SQLException {
        // recupera un domicilio por ID.
        String sql = "UPDATE domicilios SET eliminado = FALSE WHERE id = ?;\n";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }

    @Override
    public Domicilio getById(int id)throws SQLException {
        // Obtiene un domicilio por su ID.
        String sql = "SELECT * FROM domicilios WHERE id=?";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Domicilio(
                        rs.getInt("id"),
                        rs.getString("calle"),
                        rs.getString("numero"));
            }
        }
        return null;
    }

    @Override
    public List<Domicilio> getAll() throws SQLException {
        // Obtiene todos los domicilios guardados en la base de datos.
        String sql = "SELECT * FROM domicilios WHERE eliminado = FALSE";
        try(Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ){
            ResultSet rs = stmt.executeQuery(sql);
            List<Domicilio> domicilios = new ArrayList<Domicilio>();
            while(rs.next()){
                domicilios.add(new Domicilio(
                        rs.getInt("id"),
                        rs.getString("calle"),
                        rs.getString("numero")
                ));
            }
            return domicilios;
        }

    }
}
