package Models;

public abstract class Base {
    private  int id; // Identificador único
    private Boolean eliminado; // Marca en nuestra base de datos cuando un elemento fue eliminado sin borrarlo de ella

    public Base(int id, Boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Base() {}    

}
