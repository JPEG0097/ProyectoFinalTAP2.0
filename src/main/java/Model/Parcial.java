package Model;

public class Parcial {
    private int idParcial;
    private int idInscripcion;
    private int numeroParcial; // 1, 2, 3, o 4
    private double calificacionParcial;

    public Parcial() {}
    public Parcial(int idInscripcion, int numeroParcial, double calificacionParcial) {
        this.idInscripcion = idInscripcion;
        this.numeroParcial = numeroParcial;
        this.calificacionParcial = calificacionParcial;
    }

    // Getters y Setters...
    public int getIdParcial() { return idParcial; }
    public void setIdParcial(int idParcial) { this.idParcial = idParcial; }
    public int getIdInscripcion() { return idInscripcion; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
    public int getNumeroParcial() { return numeroParcial; }
    public void setNumeroParcial(int numeroParcial) { this.numeroParcial = numeroParcial; }
    public double getCalificacionParcial() { return calificacionParcial; }
    public void setCalificacionParcial(double calificacionParcial) { this.calificacionParcial = calificacionParcial; }
}