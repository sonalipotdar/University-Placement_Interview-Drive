package com.placement.portal.model;

public class Student {

    private String rollNo;
    private String name;
    private double cgpa;
    private int activeBacklogs;
    private String placementStatus;

    public Student() {
    }

    public Student(String rollNo, String name, double cgpa,
                   int activeBacklogs, String placementStatus) {
        this.rollNo = rollNo;
        this.name = name;
        this.cgpa = cgpa;
        this.activeBacklogs = activeBacklogs;
        this.placementStatus = placementStatus;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public int getActiveBacklogs() {
        return activeBacklogs;
    }

    public void setActiveBacklogs(int activeBacklogs) {
        this.activeBacklogs = activeBacklogs;
    }

    public String getPlacementStatus() {
        return placementStatus;
    }

    public void setPlacementStatus(String placementStatus) {
        this.placementStatus = placementStatus;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNo='" + rollNo + '\'' +
                ", name='" + name + '\'' +
                ", cgpa=" + cgpa +
                ", activeBacklogs=" + activeBacklogs +
                ", placementStatus='" + placementStatus + '\'' +
                '}';
    }
}
