package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {
	
	public Optional<User> validateUser(String username, String password);
	
	public boolean createUser(User user);
	
	public boolean addMovieForRating(User user, int movieId, int rating);
	
	public boolean updateMovieRating(User user, int movieId, int rating);
	
	public int getMovieRating(User user, int movieId);

	public int getMovieRateCount(int movieId);
	
	public List<Integer> getMovieRatings(int movieId);
	
	void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;

}
