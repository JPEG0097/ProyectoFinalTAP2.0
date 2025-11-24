package Model;

public class Alumno {

    private int idAlumno;
    private String nombre;
    private String matricula;
    private double promedioGeneral;

    public Alumno() {}

    public Alumno(int idAlumno, String nombre, String matricula, double promedioGeneral) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.matricula = matricula;
        this.promedioGeneral = promedioGeneral;
    }

    public Alumno(String nombre, String matricula) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.promedioGeneral = 0;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public double getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(double promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    @Override
    public String toString() {
        return nombre + " (" + matricula + ")";
    }
}
