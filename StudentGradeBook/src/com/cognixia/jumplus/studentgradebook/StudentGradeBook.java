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
import com.cognixia.jumplus.dao.CourseDao;
import com.cognixia.jumplus.dao.CourseDaoSql;
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
	
	private static CourseDao courseDao;
	
	// keeps track of the enrolled list in case any changes occurs(ex. sorting changes)
	private static List<Enrolled> enrolledStudents;
	
	private static List<Course> courseList;
		
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
							
							int coursesTeaching = courseList.size();
							if(validCourseOption != coursesTeaching + 2) { // this checks if teacher wants to exit/logout of application
								if(validCourseOption == coursesTeaching + 1) { // adding course option selected
									int courseToBeAdded = addCourseMenu();
									while(courseToBeAdded == -1) {
										courseToBeAdded = addCourseMenu();
									}
									if(teacherDao.addCourse(courseToBeAdded, teacherFound.getTeacherId())) {
										System.out.println("Congratulations! You have successfully added " +  courseDao.getCourseById(courseToBeAdded).get().getCourseName() + " to your list of courses!");
									} else {
										System.out.println("Please choose another student to enroll in your class.\n");
									}
								} else {
									//getting courseId
									int courseId = courseList.get(validCourseOption-1).getId();
									System.out.println("\nYou have chosen " + courseDao.getCourseById(courseId).get().getCourseName() + "!");
									
									// adding teacher as well so we know what teacher the student is taking for a particular course
									enrolledStudents = studentDao.getStudentsEnrolled(courseId, teacherFound.getTeacherId());
									
									int teacherOption = studentMenu();
									while(teacherOption == -1) { // This is if user inputs a string
										teacherOption = studentMenu();
									}
									while(teacherOption != 7) { // this is to keep on the student menu
										teacherOptionAction(teacherOption, validCourseOption);
										teacherOption = studentMenu();
									}
								}
								
							} else {
								System.out.println("Logging out...\n");
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
	
	private static int studentMenu() {	
		printEnrolledStudents();
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
		
	public static int mainMenu() {
		int option = 0;
		boolean valid = true; // valid option default set to true
		Scanner scan = null;
		// instantiating the Dao classes and setting the connections
		teacherDao = new TeacherDaoSql();
		
		studentDao = new StudentDaoSql();
		courseDao = new CourseDaoSql();
		try {
			teacherDao.setConnection();
			studentDao.setConnection();
			courseDao.setConnection();
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
	
	public static int teacherMenu() {
		int courseOption = 0;
		Scanner scan = null;
		
		try {

			courseList = teacherDao.getTeacherCourses(teacherFound.getTeacherId());
			
			if(courseList.isEmpty()) {
				System.out.println("It looks like you are not currently teaching any courses, please add a course.");
			} else {
				System.out.println("Here are the courses your are currently teaching:\n");
				System.out.println("Which class would you like to select and view?");
			}
				
			viewCourses(courseList);
			
			scan = new Scanner(System.in);
			courseOption = scan.nextInt();
			
			while(!(courseOption > 0 && courseOption <= courseList.size() + 2 )) { // re-prompting user
				System.out.println("Please enter a valid course from the list or exit.");
				courseOption = scan.nextInt();
			}
			
			return courseOption; // this returns the exit 

		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
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
										
					enrolledStudents = new ArrayList<>();
					for(Student s: sortedStudents) {
						enrolledStudents.add(studentDao.getStudentEnrolledByIds(s.getStudentId(), validCourseOption, teacherFound.getTeacherId()).get());
					}
					System.out.println("Success! Your student list has been updated and sorted by alphabetical order!\n");

				} else {
					enrolledStudents = enrolledStudents.stream()
	                        .sorted(Comparator.comparingDouble(Enrolled::getGrade).reversed()) // go in descending order
	                        .collect(Collectors.toList());
					System.out.println("Success! Your student list has been updated and sorted by grade!\n");
				}
				break;
			case 4: // updating
				updateStudentGradeMenu(validCourseOption);
				break;
			case 5: // adding
				int studentToBeAdded = addStudentMenu();
				while(studentToBeAdded == -1) {
					studentToBeAdded = addStudentMenu();
				}
				
				System.out.println("\nAdding...\n\n");
				if(teacherDao.addStudent(studentToBeAdded, validCourseOption, teacherFound.getTeacherId())) {
					System.out.println("Congratulations! You have successfully enrolled " + studentDao.getStudentById(studentToBeAdded).get().getFirstName() + " to the course!");
					enrolledStudents = studentDao.getStudentsEnrolled(validCourseOption, teacherFound.getTeacherId());
				} else {
					System.out.println("Please choose another student to enroll in your class.\n");
				}
				break;
			case 6: // deleting
				int studentToBeDeleted = deleteStudentMenu();
				while(studentToBeDeleted == -1) {
					studentToBeDeleted = deleteStudentMenu();
				}
				System.out.println("\nDeleting...\n\n");
				if(teacherDao.deleteStudent(studentToBeDeleted, validCourseOption, teacherFound.getTeacherId())) {
					System.out.println("Successful! You successfully removed " + studentDao.getStudentById(studentToBeDeleted).get().getFirstName() + " from the course!");
					enrolledStudents = studentDao.getStudentsEnrolled(validCourseOption, teacherFound.getTeacherId());
				} else {
					System.out.println("Could not delete student, please try again.");
				}
				break;
		}
	}
	
	private static int deleteStudentMenu() {
		System.out.println("Who would you like to remove from your course?");
		
		try {
			Scanner scan = new Scanner(System.in);
			int select = scan.nextInt();
			while(!(select > 0 && select <= enrolledStudents.size()) ) {
				System.out.println("Please enter a valid student from the enrollment list.");
				select = scan.nextInt();
			}
			
			// after choosing a valid student from the list
			return enrolledStudents.get(select-1).getStudentId();
		} catch(InputMismatchException e) {
			System.out.println("Input must be a number. Please try again.\n");
		}
		return -1;
	}
	
	private static int addCourseMenu() {
		List<Course> allCourses = courseDao.getAllCourses();
		System.out.println("Here are the list of all the course available:\n"
				+ "-----------------------------------------------------------------------");
		
		for(Course c: allCourses) {
			System.out.println("   " + c.getId() + ") Course Name: " + c.getCourseName());
		}
		
		try {
			System.out.println("\nWhich course would you like to start teaching?");
			Scanner scan = new Scanner(System.in);
			int courseId = scan.nextInt();
			
			while(!(courseId > 0 && courseId <= allCourses.size())) {
				System.out.println("Please enter a valid course id");
				courseId = scan.nextInt();
			}
			return courseId;
			
		} catch(InputMismatchException e) {
			System.out.println("Input must be a number. Please try again.\n");
			return -1;
		}
	}
	
	private static int addStudentMenu() {
		List<Student> allStudents = studentDao.getAllStudents();
		System.out.println("Here are the list of all students:\n"
				+ "-----------------------------------------------------------------------");
		
		for(Student s: allStudents) {
			System.out.println("   " + s.getStudentId() + ") Name: " + s.getFirstName() + " " + s.getLastName()
					+ ", Major: " + s.getMajor());
		}
		
		try {
			System.out.println("\nWho would you like to enroll in your course?");
			Scanner scan = new Scanner(System.in);
			int studentId = scan.nextInt();
			
			while(!(studentId > 0 && studentId <= allStudents.size())) {
				System.out.println("Please enter a valid student id");
				studentId = scan.nextInt();
			}
			return studentId;
			
		} catch(InputMismatchException e) {
			System.out.println("Input must be a number. Please try again.\n");
			return -1;
		}
	}
	
	private static void updateStudentGradeMenu(int courseId) {
		System.out.println("Who's grade would you like to update?");
		try {
			Scanner scan = new Scanner(System.in);
			int select = scan.nextInt();
			while(!(select > 0 && select <= enrolledStudents.size()) ) {
				System.out.println("Please enter a valid student from the enrollment list.");
				select = scan.nextInt();
			}
			
			// after choosing a valid student from the list
			int studentId = enrolledStudents.get(select-1).getStudentId();
			
			System.out.print("Please upgrade their grade: ");
			double upgradedGrade = scan.nextDouble();
			
			if(teacherDao.updateStudentGrade(upgradedGrade, studentId, courseId, teacherFound.getTeacherId())) {
				System.out.println("\nUpdating...\n\nsCongratulations! You have successfully updated " + studentDao.getStudentById(studentId).get().getFirstName() + "'s grade!\n");
				enrolledStudents.get(select-1).setGrade(upgradedGrade); // updating the enrollment list
			} else {
				System.out.println("Could not update the grade for you movie. Please try again.");
			}
		} catch(InputMismatchException e) {
			System.out.println("Input must be a number. Please try again.\n");
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
	
	private static void printEnrolledStudents() {
		if(enrolledStudents.isEmpty()) {
			System.out.println("It looks like you haven't enrolled anyone in this course yet, please add students to this course.");
		} else {
			System.out.println("Here are the students currently enrolled in this course:");
			System.out.println("--------------------------------------------------------");
			int counter = 1;
			for(Enrolled e: enrolledStudents) {
				// We know student exists
				Student student = studentDao.getStudentById(e.getStudentId()).get();
				System.out.println(counter + ")   Name: " + student.getFirstName() + " " + student.getLastName()
						+ ", Major: " + student.getMajor() + ", Grade: " + e.getGrade());
				counter++;
			}
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
		System.out.format("| " + counter++ + ". ADD COURSE                                                       |%n");
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
