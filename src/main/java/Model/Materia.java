package Model;

public class Materia {

    private int idMateria;
    private String nombre;
    private int creditos;

    public Materia() {}

    public Materia(int idMateria, String nombre, int creditos) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public Materia(String nombre, int creditos) {
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
