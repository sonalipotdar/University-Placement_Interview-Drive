package com.placement.portal.application.service;

import com.placement.portal.application.entity.Application;
import com.placement.portal.application.repository.ApplicationRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private static final int MAX_ROLL_NO_LENGTH = 30;
    private static final int MAX_STATUS_LENGTH = 30;

    private static final List<String> ALLOWED_STATUSES = List.of(
            "APPLIED",
            "SHORTLISTED",
            "INTERVIEWING",
            "OFFERED",
            "REJECTED"
    );

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // Submit a new application
    public Application submitApplication(String rollNo, int companyId, String interviewStatus) {

        String normalizedRollNo = rollNo == null ? null : rollNo.trim();
        String normalizedStatus = interviewStatus == null
                ? null
                : interviewStatus.trim().toUpperCase();

        validateSubmission(normalizedRollNo, companyId, normalizedStatus);

        try {
            if (!applicationRepository.studentExists(normalizedRollNo)) {
                throw new IllegalArgumentException(
                        "Student does not exist: " + normalizedRollNo
                );
            }

            if (!applicationRepository.companyExists(companyId)) {
                throw new IllegalArgumentException(
                        "Company does not exist: " + companyId
                );
            }

            if (applicationRepository.applicationExists(normalizedRollNo, companyId)) {
                throw new IllegalArgumentException(
                        "The student has already applied to this company."
                );
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
            throw new IllegalStateException(
                    "Unable to submit application due to a database error.",
                    exception
            );
        }
    }

    // Get all applications
    public List<Application> getAllApplications() {
        try {
            return applicationRepository.findAll();

        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve applications due to a database error.",
                    exception
            );
        }
    }

    // Get applications by student roll number
    public List<Application> getApplicationsByStudent(String rollNo) {

        String normalizedRollNo = rollNo == null ? null : rollNo.trim();

        if (normalizedRollNo == null || normalizedRollNo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Student roll number must not be blank."
            );
        }

        try {
            if (!applicationRepository.studentExists(normalizedRollNo)) {
                throw new IllegalArgumentException(
                        "Student does not exist: " + normalizedRollNo
                );
            }

            return applicationRepository.findByStudentRollNo(normalizedRollNo);

        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve applications for student "
                            + normalizedRollNo + " due to a database error.",
                    exception
            );
        }
    }

    // Get application by ID
    public Optional<Application> getApplicationById(int appId) {

        if (appId <= 0) {
            throw new IllegalArgumentException(
                    "Application ID must be positive."
            );
        }

        try {
            return applicationRepository.findById(appId);

        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve application " + appId
                            + " due to a database error.",
                    exception
            );
        }
    }

    // Update application status and handle placement logic
    @Transactional
    public void updateApplicationStatus(int appId, String interviewStatus) {

        String normalizedStatus = interviewStatus == null
                ? null
                : interviewStatus.trim().toUpperCase();

        if (appId <= 0) {
            throw new IllegalArgumentException(
                    "Application ID must be positive."
            );
        }

        if (normalizedStatus == null || normalizedStatus.isEmpty()) {
            throw new IllegalArgumentException(
                    "Interview status must not be blank."
            );
        }

        if (normalizedStatus.length() > MAX_STATUS_LENGTH) {
            throw new IllegalArgumentException(
                    "Interview status must not exceed 30 characters."
            );
        }

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            throw new IllegalArgumentException(
                    "Invalid interview status: " + normalizedStatus
            );
        }

        try {
            // Find the application
            Application application = applicationRepository.findById(appId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Application does not exist: " + appId
                            )
                    );

            // Update application status
            boolean updated = applicationRepository.updateStatus(
                    appId,
                    normalizedStatus
            );

            if (!updated) {
                throw new IllegalArgumentException(
                        "Unable to update application: " + appId
                );
            }

            // Perform placement cascade when status becomes OFFERED
            if ("OFFERED".equals(normalizedStatus)) {

                String rollNo = application.getRollNo();
                int companyId = application.getCompanyId();

                // Get the package offered by the selected company
                double offeredPackage =
                        applicationRepository.getCompanyPackage(companyId);

                // Mark the student as placed
                applicationRepository.markStudentPlaced(rollNo);

                // Cancel pending applications for lower packages
                int cancelledCount =
                        applicationRepository.cancelLowerPackageApplications(
                                rollNo,
                                appId,
                                offeredPackage
                        );

                System.out.println(
                        "Student " + rollNo + " is now PLACED."
                );

                System.out.println(
                        cancelledCount + " lower-package applications cancelled."
                );
            }

        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "Unable to update application status due to a database error.",
                    exception
            );
        }
    }

    // Validate application submission
    private void validateSubmission(
            String rollNo,
            int companyId,
            String interviewStatus
    ) {

        if (rollNo == null || rollNo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Student roll number must not be blank."
            );
        }

        if (rollNo.length() > MAX_ROLL_NO_LENGTH) {
            throw new IllegalArgumentException(
                    "Student roll number must not exceed 30 characters."
            );
        }

        if (companyId <= 0) {
            throw new IllegalArgumentException(
                    "Company ID must be positive."
            );
        }

        if (interviewStatus == null || interviewStatus.isEmpty()) {
            throw new IllegalArgumentException(
                    "Interview status must not be blank."
            );
        }

        if (interviewStatus.length() > MAX_STATUS_LENGTH) {
            throw new IllegalArgumentException(
                    "Interview status must not exceed 30 characters."
            );
        }

        if (!ALLOWED_STATUSES.contains(interviewStatus)) {
            throw new IllegalArgumentException(
                    "Invalid interview status: " + interviewStatus
            );
        }
    }
}