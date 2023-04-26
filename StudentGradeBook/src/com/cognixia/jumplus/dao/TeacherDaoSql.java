package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.cognixia.jumplus.connection.ConnectionManager;

public class TeacherDaoSql implements TeacherDao {
	
	// Connection used for all methods
	private Connection conn;
	
	@Override
	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		conn = ConnectionManager.getConnection();		
	}

	@Override
	public Optional<Teacher> validateTeacher(String username, String password) {
		try( PreparedStatement pstmt = conn.prepareStatement("select * from teacher where username = ? and password = ?");){
			  pstmt.setString(1, username);
	          pstmt.setString(2, password);
	          
	          ResultSet rs = pstmt.executeQuery();
	          
	          if( rs.next() ) {
	        	  int userId = rs.getInt("teacher_id");
	        	  String firstName = rs.getString("first_name");
	        	  String lastName = rs.getString("last_name");
	        	  String email = rs.getString("email");
	        	  String usrname = rs.getString("username");
	        	  String pass = rs.getString("password");
	        	  
	        	  rs.close();
	        	  
	        	  // Creating the User object
	        	  Teacher teacher = new Teacher(userId, firstName, lastName, email, usrname, pass);
	        	  
	        	  // placing it in the Optional
	        	  Optional<Teacher> userFound = Optional.of(teacher);
	        	  
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
	public boolean createTeacher(Teacher userTeacher) {
		try( PreparedStatement pstmt = conn.prepareStatement("insert into teacher(first_name, last_name, email, username, password)"
				+ "values (?, ?, ?, ?, ?)");){
			pstmt.setString(1, userTeacher.getFirstName());
			pstmt.setString(2, userTeacher.getLastName());
			pstmt.setString(3, userTeacher.getEmail());
			pstmt.setString(4, userTeacher.getUsername());
			pstmt.setString(5, userTeacher.getPassword());
			
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
	public List<Course> getTeacherCourses(int teacherId) {
		List<Course> courseList = new ArrayList<>();
		try(PreparedStatement pstmt = conn.prepareStatement("select * "
				+ "from teacher_course tc "
				+ "inner join course c ON tc.course_id = c.course_id "
				+ "where teacher_id = ?");) {
			pstmt.setInt(1, teacherId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// get the attributes of the joined table
				int courseId = rs.getInt("course_id");
				String courseCode = rs.getString("course_code");
				String name = rs.getString("name");
				
				Course course = new Course(courseId, courseCode, name);
				// add to the list
				courseList.add(course);
			}
			rs.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return courseList;
	}

	@Override
	public double getAverageGrade(int courseId, int teacherId) {
		try( PreparedStatement pstmt = conn.prepareStatement("select avg(grade) from enrolled where course_id = ? and teacher_id = ?")){
			pstmt.setInt(1, courseId);
			pstmt.setInt(2, teacherId);
			
			ResultSet rs = pstmt.executeQuery();
			
			// check if it has something
			if(rs.next()) {
				double avg = rs.getDouble(1);
				rs.close();
				return avg;
			} else {
				rs.close();
				return -1;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<Double> getGrades(int courseId, int teacherId) {
		List<Double> gradesList = new ArrayList<>();
		try(PreparedStatement pstmt = conn.prepareStatement("select grade from enrolled where course_id = ? and teacher_id = ?")){
			pstmt.setInt(1, courseId);
			pstmt.setInt(2, teacherId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				//add to the list
				gradesList.add(rs.getDouble("grade"));
			} 
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return gradesList;
	}

	@Override
	public boolean updateStudentGrade(double grade, int studentId, int courseId, int teacherId) {
		try( PreparedStatement pstmt = conn.prepareStatement("update enrolled set grade = ? where student_id = ? and course_id = ? and teacher_id = ?");){
			pstmt.setDouble(1, grade);
			pstmt.setInt(2, studentId);
			pstmt.setInt(3, courseId);
			pstmt.setInt(4, teacherId);
			
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
	public boolean addStudent(int studentId, int courseId, int teacherId) {
		try( PreparedStatement pstmt = conn.prepareStatement("insert into enrolled(student_id, course_id, teacher_id, enrolled_date, grade)"
				+ " values(?, ?, ?, ?, null)")){
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, courseId);
			pstmt.setInt(3, teacherId); // Set to be "not complete"
			pstmt.setDate(4, new Date(System.currentTimeMillis())); // Set to be "not complete"
			
			
			int count = pstmt.executeUpdate();
			if(count > 0) { // insert happened
				return true;
			}

		} catch (SQLIntegrityConstraintViolationException e) {
	        System.out.println("Error: Duplicate entry detected. This student is already enrolled in your course!");
		    return false; 
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean addCourse(int courseId, int teacherId) {
		try( PreparedStatement pstmt = conn.prepareStatement("insert into teacher_course(teacher_id, course_id)"
				+ " values(?, ?)")){
			pstmt.setInt(1, teacherId);
			pstmt.setInt(2, courseId);
			
			
			int count = pstmt.executeUpdate();
			if(count > 0) { // insert happened
				return true;
			}

		} catch (SQLIntegrityConstraintViolationException e) {
	        System.out.println("Error: Duplicate entry detected. You are already teaching this course!");
		    return false; 
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;	}

	@Override
	public boolean deleteStudent(int studentId, int courseId, int teacherId) {
		try( PreparedStatement pstmt = conn.prepareStatement("delete from enrolled where student_id = ? and course_id = ? and teacher_id = ?");){
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, courseId);
			pstmt.setInt(3, teacherId);

			int count = pstmt.executeUpdate();
			if(count > 0) {
				// if its not deleted
				return true;
			}
		} catch(SQLException e) {
			return false;
		}
		return false;	}

}
