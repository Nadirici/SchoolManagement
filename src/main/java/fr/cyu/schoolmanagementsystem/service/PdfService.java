package fr.cyu.schoolmanagementsystem.service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PdfService {

    public byte[] createStudentReport(StudentDTO student, List<CourseDTO> courses,
                                      Map<UUID, String> courseAverages, Map<UUID, Double> minAverages,
                                      Map<UUID, Double> maxAverages,double studentGlobalAverage) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Initialiser le document PDF
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Ajouter le titre
            document.add(new Paragraph("Bulletin de l'étudiant").setBold().setFontSize(18));

            // Ajouter les informations de l'étudiant
            document.add(new Paragraph("Nom : " + student.getFirstname() + " " + student.getLastname()));
            document.add(new Paragraph("Date de naissance : "+student.getDateOfBirth()));
            document.add(new Paragraph("Id étudiant : "+student.getId()));

            document.add(new Paragraph("\n"));

            // Ajouter un tableau avec les informations des cours
            Table table = new Table(new float[]{4, 2, 2, 2});
            table.addHeaderCell(new Cell().add(new Paragraph("Cours").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Ta moyenne").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Moyenne min").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Moyenne max").setBold()));

            for (CourseDTO course : courses) {
                UUID courseId = course.getId();
                String courseAverage = courseAverages.getOrDefault(courseId, "N/A");
                double minAverage = minAverages.getOrDefault(courseId, 0.0);
                double maxAverage = maxAverages.getOrDefault(courseId, 0.0);

                table.addCell(new Cell().add(new Paragraph(course.getName())));
                table.addCell(new Cell().add(new Paragraph(courseAverage)));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", minAverage))));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", maxAverage))));
            }

            document.add(new Paragraph("Moyenne générale : "+studentGlobalAverage));

            document.add(table);

            // Fermer le document
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}
