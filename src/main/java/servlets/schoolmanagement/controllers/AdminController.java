package servlets.schoolmanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.models.entity.Admin;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.services.RequestService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/adminDashboard/{id}")
public class AdminController {

    private final RequestService requestService;

    public AdminController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public String showPendingRequests(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {

        System.out.println(id);

        if (!Admin.getAdmin().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("flashError", "Accès refusé : Vous n'êtes pas administrateur.");
            return "redirect:/noaccess";
        }

        else

        model.addAttribute("pendingTeacherRequests", requestService.getPendingTeacherRequests());


        model.addAttribute("pendingStudentRequests",  requestService.getPendingStudentRequests());

        return "admin/requests"; // Chemin vers votre JSP
    }



    @PostMapping("/approve/{requestId}")
    public String approveRequest(@PathVariable String requestId) {
        requestService.approveRegistrationRequest(requestId);
        return "redirect:/admin/requests"; // Redirige vers la liste des demandes
    }

    @PostMapping("/reject/{requestId}")
    public String rejectRequest(@PathVariable String requestId) {
        requestService.rejectRegistrationRequest(requestId);
        return "redirect:/admin/requests"; // Redirige vers la liste des demandes
    }
}
