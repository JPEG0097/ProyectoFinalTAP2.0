package Model;

public class Calificacion {

    private int idCalificacion;
    private int idAlumno;
    private int idMateria;
    private double calificacionFinal;

    public Calificacion() {}

    public Calificacion(int idCalificacion, int idAlumno, int idMateria, double calificacionFinal) {
        this.idCalificacion = idCalificacion;
        this.idAlumno = idAlumno;
        this.idMateria = idMateria;
        this.calificacionFinal = calificacionFinal;
    }

    public Calificacion(int idAlumno, int idMateria, double calificacionFinal) {
        this.idAlumno = idAlumno;
        this.idMateria = idMateria;
        this.calificacionFinal = calificacionFinal;
    }

    public int getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public double getCalificacionFinal() {
        return calificacionFinal;
    }

    public void setCalificacionFinal(double calificacionFinal) {
        this.calificacionFinal = calificacionFinal;
    }

    @Override
    public String toString() {
        return "Alumno " + idAlumno + " → Materia " + idMateria + " = " + calificacionFinal;
    }
}
