package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TeacherDao {
	public Optional<Teacher> validateTeacher(String username, String password);
	
	public boolean createTeacher(Teacher userTeacher);
	
	public List<Course> getTeacherCourses(int teacherId);
	
	public double getAverageGrade(int courseId, int teacherId);
	
	public List<Double> getGrades(int courseId, int teacherId);
	
	public boolean updateStudentGrade(double grade, int studentId, int courseId, int teacherId);
	
	public boolean addStudent(int studentId, int courseId, int teacherId);
	
	public boolean deleteStudent(int studentId, int courseId, int teacherId);
	
	public boolean addCourse(int courseId, int teacherId);

	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;


}
