
package com.placement.portal.controller;

import com.placement.portal.model.Company;
import com.placement.portal.repository.CompanyRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Get all companies
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.getAllCompanies();
    }

    // Get company by ID
    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompanyById(
            @PathVariable int companyId) {

        return companyRepository.getCompanyById(companyId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add company
    @PostMapping
    public ResponseEntity<Company> addCompany(
            @RequestBody Company company) {

        companyRepository.addCompany(company);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(company);
    }

    // Update company
    @PutMapping("/{companyId}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable int companyId,
            @RequestBody Company company) {

        company.setCompanyId(companyId);

        int updated = companyRepository.updateCompany(company);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(company);
    }

    // Delete company
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable int companyId) {

        int deleted = companyRepository.deleteCompany(companyId);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
