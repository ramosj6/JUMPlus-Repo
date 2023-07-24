package com.cognixia.jumplus.dao;

import com.cognixia.jumplus.connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoSql implements UserDao {

    private Connection conn;
    @Override
    public Optional<User> validateUser(String username, String password) {
        try(PreparedStatement pstmt = conn.prepareStatement("select * from user where username = ? and password = ?")){
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if( rs.next() ){
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                String address = rs.getString("address");
                String usrname = rs.getString("username");
                String pass = rs.getString("password");

                rs.close();

                // Creating the User object
                User user = new User(userId, name, email, phone, address, usrname, pass);

                return Optional.of(user);
            } else {
                rs.close();
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.out.println("Cannot verify user due to connection issues");
            //e.printStackTrace();
            return Optional.empty();
        }    }

    @Override
    public boolean createUser(User user) {
        try( PreparedStatement pstmt = conn.prepareStatement("insert into user(name, username, password, email, phone_number, address)"
                + "values (?, ?, ?, ?, ?, ?)");){
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());

            int count = pstmt.executeUpdate();
            if(count > 0) {
                //Success if it's inserted into the table
                return true;
            }

        } catch(SQLException e) {
            // Uncomment if problems occur
//				e.printStackTrace();
            return false;
        }
        return false;    }

    @Override
    public Optional<User> getUserById(int id) {
        try( PreparedStatement pstmt = conn.prepareStatement("select * from user where user_id = ?") ) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            // rs.next() will return false if nothing found
            // if you enter the if block, that department with that id was found
            if( rs.next() ) {

                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                String address = rs.getString("address");
                String username = rs.getString("username");
                String password = rs.getString("password");

                rs.close();

                // constructing the user object
                User user = new User(userId, name, email, phone, address, username, password);

                // return the optional
                return Optional.of(user);

            } else {
                rs.close();
                // if you did not find the department with that id, return an empty optional
                return Optional.empty();
            }

        } catch(SQLException e) {
            // just in case an exception occurs, return nothing in the optional
            return Optional.empty();
        }    }

    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        conn = ConnectionManager.getConnection();
    }
}
