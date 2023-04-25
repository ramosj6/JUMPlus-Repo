package com.cognixia.jumplus.dao;

import java.util.Date;

public class Enrolled {
	
	private int studentId;
    private int courseId;
    private int teacherId;
    private Date enrolledDate;
    private double grade;
    
	public Enrolled(int studentId, int courseId, int teacherId, Date enrolledDate, double grade) {
		super();
		this.studentId = studentId;
		this.courseId = courseId;
		this.teacherId = teacherId;
		this.enrolledDate = enrolledDate;
		this.grade = grade;
	}

	public int getStudentId() {
		return studentId;
	}

	public int getCourseId() {
		return courseId;
	}
	

	public int getTeacherId() {
		return teacherId;
	}

	public Date getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Enrolled [studentId=" + studentId + ", courseId=" + courseId + ", teacherId=" + teacherId
				+ ", enrolledDate=" + enrolledDate + ", grade=" + grade + "]";
	}
}
