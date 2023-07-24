package com.cognixia.jumplus.dao;

import com.cognixia.jumplus.connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactDaoSql implements ContactDao {
    private Connection conn;

    @Override
    public List<Contact> getAllContacts(User user) {
        List<Contact> contacts = new ArrayList<>();
        String query = "select * from contacts where user_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();
            // rs.next() will return false if nothing found
            while(rs.next()){
                int id = rs.getInt("contact_id");
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                Contact contact = new Contact(id, userId, name, email, phoneNumber, address);

                //adding the Contact into the expenses list
                contacts.add(contact);
            }
            rs.close(); // make sure to close or errors will occur
        } catch(SQLException e){
            // uncomment of you're running into issues and want to know what's
            // going on
//				e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public List<Contact> getContactsSortByAlphabet(User user){
        List<Contact> sortedContacts = new ArrayList<>();
        String query = "select * from contacts where user_id = ? order by name";
        try(PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();
            // rs.next() will return false if nothing found
            while(rs.next()){
                int id = rs.getInt("contact_id");
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                Contact contact = new Contact(id, userId, name, email, phoneNumber, address);

                //adding the contact into the expenses list
                sortedContacts.add(contact);
            }
            rs.close(); // make sure to close or errors will occur
        } catch(SQLException e){
            // uncomment of you're running into issues and want to know what's
            // going on
//				e.printStackTrace();
        }
        return sortedContacts;
    }

    @Override
    public List<Contact> getContactsSortByAlphabetReverse(User user) {
        List<Contact> sortedContacts = new ArrayList<>();
        String query = "select * from contacts where user_id = ? order by name desc";
        try(PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();
            // rs.next() will return false if nothing found
            while(rs.next()){
                int id = rs.getInt("contact_id");
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                Contact contact = new Contact(id, userId, name, email, phoneNumber, address);

                //adding the contact into the expenses list
                sortedContacts.add(contact);
            }
            rs.close(); // make sure to close or errors will occur
        } catch(SQLException e){
            // uncomment of you're running into issues and want to know what's
            // going on
//				e.printStackTrace();
        }
        return sortedContacts;
    }

    @Override
    public Optional<Contact> getContactById(int id, User user) {
        try( PreparedStatement pstmt = conn.prepareStatement("select * from contacts where contact_id = ? and user_id = ?") ) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getUserId());

            ResultSet rs = pstmt.executeQuery();

            // rs.next() will return false if nothing found
            // if you enter the if block, that department with that id was found
            if( rs.next() ) {
                int contactId = rs.getInt("contact_id");
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                rs.close();

                // constructing the contact object
                Contact contact = new Contact(contactId, userId, name, email, phoneNumber, address);

                // placing it in the optional

                // return the optional
                return Optional.of(contact);

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
    public boolean addContact(Contact contact) {
        String query = "insert into contacts(user_id, name, email, phone_number, address)" +
                " values (?, ?, ?, ?, ?)";
        try( PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, contact.getUserId());
            pstmt.setString(2, contact.getName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getPhoneNumber());
            pstmt.setString(5, contact.getAddress());

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
    public boolean deleteContact(int id, User user) {
        String query = "delete from contacts where contact_id = ? and user_id = ?";
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

    @Override
    public boolean updateContact(Contact contact) {
        try( PreparedStatement pstmt = conn.prepareStatement("update contacts set name = ?, email = ?, phone_number = ?, address = ?" +
                " where contact_id = ? and user_id = ?");){
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getPhoneNumber());
            pstmt.setString(4, contact.getAddress());
            pstmt.setInt(5, contact.getContactId());
            pstmt.setInt(6, contact.getUserId());

            int count = pstmt.executeUpdate();
            if(count > 0) {
                return true; // update executed
            }
        } catch(SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        conn = ConnectionManager.getConnection();
    }
}
