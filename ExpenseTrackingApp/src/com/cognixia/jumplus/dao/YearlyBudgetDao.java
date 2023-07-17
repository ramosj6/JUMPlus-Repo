package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface YearlyBudgetDao {
    public boolean getAllYearlyBudgets(User user);
    public boolean getYearlyBudgetById(int id, User user);
    public boolean updateYearlyBudget(int id, int yearlyGoal, User user);
    public boolean createYearlyBudget(YearlyBudget yb, User user);
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;

}
