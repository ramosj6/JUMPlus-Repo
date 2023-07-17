package com.cognixia.jumplus.dao;

public class MonthlyBudget {

    private int monthlyId;
    private int userId;
    private int yearlyId;
    private String month;
    private int monthlyGoal;

    public MonthlyBudget(int monthlyId, int userId, int yearlyId, String month, int monthlyGoal) {
        this.monthlyId = monthlyId;
        this.userId = userId;
        this.yearlyId = yearlyId;
        this.month = month;
        this.monthlyGoal = monthlyGoal;
    }

    public int getMonthlyId() {
        return monthlyId;
    }

    public void setMonthlyId(int monthlyId) {
        this.monthlyId = monthlyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getYearlyId() {
        return yearlyId;
    }

    public void setYearlyId(int yearlyId) {
        this.yearlyId = yearlyId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getMonthlyGoal() {
        return monthlyGoal;
    }

    public void setMonthlyGoal(int monthlyGoal) {
        this.monthlyGoal = monthlyGoal;
    }

    @Override
    public String toString() {
        return "MonthlyBudget{" +
                "monthlyId=" + monthlyId +
                ", userId=" + userId +
                ", yearlyId=" + yearlyId +
                ", month='" + month + '\'' +
                ", monthlyGoal=" + monthlyGoal +
                '}';
    }
}
