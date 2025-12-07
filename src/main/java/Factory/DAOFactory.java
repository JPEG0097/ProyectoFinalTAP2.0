package Factory;

import DAO.*;

public interface DAOFactory {

    AlumnoDAO createAlumnoDAO();
    UsuarioDAO createUsuarioDAO();
    MateriaDAO createMateriaDAO();
    ParcialDAO createParcialDAO();
    InscripcionDAO createInscripcionDAO();
    DocenteDAO createDocenteDAO();
    GrupoDAO createGrupoDAO();
}