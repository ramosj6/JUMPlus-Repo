package com.cognixia.jump.service;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UserExistsException;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id) throws ResourceNotFoundException {
        Optional<User> found = userRepository.findById(id);

        if(found.isEmpty()){
            throw new ResourceNotFoundException("User", id);
        }

        return found.get();
    }

    public User createUser(User user) throws UserExistsException {
        Optional<User> exists = userRepository.findByUsername(user.getUsername());

        if(exists.isPresent()){
            throw new UserExistsException("User", user.getUsername());
        }

        return userRepository.save(user);
    }

}
