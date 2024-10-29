package servlets.schoolmanagement;

import lombok.Getter;
import models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.StudentService;

import java.sql.Date;

@Controller
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private final StudentService studentService;

    public RegistrationController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/soumettreInscription")
    public String soumettreInscription(@RequestParam String userType,
                                       @RequestParam String email,
                                       @RequestParam String lastname,
                                       @RequestParam String firstname,
                                       @RequestParam String date_of_birth,
                                       @RequestParam String password,
                                       Model model) {

        try {
            Date dateOfBirth = Date.valueOf(date_of_birth);

            if ("student".equals(userType)) {
                Student student = new Student(firstname, lastname, dateOfBirth, email, password);
                studentService.registerStudent(student);
                model.addAttribute("message", "Inscription réussie !");
            } else if ("teacher".equals(userType)) {
                // Logique pour enregistrer un professeur
                model.addAttribute("message", "Inscription du professeur réussie !");
            } else {
                model.addAttribute("errorMessage", "Type d'utilisateur invalide.");
            }

            return "index"; // Renvoie à index.jsp

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'inscription : " + e.getMessage());
            return "index"; // Renvoie à index.jsp en cas d'erreur
        }
    }
}
