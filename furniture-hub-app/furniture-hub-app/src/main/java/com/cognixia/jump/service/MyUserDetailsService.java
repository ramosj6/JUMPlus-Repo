//package com.cognixia.jump.service;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.cognixia.jump.repository.UserRepository;
//
//import com.cognixia.jump.model.User;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//	@Autowired
//	UserRepository repo;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		Optional<User> userFound = repo.findByUsername(username);
//
//		// if username doesn't exist in table, throw an exception
//		if(userFound.isEmpty()) {
//			throw new UsernameNotFoundException(username);
//		}
//
//		return new MyUserDetails( userFound.get() );
//	}
//
//}
