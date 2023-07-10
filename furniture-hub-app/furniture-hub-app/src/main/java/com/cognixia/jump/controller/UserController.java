package com.cognixia.jump.controller;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UserExistsException;
import com.cognixia.jump.model.User;
import com.cognixia.jump.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "User", description = "The API for managing Users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Operation(summary="Get all users in user table", description = "Gets all Users in user table from the furniture_db. ")
    @CrossOrigin
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();

        // checks if users list is empty
        if(users.isEmpty()){
            return ResponseEntity.status(200).body("There are currently no users!");
        }

        return ResponseEntity.status(200).body(users);
    }

    // getting the specific user
    @Operation(summary="Get a specific User by id", description="Get the user by id. The ID is created when a new user is registerd. Each user ID is unique.")
    @CrossOrigin
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) throws ResourceNotFoundException {
        User found = userService.getUserById(id);

        return ResponseEntity.status(200).body(found);
    }

    // creating a new user
    @Operation(summary="Create User in user table", description = "Creates a user and inserts them into the furniture_db.")
    @CrossOrigin
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) throws UserExistsException {
        user.setId(null);
        user.setPassword(encoder.encode(user.getPassword()));

        User created = userService.createUser(user);
        return ResponseEntity.status(201).body(created);
    }

}
