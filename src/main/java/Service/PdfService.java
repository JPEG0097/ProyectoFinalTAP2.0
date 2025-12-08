package Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import DAO.InscripcionDAO;
import DAO.MateriaDAO;
import DAO.ParcialDAO;
import Factory.MysqlDaoFact;
import Model.Alumno;
import Model.Inscripcion;
import Model.Materia;
import Model.Parcial;

import java.io.FileOutputStream;
import java.util.List;

public class PdfService {

    private final MysqlDaoFact factory = new MysqlDaoFact();
    private final InscripcionDAO inscripcionDAO = factory.createInscripcionDAO();
    private final MateriaDAO materiaDAO = factory.createMateriaDAO();
    private final ParcialDAO parcialDAO = factory.createParcialDAO();
    private final CalificacionesService calificacionesService = new CalificacionesService();

    public void generarHistorialPDF(Alumno alumno, String rutaArchivo) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
        documento.open();

        Font titulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        documento.add(new Paragraph("Historial Académico", titulo));
        documento.add(new Paragraph("Alumno: " + alumno.getNombre()));
        documento.add(new Paragraph("Matrícula: " + alumno.getMatricula()));
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell("Materia");
        tabla.addCell("Código");
        tabla.addCell("Promedio Final");
        tabla.addCell("Estado");

        List<Inscripcion> inscripciones = inscripcionDAO.getByAlumnoId(alumno.getIdAlumno());

        for (Inscripcion ins : inscripciones) {
            Materia materia = materiaDAO.getById(ins.getIdMateria());

            double promedio = calificacionesService.calcularPromedioMateria(ins.getIdInscripcion());
            String estado = calificacionesService.esMateriaAprobada(promedio) ? "APROBADO" : "REPROBADO";

            tabla.addCell(materia.getNombre());
            tabla.addCell(materia.getCodigoMateria());
            tabla.addCell(String.format("%.2f", promedio));
            tabla.addCell(estado);
        }

        documento.add(tabla);
        documento.close();
    }
}
