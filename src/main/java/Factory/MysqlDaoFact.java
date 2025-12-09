package Factory;

import DAO.*;

public class MysqlDaoFact implements DAOFactory {

    @Override
    public AlumnoDAO createAlumnoDAO() {
        return new AlumnoDAO();
    }

    @Override
    public UsuarioDAO createUsuarioDAO(){
        return new UsuarioDAO();
    }

    @Override
    public MateriaDAO createMateriaDAO() {
        return new MateriaDAO();
    }

    @Override
    public ParcialDAO createParcialDAO() {
        return new ParcialDAO();
    }

    @Override
    public DocenteDAO createDocenteDAO() {
        return new DocenteDAO();
    }

    @Override
    public InscripcionDAO createInscripcionDAO() {
        return new InscripcionDAO();
    }
    @Override
    public GrupoDAO createGrupoDAO() {
        return new GrupoDAO();
    }
}