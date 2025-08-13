package Service;


import Dao.GenericDAO;
import Models.Domicilio;
import java.util.List;


public class DomicilioServiceImpl implements GenericService<Domicilio>{
    // 🔹 Esta clase implementa las operaciones de negocio relacionadas con los domicilios

    // Se usa una interfaz genérica para acceder a la base de datos (DAO)
    private final GenericDAO<Domicilio> domicilioDAO;

    // 🔹 Constructor: recibe el DAO que se va a usar para trabajar con domicilios
    public DomicilioServiceImpl(GenericDAO<Domicilio> domicilioDAO) {
        this.domicilioDAO = domicilioDAO;
    }

    // 🔹 Metodo para insertar un domicilio sin transacción
    @Override
    public void insertar(Domicilio domicilio) throws Exception {
        // Validaciones básicas para evitar datos vacíos
        if(domicilio.getCalle() == null || domicilio.getCalle().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la calle no puede ser nulo");
        }
        if(domicilio.getNumero() == null || domicilio.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El numero de la calle no puede ser nulo");
        }
        System.out.println("➡ Insertando Domicilio: " + domicilio.getCalle());
        // Inserta el domicilio usando el DAO
        domicilioDAO.insertar(domicilio);
    }

    @Override
    public void actualizar(Domicilio entidad) throws Exception {
        domicilioDAO.actualizar(entidad);
    }
    
    // 🔹 Elimina un domicilio según su ID
    @Override
    public void eliminar(int id_dom) throws Exception {
        domicilioDAO.eliminar(id_dom);
    }
    
    // 🔹 Busca un domicilio por ID
    @Override
    public Domicilio getById(int id) throws Exception {
        return domicilioDAO.getById(id);
    }
    
    // 🔹 Devuelve todos los domicilios registrados
    @Override
    public List<Domicilio> getAll() throws Exception {
        return domicilioDAO.getAll();
    }
    
    @Override
    public void recuperar(int id) throws Exception {
        domicilioDAO.recuperar(id);
    }
}