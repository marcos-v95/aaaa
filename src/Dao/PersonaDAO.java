package Dao;


import Config.DatabaseConnection;
import Models.Domicilio;
import Models.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements GenericDAO<Persona>{
    // Similar a DomicilioDAO, pero trabaja con personas y sus posibles domicilios.
   

    @Override
    public void insertar(Persona persona)throws Exception {

        // Inserta una persona en la tabla.
        // También guarda el ID generado y lo asigna al objeto.
        // Si la persona tiene un domicilio, se guarda su ID como clave foránea.
        String sql = "INSERT INTO personas (nombre, apellido, dni, domicilio_id) VALUES (?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getDni());
            if(persona.getDomicilio() != null && persona.getDomicilio().getId() > 0){
                stmt.setInt(4, persona.getDomicilio().getId());
            }else{
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getInt(1));
                    System.out.println("Persona insertada con ID: " + persona.getId());
                } else {
                    throw new SQLException("La inserción de la persona falló, no se obtuvo ID generado.");
                }
            }
        }
    }

 
    @Override
    public void actualizar(Persona persona) throws Exception {
    // Modifica los datos de una persona ya existente.
        String sql = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, domicilio_id = ? WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getDni());
            if(persona.getDomicilio() != null && persona.getDomicilio().getId() > 0){
                ps.setInt(4, persona.getDomicilio().getId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setInt(5, persona.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        // Marca como eliminado una persona por su ID (borrado lógico)
        String sql = "UPDATE personas SET eliminado = TRUE, domicilio_id = NULL  WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo marcar como eliminada la persona con ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public Persona getById(int id) throws Exception {
        // Hace una consulta combinada (JOIN) para obtener la persona y su domicilio asociado.
        // aca se armo bardo porque habia que usar SELECT y JOIN
        String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.domicilio_id, p.eliminado AS persona_eliminado, " +
                "d.id AS dom_id, d.calle, d.numero, d.eliminado AS dom_eliminado  " +
                "FROM personas p LEFT JOIN domicilios d ON p.domicilio_id = d.id " +
                "WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Persona persona = new Persona();
                    persona.setId(rs.getInt("id"));
                    persona.setNombre(rs.getString("nombre"));
                    persona.setApellido(rs.getString("apellido"));
                    persona.setDni(rs.getString("dni"));
                    persona.setEliminado(rs.getBoolean("persona_eliminado"));

                    int domicilioId = rs.getInt("domicilio_id"); // CORREGIDO: 'id_domicilio' a 'domicilio_id'
                    if (domicilioId > 0) {
                        Domicilio domicilio = new Domicilio();
                        domicilio.setId(rs.getInt("dom_id"));
                        domicilio.setCalle(rs.getString("calle"));
                        domicilio.setNumero(rs.getString("numero"));
                        domicilio.setEliminado(rs.getBoolean("dom_eliminado"));
                        persona.setDomicilio(domicilio);
                    } else {
                        persona.setDomicilio(null);
                    }
                    return persona;
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener persona por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override 
public List<Persona> getAll() throws Exception {
    List<Persona> personas = new ArrayList<>();
    String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.domicilio_id, " +
            "d.id AS dom_id, d.calle, d.numero, d.eliminado AS dom_eliminado " +
            "FROM personas p LEFT JOIN domicilios d ON p.domicilio_id = d.id " +
            "WHERE p.eliminado = FALSE ";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Persona persona = new Persona();
            persona.setId(rs.getInt("id")); 
            persona.setNombre(rs.getString("nombre"));
            persona.setApellido(rs.getString("apellido"));
            persona.setDni(rs.getString("dni"));

            int domicilioId = rs.getInt("domicilio_id");
            if (domicilioId > 0) {
                Domicilio domicilio = new Domicilio();
                domicilio.setId(rs.getInt("dom_id"));
                domicilio.setCalle(rs.getString("calle"));
                domicilio.setNumero(rs.getString("numero"));
                domicilio.setEliminado(rs.getBoolean("dom_eliminado"));
                persona.setDomicilio(domicilio);
            } else {
                persona.setDomicilio(null);
            }

            personas.add(persona);
        }
    } catch (SQLException e) {
        throw new Exception("Error al obtener todas las personas: " + e.getMessage(), e);
    }
    return personas;
}

/**
 * Busca personas cuyo nombre o apellido coincida parcialmente con el filtro indicado.
 * Solo se incluyen personas que no estén marcadas como eliminadas.
 */
    public List<Persona> buscarPorNombreApellido(String filtro) throws SQLException {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.domicilio_id, p.eliminado AS persona_eliminado, " +
                "d.id AS dom_id, d.calle, d.numero, d.eliminado AS dom_eliminado " +   
                "FROM personas p LEFT JOIN domicilios d ON p.domicilio_id = d.id " +
                "WHERE p.eliminado = FALSE AND (p.nombre LIKE ? OR p.apellido LIKE ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            stmt.setString(2, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setDni(rs.getString("dni"));
                persona.setEliminado(rs.getBoolean("persona_eliminado"));


                int domicilioId = rs.getInt("domicilio_id");
                if (domicilioId > 0) {
                    Domicilio domicilio = new Domicilio();
                    domicilio.setId(rs.getInt("dom_id"));
                    domicilio.setCalle(rs.getString("calle"));
                    domicilio.setNumero(rs.getString("numero"));
                    domicilio.setEliminado(rs.getBoolean("dom_eliminado"));
                    persona.setDomicilio(domicilio);
                }
                personas.add(persona);
            }
        }
        return personas;
    }
    
    @Override
    public void recuperar(int id)throws SQLException {
        // recupera una persona por ID.
        String sql = "UPDATE personas SET eliminado = FALSE WHERE id = ?;\n";
        try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }

}