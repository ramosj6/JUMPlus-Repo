package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExpenseDao {
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;

    public List<Expense> getAllExpenses(User user);

    public List<Expense> getFiveUpcomingExpenses(User user);

    public Optional<Expense> getExpenseById(int id, User user);

    public boolean createExpense(Expense movie);

    public boolean deleteExpense(int id, User user);

}
