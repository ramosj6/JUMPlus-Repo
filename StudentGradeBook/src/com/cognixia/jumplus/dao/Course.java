package com.cognixia.jumplus.dao;

public class Course {
	
	// private variables for course
	private int id;
		
	private String courseCode;
		
	private String courseName;

	public Course(int id, String courseCode, String courseName) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.courseName = courseName;
	}

	public int getId() {
		return id;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseCode=" + courseCode + ", courseName=" + courseName + "]";
	}
	
	
	

}
