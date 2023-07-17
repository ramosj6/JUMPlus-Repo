package com.cognixia.jumplus.dao;

import com.cognixia.jumplus.connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseDaoSql implements ExpenseDao {

    private Connection conn;

    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        conn = ConnectionManager.getConnection();
    }

    @Override
    public List<Expense> getAllExpenses(User user) {
        List<Expense> expenses = new ArrayList<>();
        String query = "select * from expense where user_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();
            // rs.next() will return false if nothing found
            while(rs.next()){
                int id = rs.getInt("expense_id");
                int userId = rs.getInt("user_id");
                String category = rs.getString("category");
                LocalDate date = LocalDate.parse(rs.getString("initial_date"));
                double amount = rs.getDouble("amount");
                boolean recurring = rs.getBoolean("recurring");

                Expense expense = new Expense(id, userId, category, date, amount, recurring);

                //adding the expense into the expenses list
                expenses.add(expense);
            }
            rs.close(); // make sure to close or errors will occur

        } catch(SQLException e){
            // uncomment of you're running into issues and want to know what's
            // going on
//				e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public List<Expense> getFiveUpcomingExpenses(User user) {
        List<Expense> expenses = new ArrayList<>();
        String query = "select * from expense where user_id = ? and initial_date >= CURDATE() order by initial_date limit 5";
        try(PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();
            // rs.next() will return false if nothing found
            while(rs.next()){
                int id = rs.getInt("expense_id");
                int userId = rs.getInt("user_id");
                String category = rs.getString("category");
                LocalDate date = LocalDate.parse(rs.getString("initial_date"));
                double amount = rs.getDouble("amount");
                boolean recurring = rs.getBoolean("recurring");

                Expense expense = new Expense(id, userId, category, date, amount, recurring);

                //adding the expense into the expenses list
                expenses.add(expense);
            }
            rs.close(); // make sure to close or errors will occur

        } catch(SQLException e){
            // uncomment of you're running into issues and want to know what's
            // going on
//				e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public Optional<Expense> getExpenseById(int id, User user) {
        try( PreparedStatement pstmt = conn.prepareStatement("select * from expense where expense_id = ? and user = ?") ) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getUserId());

            ResultSet rs = pstmt.executeQuery();

            // rs.next() will return false if nothing found
            // if you enter the if block, that department with that id was found
            if( rs.next() ) {

                int expenseId = rs.getInt("expense_id");
                int userId = rs.getInt("user_id");
                String category = rs.getString("category");
                LocalDate date = LocalDate.parse(rs.getString("initial_date"));
                double amount = rs.getDouble("amount");
                boolean recurring = rs.getBoolean("recurring");

                rs.close();

                // constructing the expense object
                Expense expense = new Expense(expenseId, userId, category, date, amount, recurring);

                // placing it in the optional

                // return the optional
                return Optional.of(expense);

            }
            else {
                rs.close();
                // if you did not find the department with that id, return an empty optional
                return Optional.empty();
            }

        } catch(SQLException e) {
            // just in case an exception occurs, return nothing in the optional
            return Optional.empty();
        }
    }

    @Override
    public boolean createExpense(Expense expense) {
        String query = "insert into expense(user_id, category, initial_date, amount, recurring)" +
                " values (?, ?, ?, ?, ?)";
        try( PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, expense.getUserId());
            pstmt.setString(2, expense.getCategory());
            pstmt.setString(3, expense.getExpenseDate().toString());
            pstmt.setDouble(4, expense.getAmount());
            pstmt.setBoolean(5, expense.isRecurring());

            int count = pstmt.executeUpdate();
            if(count > 0) { // insert happened
                return true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteExpense(int id, User user) {
        String query = "delete from expense where expense_id = ? and user_id = ?";
        try( PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getUserId());

            int count = pstmt.executeUpdate();
            if(count > 0){
                // if it's not deleted
                return true;
            }
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
