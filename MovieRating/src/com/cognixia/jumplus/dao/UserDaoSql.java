package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cognixia.jumplus.connection.ConnectionManager;

public class UserDaoSql implements UserDao{
	
	// Connection used for all methods
	private Connection conn;

	@Override
	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		conn = ConnectionManager.getConnection();
	}

	@Override
	public Optional<User> validateUser(String username, String password) {
		try( PreparedStatement pstmt = conn.prepareStatement("select * from user where username = ? and password = ?");){
			  pstmt.setString(1, username);
	          pstmt.setString(2, password);
	          
	          ResultSet rs = pstmt.executeQuery();
	          
	          if( rs.next() ) {
	        	  int userId = rs.getInt("user_id");
	        	  String firstName = rs.getString("first_name");
	        	  String lastName = rs.getString("last_name");
	        	  String email = rs.getString("email");
	        	  String usrname = rs.getString("username");
	        	  String pass = rs.getString("password");
	        	  
	        	  rs.close();
	        	  
	        	  // Creating the User object
	        	  User user = new User(userId, firstName, lastName, email, usrname, pass);
	        	  
	        	  // placing it in the Optional
	        	  Optional<User> userFound = Optional.of(user);
	        	  
	        	  return userFound;
	          } else {
	        	  rs.close();
	        	  return Optional.empty();
	          }
	          
	          
		} catch (SQLException e) {
			System.out.println("Cannot verify user due to connection issues");
			//e.printStackTrace();
			return Optional.empty();
		}
	}
	

	@Override
	public boolean createUser(User user) {
		try( PreparedStatement pstmt = conn.prepareStatement("insert into user(first_name, last_name, email, username, password)"
				+ "values (?, ?, ?, ?, ?)");){
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getUsername());
			pstmt.setString(5, user.getPassword());
			
			int count = pstmt.executeUpdate();
			if(count > 0) {
				//Success if its inserted into the table
				return true;
			}
			
		} catch(SQLException e) {
			// Uncomment if problems occur
//				e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean updateMovieRating(User user, int movieId, int rating) {
		try(PreparedStatement pstmt = conn.prepareStatement("update user_rate_movie set rating = ? where user_id = ? and movie_id = ?");){
			pstmt.setInt(1, rating);
			pstmt.setInt(2, user.getUserId());
			pstmt.setInt(3, movieId);

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
	public boolean addMovieForRating(User user, int movieId, int rating) {
		try( PreparedStatement pstmt = conn.prepareStatement("insert into user_rate_movie(user_id, movie_id, rating)"
				+ " values(?, ?, ?)")){
			pstmt.setInt(1, user.getUserId());
			pstmt.setInt(2, movieId);
			pstmt.setInt(3, rating); // Set to be "not complete"
			
			int count = pstmt.executeUpdate();
			if(count > 0) { // insert happened
				return true;
			}

		} catch (SQLIntegrityConstraintViolationException e) {
	        System.out.println("Error: Duplicate entry detected. This movie is already being tracked.");
		    return false; 
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
		
	}

	@Override
	public int getMovieRating(User user, int movieId) {
		int rating = 0;
		try(PreparedStatement pstmt = conn.prepareStatement("select rating from user_rate_movie where user_id = ? and movie_id = ?")){
			pstmt.setInt(1, user.getUserId());
			pstmt.setInt(2, movieId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				rating = rs.getInt("rating");
			}
			rs.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return rating;
	}

	@Override
	public int getMovieRateCount(int movieId) {
		try(PreparedStatement pstmt = conn.prepareStatement("select count(*) from user_rate_movie where movie_id = ?")){
			pstmt.setInt(1, movieId);
			
			ResultSet rs = pstmt.executeQuery();
			
			// check if it has something
			if(rs.next()) {
				int records = rs.getInt(1);
				rs.close();
				return records;
			} else {
				rs.close();
				return -1;
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	@Override
	public List<Integer> getMovieRatings(int movieId) {
		List<Integer> movieRatingList = new ArrayList<>();
		try(PreparedStatement pstmt = conn.prepareStatement("select rating from user_rate_movie where movie_id = ?")){
			pstmt.setInt(1, movieId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				//add to the list
				movieRatingList.add(rs.getInt("rating"));
			} 
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return movieRatingList;
	}

}
