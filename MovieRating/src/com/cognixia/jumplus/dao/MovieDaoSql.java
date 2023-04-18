package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cognixia.jumplus.connection.ConnectionManager;

public class MovieDaoSql implements MovieDao{
	
	// Connection used for all methods
	private Connection conn;

	@Override
	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		conn = ConnectionManager.getConnection();
	}

	@Override
	public List<Movie> getAllMovies() {
		List<Movie> movies = new ArrayList<>();
		try(Statement stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("Select * from movies");){
			while(rs.next()) {
				int id = rs.getInt("movie_id");
				String title = rs.getString("title");
				String genre = rs.getString("genre");
				int length = rs.getInt("length");
				int avgRating = rs.getInt("average_rating");

				
				Movie movie = new Movie(id, title, genre, length, avgRating);
				
				// adding the movie into the movie list
				movies.add(movie);
			}
			
			
		} catch(SQLException e) {
			// uncomment of you're running into issues and want to know what's
			// going on
//				e.printStackTrace();
		}
		return movies;
	}

	@Override
	public Optional<Movie> getMovieById(int id) {
		
		try( PreparedStatement pstmt = conn.prepareStatement("select * from movies where movie_id = ?") ) {
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			// rs.next() will return false if nothing found
			// if you enter the if block, that department with that id was found
			if( rs.next() ) {
				
				int movieId = rs.getInt("movie_id");
				String title = rs.getString("title");
				String genre = rs.getString("genre");
				int length = rs.getInt("length");
				int avgRating = rs.getInt("average_rating");
				
				rs.close();
				
				// constructing the movie object
				Movie movie = new Movie(movieId, title, genre, length, avgRating);
				

				// placing it in the optional
				Optional<Movie> movieFound = Optional.of(movie);
				
				// return the optional
				return movieFound;
				
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
	public boolean createMovie(Movie movie) {
		try( PreparedStatement pstmt = conn.prepareStatement("insert into movies(title, genre, length, film_studios)"
				+ "values (?, ?, ?, ? )");){
			pstmt.setString(1, movie.getTitle());
			pstmt.setString(2, movie.getGenre());
			pstmt.setInt(3, movie.getLengthMin());
			pstmt.setInt(4, movie.getAvgRating());
			
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
	public boolean deleteMovie(int id) {
		try( PreparedStatement pstmt = conn.prepareStatement("delete from movies where movie_id = ?");){
			pstmt.setInt(1, id);
			int count = pstmt.executeUpdate();
			if(count > 0) {
				// if its not deleted
				return true;
			}
		} catch(SQLException e) {
			return false;
		}
		return false;	}

	// will only update the average rating for our case
	@Override
	public boolean updateMovie(double avgRating, int movieId) {
		try(PreparedStatement pstmt = conn.prepareStatement("update movies set average_rating = ? where movie_id = ?");){
			pstmt.setDouble(1, avgRating);
			pstmt.setInt(2, movieId);
			
			int count = pstmt.executeUpdate();
			if(count > 0) {
				return true;
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
