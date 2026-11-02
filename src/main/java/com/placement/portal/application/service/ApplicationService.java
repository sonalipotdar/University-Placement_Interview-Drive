package com.placement.portal.application.service;

import com.placement.portal.application.entity.Application;
import com.placement.portal.application.repository.ApplicationRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private static final int MAX_ROLL_NO_LENGTH = 30;
    private static final int MAX_STATUS_LENGTH = 30;

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application submitApplication(String rollNo, int companyId, String interviewStatus) {
        String normalizedRollNo = rollNo == null ? null : rollNo.trim();
        String normalizedStatus = interviewStatus == null ? null : interviewStatus.trim();
        validateSubmission(normalizedRollNo, companyId, normalizedStatus);

        try {
            if (!applicationRepository.studentExists(normalizedRollNo)) {
                throw new IllegalArgumentException("Student does not exist: " + normalizedRollNo);
            }
            if (!applicationRepository.companyExists(companyId)) {
                throw new IllegalArgumentException("Company does not exist: " + companyId);
            }
            if (applicationRepository.applicationExists(normalizedRollNo, companyId)) {
                throw new IllegalArgumentException("The student has already applied to this company.");
            }
            if (!applicationRepository.studentIsEligible(normalizedRollNo, companyId)) {
                throw new IllegalArgumentException(
                        "Student is not eligible for this company based on placement status, CGPA, or backlogs."
                );
            }

            return applicationRepository.save(
                    new Application(0, normalizedRollNo, companyId, normalizedStatus)
            );
        } catch (DuplicateKeyException exception) {
            throw new IllegalArgumentException(
                    "The student has already applied to this company.",
                    exception
            );
        } catch (DataAccessException exception) {
            throw new IllegalStateException("Unable to submit application due to a database error.", exception);
        }
    }

    public List<Application> getAllApplications() {
        try {
            return applicationRepository.findAll();
        } catch (DataAccessException exception) {
            throw new IllegalStateException("Unable to retrieve applications due to a database error.", exception);
        }
    }

    public List<Application> getApplicationsByStudent(String rollNo) {
        String normalizedRollNo = rollNo == null ? null : rollNo.trim();
        if (normalizedRollNo == null || normalizedRollNo.isEmpty()) {
            throw new IllegalArgumentException("Student roll number must not be blank.");
        }

        try {
            if (!applicationRepository.studentExists(normalizedRollNo)) {
                throw new IllegalArgumentException("Student does not exist: " + normalizedRollNo);
            }
            return applicationRepository.findByStudentRollNo(normalizedRollNo);
        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve applications for student " + normalizedRollNo + " due to a database error.",
                    exception
            );
        }
    }

    public Optional<Application> getApplicationById(int appId) {
        if (appId <= 0) {
            throw new IllegalArgumentException("Application ID must be positive.");
        }

        try {
            return applicationRepository.findById(appId);
        } catch (DataAccessException exception) {
            throw new IllegalStateException("Unable to retrieve application " + appId + " due to a database error.",
                    exception);
        }
    }

    public void updateApplicationStatus(int appId, String interviewStatus) {
        String normalizedStatus = interviewStatus == null ? null : interviewStatus.trim();
        if (appId <= 0) {
            throw new IllegalArgumentException("Application ID must be positive.");
        }
        if (normalizedStatus == null || normalizedStatus.isEmpty()) {
            throw new IllegalArgumentException("Interview status must not be blank.");
        }
        if (normalizedStatus.length() > MAX_STATUS_LENGTH) {
            throw new IllegalArgumentException("Interview status must not exceed 30 characters.");
        }

        try {
            boolean updated = applicationRepository.updateStatus(appId, normalizedStatus);
            if (!updated && applicationRepository.findById(appId).isEmpty()) {
                throw new IllegalArgumentException("Application does not exist: " + appId);
            }
        } catch (DataAccessException exception) {
            throw new IllegalStateException("Unable to update application status due to a database error.", exception);
        }
    }

    private void validateSubmission(String rollNo, int companyId, String interviewStatus) {
        if (rollNo == null || rollNo.isEmpty()) {
            throw new IllegalArgumentException("Student roll number must not be blank.");
        }
        if (rollNo.length() > MAX_ROLL_NO_LENGTH) {
            throw new IllegalArgumentException("Student roll number must not exceed 30 characters.");
        }
        if (companyId <= 0) {
            throw new IllegalArgumentException("Company ID must be positive.");
        }
        if (interviewStatus == null || interviewStatus.isEmpty()) {
            throw new IllegalArgumentException("Interview status must not be blank.");
        }
        if (interviewStatus.length() > MAX_STATUS_LENGTH) {
            throw new IllegalArgumentException("Interview status must not exceed 30 characters.");
        }
    }
}
