package servlets.schoolmanagement;

import DAO.StudentDAO;
import models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.StudentService;

import java.sql.Date;

@Controller
@RequestMapping("/inscription")
public class RegistrationController {

    private final StudentService studentService;

    @Autowired
    public RegistrationController(StudentService studentService) {
        this.studentService = studentService; // Injection de dépendance
    }

    // Classe pour encapsuler les données d'inscription
    public static class RegistrationForm {
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private String dateOfBirth;
        private String userType;

        // Getters et setters
        public String getFirstname() { return firstname; }
        public void setFirstname(String firstname) { this.firstname = firstname; }

        public String getLastname() { return lastname; }
        public void setLastname(String lastname) { this.lastname = lastname; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }

    @PostMapping("/soumettreInscription")
    public String soumettreInscription(
            @ModelAttribute RegistrationForm form,
            Model model) {

        // Ajoutez des journaux pour chaque paramètre
        System.out.println("FirstName: " + form.getFirstname());
        System.out.println("LastName: " + form.getLastname());
        System.out.println("Email: " + form.getEmail());
        System.out.println("UserType: " + form.getUserType());

        try {
            // Convertir la date de naissance en objet Date
            Date dateOfBirth = Date.valueOf(form.getDateOfBirth());

            // Créer un nouvel étudiant
            if ("student".equals(form.getUserType())) {
                Student student = new Student(form.getFirstname(), form.getLastname(), dateOfBirth, form.getEmail(), form.getPassword());
                // Enregistrer l'étudiant dans la base de données
                studentService.registerStudent(student);
            } else if ("teacher".equals(form.getUserType())) {
                // Logique pour enregistrer un professeur, si nécessaire
                // Vous pouvez créer une classe Teacher et l'enregistrer ici
            }

            // Ajouter un attribut à la vue
            model.addAttribute("message", "Inscription réussie !");
            return "test"; // Redirigez vers une page de succès
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'inscription : " + e.getMessage());
            return "index"; // Renvoyer à la page d'inscription
        }
    }
}
