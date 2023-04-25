package com.cognixia.jumplus.studentgradebook;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cognixia.jumplus.dao.Course;
import com.cognixia.jumplus.dao.Enrolled;
import com.cognixia.jumplus.dao.Student;
import com.cognixia.jumplus.dao.StudentDao;
import com.cognixia.jumplus.dao.StudentDaoSql;
import com.cognixia.jumplus.dao.Teacher;
import com.cognixia.jumplus.dao.TeacherDao;
import com.cognixia.jumplus.dao.TeacherDaoSql;



public class StudentGradeBook {
	
	// global boolean value to keep track if user logs out
	private static boolean isLogin;
	
	// Keeps track of the user for the application
	private static Teacher teacherFound;
	private static TeacherDao teacherDao;
	
	private static StudentDao studentDao;
	
	// keeps track of the enrolled list in case any changes occurs(ex. sorting changes)
	private static List<Enrolled> enrolledStudents;
		
	public static void main(String[] args) {
		int validOption = mainMenu();
		while(validOption == -1) { // loop here if user enters a string
			validOption = mainMenu();
		}
		
		// branch to other functions when a particular option is chosen
		while(validOption != 3) {
			switch(validOption) {
				case 1:
					registrationMenu();
					break;
				case 2:
					if(login()) {
						while(isLogin) {
							int validCourseOption = teacherMenu();
							while(validCourseOption == -1) {
								validCourseOption = teacherMenu();
							}
							if(validCourseOption != teacherDao.getTeacherCourses(teacherFound.getTeacherId()).size()+1) {
								int teacherOption = studentMenu(validCourseOption);
								while(teacherOption == -1) {
									teacherOption = studentMenu(validCourseOption);
								}
								if(teacherOption != 7) {
									teacherOptionAction(teacherOption, validCourseOption);
								}
							} else {
								isLogin = false;
								teacherFound = null; // reset user
							}
						}
					} else {
						System.out.println("Couldn't find that user. Please try again!");
					}
					break;
			}
			validOption = mainMenu();
			while(validOption == -1) { // loop here if user enters a string
				validOption = mainMenu();
			}
		}
		System.out.println("Terminating the program...");
	}
	
