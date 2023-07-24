package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ContactDao {

    public List<Contact> getAllContacts(User user);

    public List<Contact> getContactsSortByAlphabet(User user);

    public List<Contact> getContactsSortByAlphabetReverse(User user);

    public Optional<Contact> getContactById(int id, User user);

    public boolean addContact(Contact contact);

    public boolean deleteContact(int id, User user);

    public boolean updateContact(Contact contact);

    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;

}
