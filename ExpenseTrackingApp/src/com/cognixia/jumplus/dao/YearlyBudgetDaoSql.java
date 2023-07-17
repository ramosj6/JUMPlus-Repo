package com.cognixia.jumplus.dao;

import com.cognixia.jumplus.connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class YearlyBudgetDaoSql implements YearlyBudgetDao {
    private Connection conn;
    @Override
    public boolean getAllYearlyBudgets(User user) {
        return false;
    }

    @Override
    public boolean getYearlyBudgetById(int id, User user) {
        return false;
    }

    @Override
    public boolean updateYearlyBudget(int id, int yearlyGoal, User user) {
        return false;
    }

    @Override
    public boolean createYearlyBudget(YearlyBudget yb, User user) {
        return false;
    }

    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        conn = ConnectionManager.getConnection();
    }
}
