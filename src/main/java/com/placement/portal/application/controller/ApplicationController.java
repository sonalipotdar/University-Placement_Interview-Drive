package com.placement.portal.application.controller;

import com.placement.portal.application.entity.Application;
import com.placement.portal.application.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> submitApplication(@RequestBody CreateApplicationRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(new ApiError("Request body must not be null."));
        }
        if (request.companyId() == null) {
            return ResponseEntity.badRequest().body(new ApiError("Company ID must not be null."));
        }

        Application application = applicationService.submitApplication(
                request.rollNo(),
                request.companyId(),
                request.interviewStatus()
        );
        return ResponseEntity.created(URI.create("/applications/" + application.getAppId()))
                .body(application);
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{appId}")
    public ResponseEntity<Application> getApplicationById(@PathVariable int appId) {
        return applicationService.getApplicationById(appId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{rollNo}")
    public ResponseEntity<List<Application>> getApplicationsByStudent(@PathVariable String rollNo) {
        return ResponseEntity.ok(applicationService.getApplicationsByStudent(rollNo));
    }

    @PutMapping("/{appId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable int appId,
            @RequestBody StatusUpdateRequest request
    ) {
        if (request == null) {
            return ResponseEntity.badRequest().body(new ApiError("Request body must not be null."));
        }

        try {
            applicationService.updateApplicationStatus(appId, request.interviewStatus());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            if (exception.getMessage() != null
                    && exception.getMessage().startsWith("Application does not exist:")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(exception.getMessage()));
            }
            throw exception;
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleInvalidInput(IllegalArgumentException exception) {
        String message = exception.getMessage();
        if (message != null && message.startsWith("The student has already applied")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(message));
        }
        if (message != null && (message.startsWith("Student does not exist:")
                || message.startsWith("Company does not exist:"))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(message));
        }
        return ResponseEntity.badRequest().body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleServiceFailure(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(exception.getMessage()));
    }

    public record CreateApplicationRequest(String rollNo, Integer companyId, String interviewStatus) {
    }

    public record StatusUpdateRequest(String interviewStatus) {
    }

    public record ApiError(String message) {
    }
}
