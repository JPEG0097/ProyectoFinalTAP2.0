package Model;

public class Docente {
    private int idDocente;
    private String nombre;

    public Docente() {}
    public Docente(String nombre) { this.nombre = nombre; }

    // Getters y Setters
    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}
