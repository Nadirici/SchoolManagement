package servlets.schoolmanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.models.entity.Admin;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.services.RequestService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RequestService requestService;

    public AdminController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/dashboard/{id}/requests")
    public String showPendingRequests(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Admin ID: " + id);

        // Vérifiez si l'utilisateur a les droits d'accès
        if (!isAdmin(id)) {
            redirectAttributes.addFlashAttribute("flashError", "Accès refusé : Vous n'êtes pas administrateur.");
            return "noaccess";
        }

        // Récupérer les demandes en attente
        List<RegistrationRequest> pendingTeacherRequests = requestService.getPendingTeacherRequests();
        List<RegistrationRequest> pendingStudentRequests = requestService.getPendingStudentRequests();

        model.addAttribute("pendingTeacherRequests", pendingTeacherRequests);
        model.addAttribute("pendingStudentRequests", pendingStudentRequests);
        return "admin/requests"; // Chemin vers votre JSP
    }

    @PostMapping("/dashboard/approve/{requestId}")
    public String approveRequest(@PathVariable String requestId, RedirectAttributes redirectAttributes) {
        try {
            requestService.approveRegistrationRequest(requestId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Demande approuvée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashError", "Erreur lors de l'approbation de la demande : " + e.getMessage());
        }
        return "redirect:/admin/dashboard/" + Admin.getAdmin().getId() + "/requests";
    }

    @PostMapping("/dashboard/reject/{requestId}")
    public String rejectRequest(@PathVariable String requestId, RedirectAttributes redirectAttributes) {
        try {
            requestService.rejectRegistrationRequest(requestId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Demande rejetée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashError", "Erreur lors du rejet de la demande : " + e.getMessage());
        }
        return "redirect:/admin/dashboard/" + Admin.getAdmin().getId() + "/requests"; // Redirige vers la liste des demandes
    }

    private boolean isAdmin(String id) {
        return Admin.getAdmin().getId().equals(id);
    }
}
