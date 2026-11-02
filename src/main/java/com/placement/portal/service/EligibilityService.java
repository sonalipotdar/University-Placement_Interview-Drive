
package com.placement.portal.service;

import com.placement.portal.model.Student;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {

    public boolean isEligible(
            Student student,
            double minimumCgpa,
            int maximumBacklogs) {

        // Check whether student exists
        if (student == null) {
            return false;
        }

        // Check minimum CGPA
        if (student.getCgpa() < minimumCgpa) {
            return false;
        }

        // Check maximum active backlogs
        if (student.getActiveBacklogs() > maximumBacklogs) {
            return false;
        }

        // Check placement status
        if (!"UNPLACED".equalsIgnoreCase(
                student.getPlacementStatus())) {
            return false;
        }

        return true;
    }
}
