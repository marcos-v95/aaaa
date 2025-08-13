package Service;

import java.util.List;

public interface GenericService<T> {
// 🔹 Esta interfaz genérica define las operaciones básicas que deben implementar todos los servicios

    void insertar(T entidad) throws Exception;
//    void insertarTx(T entidad) throws Exception;
    void actualizar(T entidad)throws Exception;
    void eliminar(int id)throws Exception;
    T getById(int id)throws Exception;
    List<T> getAll()throws Exception;
    void recuperar(int id) throws Exception;



}
