package com.placement.portal.application.service;

import com.placement.portal.application.entity.Application;
import com.placement.portal.application.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationServiceTest {

    private FakeApplicationRepository repository;
    private ApplicationService service;

    @BeforeEach
    void setUp() {
        repository = new FakeApplicationRepository();
        service = new ApplicationService(repository);
    }

    @Test
    void submitsApplicationForEligibleStudent() {
        Application application = service.submitApplication("STU001", 7, "APPLIED");

        assertEquals(1, application.getAppId());
        assertEquals("STU001", application.getRollNo());
        assertEquals(7, application.getCompanyId());
        assertEquals("APPLIED", application.getInterviewStatus());
        assertEquals(1, repository.applications.size());
    }

    @Test
    void rejectsDuplicateApplication() {
        service.submitApplication("STU001", 7, "APPLIED");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("STU001", 7, "APPLIED")
        );

        assertTrue(exception.getMessage().contains("already applied"));
        assertEquals(1, repository.applications.size());
    }

    @Test
    void rejectsUnknownStudent() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("UNKNOWN", 7, "APPLIED")
        );

        assertTrue(exception.getMessage().contains("Student does not exist"));
    }

    @Test
    void rejectsUnknownCompany() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("STU001", 99, "APPLIED")
        );

        assertTrue(exception.getMessage().contains("Company does not exist"));
    }

    @Test
    void rejectsIneligibleStudent() {
        repository.eligible = false;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("STU001", 7, "APPLIED")
        );

        assertTrue(exception.getMessage().contains("not eligible"));
    }

    @Test
    void rejectsBlankOrOverlongStatus() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("STU001", 7, " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("STU001", 7, "S".repeat(31))
        );

        service.submitApplication("STU001", 7, "S".repeat(30));
    }

    @Test
    void rejectsOverlongRollNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.submitApplication("R".repeat(31), 7, "APPLIED")
        );
    }

    @Test
    void retrievesApplicationsByStudentAndId() {
        Application submitted = service.submitApplication("STU001", 7, "APPLIED");

        assertEquals(List.of(submitted), service.getAllApplications());
        assertEquals(List.of(submitted), service.getApplicationsByStudent("STU001"));
        assertEquals(Optional.of(submitted), service.getApplicationById(submitted.getAppId()));
        assertFalse(service.getApplicationById(99).isPresent());
    }

    @Test
    void updatesApplicationStatus() {
        Application submitted = service.submitApplication("STU001", 7, "APPLIED");

        service.updateApplicationStatus(submitted.getAppId(), "INTERVIEWED");

        assertEquals("INTERVIEWED", service.getApplicationById(submitted.getAppId())
                .orElseThrow().getInterviewStatus());
    }

    @Test
    void rejectsBlankOrOverlongUpdatedStatus() {
        assertThrows(IllegalArgumentException.class, () -> service.updateApplicationStatus(1, " "));
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateApplicationStatus(1, "S".repeat(31))
        );
    }

    private static class FakeApplicationRepository extends ApplicationRepository {

        private final List<Application> applications = new ArrayList<>();
        private boolean eligible = true;

        private FakeApplicationRepository() {
            super(null);
        }

        @Override
        public Application save(Application application) {
            application.setAppId(applications.size() + 1);
            applications.add(application);
            return application;
        }

        @Override
        public List<Application> findAll() {
            return new ArrayList<>(applications);
        }

        @Override
        public Optional<Application> findById(int appId) {
            return applications.stream().filter(application -> application.getAppId() == appId).findFirst();
        }

        @Override
        public List<Application> findByStudentRollNo(String rollNo) {
            return applications.stream()
                    .filter(application -> application.getRollNo().equals(rollNo))
                    .toList();
        }

        @Override
        public boolean updateStatus(int appId, String interviewStatus) {
            Optional<Application> application = findById(appId);
            application.ifPresent(value -> value.setInterviewStatus(interviewStatus));
            return application.isPresent();
        }

        @Override
        public boolean applicationExists(String rollNo, int companyId) {
            return applications.stream().anyMatch(application ->
                    application.getRollNo().equals(rollNo) && application.getCompanyId() == companyId);
        }

        @Override
        public boolean studentExists(String rollNo) {
            return "STU001".equals(rollNo);
        }

        @Override
        public boolean companyExists(int companyId) {
            return companyId == 7;
        }

        @Override
        public boolean studentIsEligible(String rollNo, int companyId) {
            return eligible;
        }
    }
}
