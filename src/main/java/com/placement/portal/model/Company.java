
package com.placement.portal.model;

public class Company {

    private int companyId;
    private String companyName;
    private double minCgpa;
    private int maxActiveBacklogs;
    private double packageOffered;
    private String jobRole;

    // Default constructor
    public Company() {
    }

    // Parameterized constructor
    public Company(int companyId, String companyName,
                   double minCgpa, int maxActiveBacklogs,
                   double packageOffered, String jobRole) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.minCgpa = minCgpa;
        this.maxActiveBacklogs = maxActiveBacklogs;
        this.packageOffered = packageOffered;
        this.jobRole = jobRole;
    }

    // Getters and Setters

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMaxActiveBacklogs() {
        return maxActiveBacklogs;
    }

    public void setMaxActiveBacklogs(int maxActiveBacklogs) {
        this.maxActiveBacklogs = maxActiveBacklogs;
    }

    public double getPackageOffered() {
        return packageOffered;
    }

    public void setPackageOffered(double packageOffered) {
        this.packageOffered = packageOffered;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", minCgpa=" + minCgpa +
                ", maxActiveBacklogs=" + maxActiveBacklogs +
                ", packageOffered=" + packageOffered +
                ", jobRole='" + jobRole + '\'' +
                '}';
    }
}
