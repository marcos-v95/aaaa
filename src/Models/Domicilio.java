package Models;

public class Domicilio extends Base {
    private String calle;
    private String numero;

    public Domicilio(int id, String calle, String numero) {
        super(id, false);
        this.calle = calle;
        this.numero = numero;
    }

    public Domicilio() {
        super();
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Domicilio{" +
                "id=" + getId() +
                ", calle='" + calle + '\'' +
                ", numero='" + numero + '\'' +
                ", eliminado=" + isEliminado() +
                '}';
    }
}
