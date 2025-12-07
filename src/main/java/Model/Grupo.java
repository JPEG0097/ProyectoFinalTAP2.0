package Model;

public class Grupo {
    private int idGrupo;
    private String nombreGrupo;
    private int semestre;

    public Grupo() {}

    public Grupo(int idGrupo, String nombreGrupo, int semestre) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.semestre = semestre;
    }

    public Grupo(String nombreGrupo, int semestre) {
        this.nombreGrupo = nombreGrupo;
        this.semestre = semestre;
    }

    // Getters y Setters...
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }
    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    @Override
    public String toString() { return nombreGrupo + " (" + semestre + "°)"; }
}