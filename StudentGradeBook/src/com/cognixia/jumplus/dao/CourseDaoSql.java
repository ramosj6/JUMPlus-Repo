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

public class CourseDaoSql implements CourseDao {
	
	// Connection used for all methods
	private Connection conn;
	

	@Override
	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		conn = ConnectionManager.getConnection();
		
	}

	@Override
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<>();
		try(Statement stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("Select * from course");){
			while(rs.next()) {
				int id = rs.getInt("course_id");
				String courseCode = rs.getString("course_code");
				String name = rs.getString("name");
								
				Course course = new Course(id, courseCode, name);
				
				// adding the movie into the movie list
				courses.add(course);
			}
			rs.close();
			
		} catch(SQLException e) {
			// uncomment of you're running into issues and want to know what's
			// going on
//				e.printStackTrace();
		}
		return courses;
	}

	@Override
	public Optional<Course> getCourseById(int id) {
		try( PreparedStatement pstmt = conn.prepareStatement("select * from course where course_id = ?") ) {
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			// rs.next() will return false if nothing found
			// if you enter the if block, that department with that id was found
			if( rs.next() ) {
				
				int courseId = rs.getInt("course_id");
				String courseCode = rs.getString("course_code");
				String name = rs.getString("name");
				
				rs.close();
				
				// constructing the movie object
				Course course = new Course(courseId, courseCode, name);
				

				// placing it in the optional
				Optional<Course> courseFound = Optional.of(course);
				
				// return the optional
				return courseFound;
				
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

}
