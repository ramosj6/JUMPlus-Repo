package com.cognixia.jumplus.dao;

import java.time.LocalDate;

// Pojo class for Expense of the user
public class Expense {

    private int expenseId;
    private int userId;
    private String category;
    private LocalDate expenseDate;
    private double amount;
    private boolean recurring;

    public Expense(int expenseId, int userId, String category, LocalDate expenseDate, double amount, boolean recurring) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.category = category;
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.recurring = recurring;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", expenseDate=" + expenseDate +
                ", amount=" + amount +
                ", recurring=" + recurring +
                '}';
    }
}
