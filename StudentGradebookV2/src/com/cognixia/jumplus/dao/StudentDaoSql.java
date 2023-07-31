package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cognixia.jumplus.connection.ConnectionManager;

public class StudentDaoSql implements StudentDao {
	// Connection used for all methods
	private Connection conn;
	
	@Override
	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		conn = ConnectionManager.getConnection();
	}

	@Override
	public boolean updateStudentGPA(double gpa, int studentId) {
		try(PreparedStatement pstmt = conn.prepareStatement("update student set gpa = ? where student_id = ?");){
			pstmt.setDouble(1, gpa);
			pstmt.setInt(2, studentId);
			
			int count = pstmt.executeUpdate();
			if(count > 0) {
				return true; // update executed
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<>();
		try(Statement stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("Select * from student");){
			while(rs.next()) {
				int id = rs.getInt("student_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				double gpa = rs.getDouble("gpa");
				String major = rs.getString("major");

				
				Student student = new Student(id, firstName, lastName, email, gpa, major);
				
				// adding the movie into the movie list
				students.add(student);
			}
			
			
		} catch(SQLException e) {
			// uncomment of you're running into issues and want to know what's
			// going on
//				e.printStackTrace();
		}
		return students;	}

	@Override
	public Optional<Student> getStudentById(int id) {
		
		try( PreparedStatement pstmt = conn.prepareStatement("select * from student where student_id = ?") ) {
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			// rs.next() will return false if nothing found
			// if you enter the if block, that department with that id was found
			if( rs.next() ) {
				
				int studentId = rs.getInt("student_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				double gpa = rs.getDouble("gpa");
				String major = rs.getString("major");

				rs.close();
				Student student = new Student(studentId, firstName, lastName, email, gpa, major);
				

				// placing it in the optional
				Optional<Student> studentFound = Optional.of(student);
				
				// return the optional
				return studentFound;
				
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
	public List<Enrolled> getStudentsEnrolled(int id, int teachId) {
		List<Enrolled> studentsEnrolled = new ArrayList<>();
		try(PreparedStatement pstmt = conn.prepareStatement("select * from enrolled where course_id = ? and teacher_id = ?");){
			pstmt.setInt(1, id);
			pstmt.setInt(2, teachId);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int studentId = rs.getInt("student_id");
				int courseId = rs.getInt("course_id");
				int teacherId  = rs.getInt("teacher_id");
				Date date = rs.getDate("enrolled_date");
				double grade = rs.getDouble("grade");

				Enrolled enrolled = new Enrolled(studentId, courseId, teacherId, date, grade);
				
				// adding the movie into the movie list
				studentsEnrolled.add(enrolled);
			}
			
			
		} catch(SQLException e) {
			// uncomment of you're running into issues and want to know what's
			// going on
//			e.printStackTrace();
		}
		return studentsEnrolled;

	}

	@Override
	public Optional<Enrolled> getStudentEnrolledByIds(int studentId, int courseId, int teacherId) {
		try( PreparedStatement pstmt = conn.prepareStatement("select * from enrolled where student_id = ? and course_id = ? and teacher_id = ? ");){
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, courseId);
			pstmt.setInt(3, teacherId);
			
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
				int studId = rs.getInt("student_id");
				int crsId = rs.getInt("course_id");
				int teachId  = rs.getInt("teacher_id");
				Date date = rs.getDate("enrolled_date");
				double grade = rs.getDouble("grade");
				
				rs.close();

				Enrolled enrolled = new Enrolled(studId, crsId, teachId, date, grade);
				
				// placing it in the optional
				Optional<Enrolled> enrolledFound = Optional.of(enrolled);
				
				// return the optional
				return enrolledFound;
			} else {
				rs.close();
				return Optional.empty();
			}
				
		} catch(SQLException e) {
//			e.printStackTrace();
			return Optional.empty();
		}
	}

}
