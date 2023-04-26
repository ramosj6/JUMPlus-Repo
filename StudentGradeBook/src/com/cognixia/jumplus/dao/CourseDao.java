package com.cognixia.jumplus.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseDao {

	public List<Course> getAllCourses();
	
	public Optional<Course> getCourseById(int id);

	void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;

}
