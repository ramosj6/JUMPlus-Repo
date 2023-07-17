package com.cognixia.jumplus.dao;

import com.cognixia.jumplus.connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MonthlyBudgetDaoSql implements MonthlyBudgetDao {
    // Connection used for all methods
    private Connection conn;
    @Override
    public boolean getAllMonthlyBudgets(User user) {
        return false;
    }

    @Override
    public boolean getMonthlyBudgetById(int id, User user) {
        return false;
    }

    @Override
    public boolean updateMonthlyBudget(int id, int monthlyGoal, User user) {
        return false;
    }

    @Override
    public boolean createMonthlyBudget(MonthlyBudget mb, User user, int yearId) {
        return false;
    }

    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        conn = ConnectionManager.getConnection();
    }
}
