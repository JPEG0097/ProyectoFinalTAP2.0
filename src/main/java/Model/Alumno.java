package Model;

public class Alumno {
    private int idAlumno;
    private String nombre;
    private String matricula;
    private int idGrupo;
    private int idUsuario;

    public Alumno() {}

    // Constructor que acepta solo 2 argumentos (para casos donde no se necesita el grupo/usuario)
    public Alumno(String nombre, String matricula) {
        this.nombre = nombre;
        this.matricula = matricula;
    }

    // Constructor para REGISTRO COMPLETO (4 argumentos: el que el Controller necesita)
    public Alumno(String nombre, String matricula, int idGrupo, int idUsuario) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.idGrupo = idGrupo;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters...
    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() { return nombre + " (" + matricula + ")"; }
}