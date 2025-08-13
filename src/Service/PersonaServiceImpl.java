package Service;

import Dao.PersonaDAO;
import Models.Persona;
import java.util.List;

// 🔹 Esta clase implementa las operaciones de negocio relacionadas con personas
public class PersonaServiceImpl implements GenericService<Persona> {

    private final PersonaDAO personaDAO;
    private final DomicilioServiceImpl domicilioServiceImpl;
    

    // 🔹 Constructor que recibe los objetos DAO para persona y domicilio
    public PersonaServiceImpl(PersonaDAO personaDAO, DomicilioServiceImpl domicilioServiceImpl) {
        this.personaDAO = personaDAO;
        this.domicilioServiceImpl = domicilioServiceImpl;
    }

    // 🔹 Inserta una persona SIN usar transacción (uso directo del DAO)
    @Override
    public void insertar(Persona persona) throws Exception {
        // Validaciones para asegurarse de que los datos estén completos
        if(persona.getNombre() == null || persona.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la persona no puede ser nulo o vacío");
        }
        if(persona.getApellido() == null || persona.getApellido().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido de la persona no puede ser nulo o vacío");
        }
        if(persona.getDni() == null || persona.getDni().trim().isEmpty()){
            throw new IllegalArgumentException("El DNI de la persona no puede ser nulo o vacío");
        }

       
        
        System.out.println("➡ Insertando Persona: " + persona.getNombre());
        personaDAO.insertar(persona); // Inserta la persona en la base de datos
    }


    // 🔹 Actualiza una persona (solo sus datos, no el domicilio)
    @Override
    public void actualizar(Persona entidad) throws Exception {
        if(entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        if(entidad.getApellido() == null || entidad.getApellido().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido no puede ser vacío");
        }
        if(entidad.getDni() == null || entidad.getDni().trim().isEmpty()){
            throw new IllegalArgumentException("El DNI no puede ser vacío");
        }
        personaDAO.actualizar(entidad);
    }

    // 🔹 Elimina una persona según su ID
    @Override
    public void eliminar(int id) throws Exception {
        personaDAO.eliminar(id);
    }

    // 🔹 Obtiene una persona por su ID
    @Override
    public Persona getById(int id) throws Exception {
        return personaDAO.getById(id);
    }

    // 🔹 Obtiene todas las personas registradas
    @Override
    public List<Persona> getAll() throws Exception {
        return personaDAO.getAll();
    }

    // 🔹 Devuelve el servicio de domicilio (para usar en el menú o desde otras clases)
   
//Devuelve una lista de personas cuyo nombre o apellido contenga el texto indicado.
    public List<Persona> buscarPorNombreApellido(String filtro) throws Exception {
        return personaDAO.buscarPorNombreApellido(filtro);
    }

    @Override
    public void recuperar(int id) throws Exception {
        personaDAO.recuperar(id);
    }
    
    public DomicilioServiceImpl getDomicilioService() {
        return this.domicilioServiceImpl;
    }

}
