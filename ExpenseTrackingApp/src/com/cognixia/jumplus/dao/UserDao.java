package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    public Optional<User> validateUser(String username, String password);
    public boolean createUser(User user);
    public Optional<User> getUserById(int id);

    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;
}
