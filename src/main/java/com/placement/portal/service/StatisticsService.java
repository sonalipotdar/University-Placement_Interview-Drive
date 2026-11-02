
package com.placement.portal.service;

import com.placement.portal.model.PlacementStatistics;
import com.placement.portal.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    // Constructor injection
    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    // Generate placement statistics
    public PlacementStatistics getStatistics() {

        // Get total students
        int totalStudents =
                statisticsRepository.getTotalStudents();

        // Get total companies
        int totalCompanies =
                statisticsRepository.getTotalCompanies();

        // Get total placed students
        int totalPlacedStudents =
                statisticsRepository.getTotalPlacedStudents();

        // Get total offers
        int totalOffers =
                statisticsRepository.getTotalOffers();

        // Get average package
        double averagePackage =
                statisticsRepository.getAveragePackage();

        // Calculate placement percentage
        double placementPercentage = 0.0;

        if (totalStudents > 0) {
            placementPercentage =
                    (totalPlacedStudents * 100.0) / totalStudents;
        }

        // Return all statistics
        return new PlacementStatistics(
                totalStudents,
                totalCompanies,
                totalPlacedStudents,
                placementPercentage,
                averagePackage,
                totalOffers
        );
    }
}
