
package com.placement.portal.model;

public class Company {

    private int companyId;
    private String name;
    private double minCgpa;
    private int maxBacklogs;
    private double packageLpa;

    // Default constructor
    public Company() {
    }

    // Parameterized constructor
    public Company(int companyId, String name,
                   double minCgpa, int maxBacklogs,
                   double packageLpa) {

        this.companyId = companyId;
        this.name = name;
        this.minCgpa = minCgpa;
        this.maxBacklogs = maxBacklogs;
        this.packageLpa = packageLpa;
    }

    // Getters and Setters

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMaxBacklogs() {
        return maxBacklogs;
    }

    public void setMaxBacklogs(int maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    public double getPackageLpa() {
        return packageLpa;
    }

    public void setPackageLpa(double packageLpa) {
        this.packageLpa = packageLpa;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", name='" + name + '\'' +
                ", minCgpa=" + minCgpa +
                ", maxBacklogs=" + maxBacklogs +
                ", packageLpa=" + packageLpa +
                '}';
    }
}
