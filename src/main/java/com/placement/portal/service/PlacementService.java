
package com.placement.portal.service;

import com.placement.portal.model.Student;
import com.placement.portal.model.Company;
import com.placement.portal.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlacementService {

    private final StudentRepository studentRepository;
    private final EligibilityService eligibilityService;

    // Constructor injection
    public PlacementService(StudentRepository studentRepository,
                            EligibilityService eligibilityService) {
        this.studentRepository = studentRepository;
        this.eligibilityService = eligibilityService;
    }

    // 1. Check whether a student is eligible for a company
    public boolean validateStudentEligibility(String rollNo, Company company) {

        if (company == null) {
            return false;
        }

        Optional<Student> optionalStudent =
                studentRepository.getStudentByRollNo(rollNo);

        if (optionalStudent.isEmpty()) {
            return false;
        }

        Student student = optionalStudent.get();

        return eligibilityService.isEligible(
                student,
                company.getMinCgpa(),
                company.getMaxActiveBacklogs()
        );
    }

    // 2. Update student placement status after receiving an offer
    public boolean markStudentAsPlaced(String rollNo) {

        Optional<Student> optionalStudent =
                studentRepository.getStudentByRollNo(rollNo);

        if (optionalStudent.isEmpty()) {
            return false;
        }

        Student student = optionalStudent.get();

        student.setPlacementStatus("PLACED");

        return studentRepository.updateStudent(student) > 0;
    }

    // 3. Check whether a student is already placed
    public boolean isStudentAlreadyPlaced(String rollNo) {

        Optional<Student> optionalStudent =
                studentRepository.getStudentByRollNo(rollNo);

        if (optionalStudent.isEmpty()) {
            return false;
        }

        return "PLACED".equalsIgnoreCase(
                optionalStudent.get().getPlacementStatus()
        );
    }
}