	private static boolean login() {
		try {
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\nLogin:\n--------------");
			System.out.print("Username: ");
			String username = sc.next();
			System.out.print("Password: ");
			String password = sc.next();
			System.out.println();
			
			Optional<Teacher> userToFind = teacherDao.validateTeacher(username, password);
			
			// check if the optional has something
			if(userToFind.isPresent()) {
				teacherFound = userToFind.get();
				System.out.println("----------------------------------------------------------------");
				System.out.println("User login Success! Welcome " + teacherFound.getFirstName() + "!");
				isLogin = true;
				return true;
			} else {
				return false;
			}			
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static void registrationMenu() {
		System.out.print("-----------------\nRegistration: Please provide the following details below.\n"
				+ "First Name: ");
		Scanner scan = new Scanner(System.in);
		String firstName = scan.next();
		
		System.out.print("Last Name: ");
		String lastName = scan.next();
			
		System.out.print("Email: ");
		String email = scan.next();
		// regex for validating email format
		String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		
		while(!patternMatches(email, regex)) {
			System.out.print("Incorrect email format. Please try again.\nEmail:");
			email = scan.next();
		}
		
		System.out.print("Username: ");
		String username = scan.next();

		System.out.print("Password: ");
		String password = scan.next();
		System.out.print("Confirm Password: ");
		String confirmPassword = scan.next();
		while(!password.equals(confirmPassword)) {
			System.out.print("Passwords do not match. Please re-confirm your password\nConfirm Password: ");
			confirmPassword = scan.next();
		}
		
		//adding the user to the user table
		Teacher user = new Teacher(1, firstName, lastName, email, username, password);
		if(teacherDao.createTeacher(user)) {
			System.out.println("Successfully created user!");
		} else {
			System.out.println("Could not create user. Please try again.");
		}
	}
	
	// WORKING HERE!!!
	private static int studentMenu(int courseId) {
		// adding teacher as well so we know what teacher the student is taking for a particular course
		enrolledStudents = studentDao.getStudentsEnrolled(courseId, teacherFound.getTeacherId());
		if(enrolledStudents.isEmpty()) {
			System.out.println("It looks like you haven't enrolled anyone in this course yet, please add students to this course.");
		} else {
			System.out.println("Here are the students currently enrolled in this course:");
			System.out.println("--------------------------------------------------------");
			for(Enrolled e: enrolledStudents) {
				// We know student exists
				Student student = studentDao.getStudentById(e.getStudentId()).get();
				System.out.println("   Name: " + student.getFirstName() + " " + student.getLastName()
						+ ", Major: " + student.getMajor() + ", Grade: " + e.getGrade());
			}
		}
		
		System.out.println("\nHow would you like to proceed?");
		System.out.println("+========================================================+\r\n"
				+ "|  1. DISPLAY AVERAGE GRADE                            |\r\n"
				+ "|  2. DISPLAY MEDIAN GRADE                             |\r\n"
				+ "|  3. SORT                                             |\r\n"
				+ "|  4. UPDATE                                           |\r\n"
				+ "|  5. ADD                                              |\r\n"
				+ "|  6. DELETE                                           |\r\n"
				+ "|  7. EXIT                                             |\r\n"
				+ "+========================================================+");

		int option = 0;
		
		try {
			Scanner scan = new Scanner(System.in);
			option = scan.nextInt();
			while(option != 0 && option!= 1 && option!= 2 && option != 3 && option != 4 
					&& option != 5 && option != 6 && option != 7) {
				System.out.println("Please enter a valid option.");
				option = scan.nextInt();
			}
			return option;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;		
		}
	}
	
	public static int teacherMenu() {
		int courseOption = 0;
		Scanner scan = null;
		
		try {

			List<Course> courseList = teacherDao.getTeacherCourses(teacherFound.getTeacherId());
			System.out.println("Here are the courses your are currently teaching:\n");
			
			viewCourses(courseList);
			
			System.out.println("Which class would you like to select and view?");
			scan = new Scanner(System.in);
			courseOption = scan.nextInt();
			
			while(!(courseOption > 0 && courseOption <= courseList.size() + 1 )) { // re-prompting user
				System.out.println("Please enter a valid course from the list or exit.");
				courseOption = scan.nextInt();
			}
			
			// return course id instead of number associated with course name
			return courseList.get(courseOption-1).getId();
			
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;	
		}
	}
	
	public static int mainMenu() {
		int option = 0;
		boolean valid = true; // valid option default set to true
		Scanner scan = null;
		// instantiating the Dao classes and setting the connections
		teacherDao = new TeacherDaoSql();
		
		studentDao = new StudentDaoSql();
		try {
			teacherDao.setConnection();
			studentDao.setConnection();
			scan = new Scanner(System.in);
			// main menu in console
			System.out.println("\nWELCOME TEACHER, TO THE STUDENT GRADEBOOK APP! Please register as a new teacher or login to get started.\n");
			System.out.println("+========================================================+\r\n"
					+ "|  1. NEW TEACHER? REGISTER                            |\r\n"
					+ "|  2. LOGIN                                            |\r\n"
					+ "|  3. EXIT                                             |\r\n"
					+ "+========================================================+");
			option = scan.nextInt(); // get the option that user chooses
			if(option != 1 && option != 2 && option != 3) {
				valid = false;
			}
			while(!valid) {
				System.out.println("Please enter a valid option from the menu."); // re-prompting the user
				option = scan.nextInt();
				if(option == 1 || option == 2 || option == 3) {
					valid = true;
				}
			}
			return option;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;
		}  catch(ClassNotFoundException | IOException | SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			return -1;
		}
	}
	
	private static void teacherOptionAction(int teacherOption, int validCourseOption) {
		switch(teacherOption) {
			case 1:
				double avg = teacherDao.getAverageGrade(validCourseOption, teacherFound.getTeacherId());
				System.out.println("The average grade for this class is " +  String.format("%.2f", avg) + "\n");
				break;
			case 2:
				List<Double> grades = teacherDao.getGrades(validCourseOption, teacherFound.getTeacherId());
				if(grades.isEmpty()) {
					System.out.println("Sorry, it looks like you don't have any students enrolled in your course.");
				} else {
					// source: http://www.java2s.com/example/java-utility-method/median/median-arraylist-double-values-82543.html
			        Collections.sort(grades);

			        if (grades.size() % 2 == 1)
			            System.out.println("The median grade for your course is " + grades.get((grades.size() + 1) / 2 - 1) + "\n");
			        else {
			            double lower = grades.get(grades.size() / 2 - 1);
			            double upper = grades.get(grades.size() / 2);

			            System.out.println("The median grade for your course is " + ((lower + upper) / 2.0) + "\n");
			        }
				}
				break;
			case 3:
				int sortOption = sortOption();;
				while(sortOption == -1) {
					sortOption = sortOption();
				}
				
				if(sortOption == 1) {
					// sorting by alphabet
					List<Student> students = new ArrayList<>();
					for (Enrolled e: enrolledStudents) {
						Student student = studentDao.getStudentById(e.getStudentId()).get();
						students.add(student);
					}
					Comparator<Student> compareByName = Comparator
							.comparing( Student::getFirstName )
							.thenComparing(Student::getLastName);
					
					// getting the sorted list
					List<Student> sortedStudents = students.stream()
							.sorted(compareByName)
							.collect(Collectors.toList());
					
					System.out.println(sortedStudents);
					
					enrolledStudents = new ArrayList<>();
					for(Student s: sortedStudents) {
						enrolledStudents.add(studentDao.getStudentEnrolledByIds(s.getStudentId(), validCourseOption, teacherFound.getTeacherId()).get());
					}
				} else {
					enrolledStudents = enrolledStudents.stream()
	                        .sorted(Comparator.comparingDouble(Enrolled::getGrade))
	                        .filter(x2 -> x2.getGrade() <= 1)
	                        .collect(Collectors.toList());
					System.out.println("Your student list has been updated and sorted by grade:");
				}
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
		}
	}
	
	private static int sortOption() {
		System.out.println("How would you like to sort by?\n"
				+ "----------------------------\n"
				+ "1. Alphabetical\n"
				+ "2. Grade");	
		int sortOption = 0;
		try {
			Scanner scan = new Scanner(System.in);
			sortOption = scan.nextInt();
			while(sortOption != 1 && sortOption != 2) {
				System.out.println("Please enter a valid sorting option.");
				sortOption = scan.nextInt();
			}
			return sortOption;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;
		} 
		
	}
	
	private static void viewCourses(List<Course> courseList) {
		// Source: https://stackoverflow.com/questions/15215326/how-can-i-create-table-using-ascii-in-a-console
		// once user is logged in, display the movies in database
		String leftAlignFormat = "| %-40s | %-11s | %-10d |%n";

		System.out.format("+=====================================================================+%n");
		System.out.format("| Course Name                              | Course Code | Course Id  |%n");
		System.out.format("+------------------------------------------+-------------+------------+%n");
		int counter = 1;
		for (Course c: courseList) {
		    System.out.format(leftAlignFormat, counter + ". " + c.getCourseName(), c.getCourseCode(), c.getId());
			counter++;
		}
		System.out.format("| " + counter + ". EXIT/LOGOUT                                                      |%n");
		System.out.format("+=====================================================================+%n");
	}
	
	//Source: https://www.baeldung.com/java-email-validation-regex#:~:text=The%20simplest%20regular%20expression%20to,otherwise%2C%20the%20result%20is%20false.
	public static boolean patternMatches(String emailAddress, String regexPattern) {
	    return Pattern.compile(regexPattern)
	      .matcher(emailAddress)
	      .matches();
	}
	
}
