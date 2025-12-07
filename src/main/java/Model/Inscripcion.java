package Model;

import java.time.LocalDate;
import java.sql.Date;

public class Inscripcion {
    private int idInscripcion;
    private int idAlumno;
    private int idMateria;
    private LocalDate fechaInscripcion;

    public Inscripcion() {}

    public Inscripcion(int idInscripcion, int idAlumno, int idMateria, LocalDate fechaInscripcion) {
        this.idInscripcion = idInscripcion;
        this.idAlumno = idAlumno;
        this.idMateria = idMateria;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Inscripcion(int idAlumno, int idMateria, LocalDate fechaInscripcion) {
        this.idAlumno = idAlumno;
        this.idMateria = idMateria;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y Setters...
    public int getIdInscripcion() { return idInscripcion; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }
    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    @Override
    public String toString() {
        return "A:" + idAlumno + " M:" + idMateria + " (" + fechaInscripcion + ")";
    }
}