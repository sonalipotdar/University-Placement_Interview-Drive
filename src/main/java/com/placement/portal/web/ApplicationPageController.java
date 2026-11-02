
package com.placement.portal.web;

import com.placement.portal.application.entity.Application;
import com.placement.portal.application.service.ApplicationService;
import com.placement.portal.model.Student;
import com.placement.portal.model.Company;
import com.placement.portal.repository.StudentRepository;
import com.placement.portal.repository.CompanyRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ApplicationPageController {

    private final ApplicationService applicationService;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public ApplicationPageController(
            ApplicationService applicationService,
            StudentRepository studentRepository,
            CompanyRepository companyRepository) {

        this.applicationService = applicationService;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
    }

    // View all applications
    @GetMapping("/applications-page")
    public String applications(Model model) {
        model.addAttribute(
                "applications",
                applicationService.getAllApplications()
        );
        return "applications/list";
    }

    // Open application form
    @GetMapping("/applications-page/add")
    public String addApplicationPage(Model model) {

        model.addAttribute("students",
                studentRepository.getAllStudents());

        model.addAttribute("companies",
                companyRepository.getAllCompanies());

        model.addAttribute("application", new Application());

        return "applications/add";
    }

    // Submit application
    @PostMapping("/applications-page/add")
    public String submitApplication(
            @RequestParam String rollNo,
            @RequestParam int companyId,
            RedirectAttributes redirectAttributes) {

        try {
            applicationService.submitApplication(
                    rollNo,
                    companyId,
                    "APPLIED"
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Application submitted successfully!"
            );

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage()
            );
        }

        return "redirect:/applications-page";
    }

    // Search applications by student
    @GetMapping("/applications-page/student")
    public String applicationsByStudent(
            @RequestParam String rollNo,
            Model model) {

        model.addAttribute(
                "applications",
                applicationService.getApplicationsByStudent(rollNo)
        );

        model.addAttribute("searchRollNo", rollNo);

        return "applications/list";
    }

    // Update interview status
    @PostMapping("/applications-page/update-status/{appId}")
    public String updateStatus(
            @PathVariable int appId,
            @RequestParam String interviewStatus,
            RedirectAttributes redirectAttributes) {

        try {
            applicationService.updateApplicationStatus(
                    appId,
                    interviewStatus
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Application status updated successfully!"
            );

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage()
            );
        }

        return "redirect:/applications-page";
    }
}
