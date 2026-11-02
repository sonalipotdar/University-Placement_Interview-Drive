package com.placement.portal.web;

import com.placement.portal.model.Company;
import com.placement.portal.repository.CompanyRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompanyPageController {

    private final CompanyRepository companyRepository;

    public CompanyPageController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/companies-page")
    public String companies(Model model) {
        model.addAttribute("companies",
                companyRepository.getAllCompanies());

        return "companies/list";
    }

    @GetMapping("/companies-page/add")
    public String addCompanyPage(Model model) {
        model.addAttribute("company", new Company());
        return "companies/add";
    }

    @PostMapping("/companies-page/add")
    public String addCompany(
            @ModelAttribute Company company,
            RedirectAttributes redirectAttributes) {

        companyRepository.addCompany(company);

        redirectAttributes.addFlashAttribute(
                "success", "Company added successfully!");

        return "redirect:/companies-page";
    }

    @GetMapping("/companies-page/edit/{companyId}")
    public String editCompanyPage(
            @PathVariable int companyId,
            Model model) {

        Company company = companyRepository
                .getCompanyById(companyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Company not found"));

        model.addAttribute("company", company);

        return "companies/edit";
    }

    @PostMapping("/companies-page/edit/{companyId}")
    public String updateCompany(
            @PathVariable int companyId,
            @ModelAttribute Company company,
            RedirectAttributes redirectAttributes) {

        company.setCompanyId(companyId);
        companyRepository.updateCompany(company);

        redirectAttributes.addFlashAttribute(
                "success", "Company updated successfully!");

        return "redirect:/companies-page";
    }

    @PostMapping("/companies-page/delete/{companyId}")
    public String deleteCompany(
            @PathVariable int companyId,
            RedirectAttributes redirectAttributes) {

        companyRepository.deleteCompany(companyId);

        redirectAttributes.addFlashAttribute(
                "success", "Company deleted successfully!");

        return "redirect:/companies-page";
    }
}