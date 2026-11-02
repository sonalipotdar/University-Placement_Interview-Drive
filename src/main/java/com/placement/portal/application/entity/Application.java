package com.placement.portal.application.entity;

public class Application {

    private int appId;
    private String rollNo;
    private int companyId;
    private String interviewStatus;

    public Application() {
    }

    public Application(int appId, String rollNo, int companyId, String interviewStatus) {
        this.appId = appId;
        this.rollNo = rollNo;
        this.companyId = companyId;
        this.interviewStatus = interviewStatus;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    @Override
    public String toString() {
        return "Application{" +
                "appId=" + appId +
                ", rollNo='" + rollNo + '\'' +
                ", companyId=" + companyId +
                ", interviewStatus='" + interviewStatus + '\'' +
                '}';
    }
}
