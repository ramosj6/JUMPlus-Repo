package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StudentDao {

	public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;
	
	public List<Student> getAllStudents();

	public Optional<Student> getStudentById(int id);
	
	public Optional<Enrolled> getStudentEnrolledByIds(int studentId, int courseId, int teacherId);
	
	public List<Enrolled> getStudentsEnrolled(int id, int teacherId);

	public boolean updateStudentGPA(double gpa, int studentId);

}
