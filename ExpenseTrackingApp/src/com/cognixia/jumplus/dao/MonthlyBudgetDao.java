package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface MonthlyBudgetDao {

    public boolean getAllMonthlyBudgets(User user);
    public boolean getMonthlyBudgetById(int id, User user);
    public boolean updateMonthlyBudget(int id, int monthlyGoal, User user);
    public boolean createMonthlyBudget(MonthlyBudget mb, User user, int yearId);
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;
}
