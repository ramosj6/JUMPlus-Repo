package com.cognixia.jumplus.dao;

public class Student {
	
	private int studentId;
	private String firstName;
	private String lastName;
	private String email;
	private double gpa;
	private String major;
	
	public Student(int studentId, String firstName, String lastName, String email, double gpa, String major) {
		super();
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gpa = gpa;
		this.major = major;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getStudentId() {
		return studentId;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", gpa=" + gpa + ", major=" + major + "]";
	}
	
}
