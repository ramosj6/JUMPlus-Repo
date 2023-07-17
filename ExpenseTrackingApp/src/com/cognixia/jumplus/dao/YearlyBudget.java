package com.cognixia.jumplus.dao;

public class YearlyBudget {

    private int yearlyId;
    private int userId;
    private int year;
    private int yearlyGoal;

    public YearlyBudget(int yearlyId, int userId, int year, int yearlyGoal) {
        this.yearlyId = yearlyId;
        this.userId = userId;
        this.year = year;
        this.yearlyGoal = yearlyGoal;
    }

    public int getYearlyId() {
        return yearlyId;
    }

    public void setYearlyId(int yearlyId) {
        this.yearlyId = yearlyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYearlyGoal() {
        return yearlyGoal;
    }

    public void setYearlyGoal(int yearlyGoal) {
        this.yearlyGoal = yearlyGoal;
    }

    @Override
    public String toString() {
        return "YearlyBudget{" +
                "yearlyId=" + yearlyId +
                ", userId=" + userId +
                ", year=" + year +
                ", yearlyGoal=" + yearlyGoal +
                '}';
    }
}
