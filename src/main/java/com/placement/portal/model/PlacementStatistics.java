
package com.placement.portal.model;

public class PlacementStatistics {

    private int totalStudents;
    private int totalCompanies;
    private int totalPlacedStudents;
    private double placementPercentage;
    private double averagePackage;
    private int totalOffers;

    public PlacementStatistics(
            int totalStudents,
            int totalCompanies,
            int totalPlacedStudents,
            double placementPercentage,
            double averagePackage,
            int totalOffers) {

        this.totalStudents = totalStudents;
        this.totalCompanies = totalCompanies;
        this.totalPlacedStudents = totalPlacedStudents;
        this.placementPercentage = placementPercentage;
        this.averagePackage = averagePackage;
        this.totalOffers = totalOffers;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public int getTotalCompanies() {
        return totalCompanies;
    }

    public int getTotalPlacedStudents() {
        return totalPlacedStudents;
    }

    public double getPlacementPercentage() {
        return placementPercentage;
    }

    public double getAveragePackage() {
        return averagePackage;
    }

    public int getTotalOffers() {
        return totalOffers;
    }
}
