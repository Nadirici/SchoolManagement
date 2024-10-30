package servlets.schoolmanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.services.RequestService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RequestService requestService;

    public AdminController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public String showPendingRequests(Model model) {


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
