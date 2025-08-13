package Models;

public class Persona extends Base {
    private String nombre;
    private String apellido;
    private String dni;
    private Domicilio domicilio;

    public Persona(int id, String nombre, String apellido, String dni) {
        super(id, false);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public Persona() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + getId() +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", domicilio=" + domicilio +
                ", eliminado=" + isEliminado() +
                '}';
    }
}
