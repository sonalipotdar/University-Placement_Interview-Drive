
package com.placement.portal.controller;

import com.placement.portal.model.Company;
import com.placement.portal.model.Student;
import com.placement.portal.repository.CompanyRepository;
import com.placement.portal.repository.StudentRepository;
import com.placement.portal.service.EligibilityService;
import com.placement.portal.service.StatisticsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlacementController {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final EligibilityService eligibilityService;
    private final StatisticsService statisticsService;

    public PlacementController(
            StudentRepository studentRepository,
            CompanyRepository companyRepository,
            EligibilityService eligibilityService,
            StatisticsService statisticsService) {

        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.eligibilityService = eligibilityService;
        this.statisticsService = statisticsService;
    }

    // Check student eligibility for a company
    @GetMapping("/eligibility/{rollNo}/{companyId}")
    public ResponseEntity<?> checkEligibility(
            @PathVariable String rollNo,
            @PathVariable int companyId) {

        Student student = studentRepository
                .getStudentByRollNo(rollNo)
                .orElse(null);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        Company company = companyRepository
                .getCompanyById(companyId)
                .orElse(null);

        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        boolean eligible = eligibilityService.isEligible(
                student,
                company.getMinCgpa(),
                company.getMaxActiveBacklogs()
        );

        return ResponseEntity.ok(
                new EligibilityResponse(
                        rollNo,
                        companyId,
                        eligible,
                        eligible ? "Student is eligible"
                                 : "Student is not eligible"
                )
        );
    }

    // Get placement statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    public record EligibilityResponse(
            String rollNo,
            int companyId,
            boolean eligible,
            String message
    ) {
    }
}
