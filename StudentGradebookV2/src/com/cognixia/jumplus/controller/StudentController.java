package com.cognixia.jumplus.controller;

import com.cognixia.jumplus.dao.*;
import com.cognixia.jumplus.utility.ColorsUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import static com.cognixia.jumplus.utility.ConsolePrinterUtility.*;

public class StudentController {

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

    public static void run(String[] args){
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
                                        System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START + "Congratulations! You have successfully added " +
                                                courseDao.getCourseById(courseToBeAdded).get().getCourseName() + " to your list of courses!" +
                                                ColorsUtility.ITALICS_END + ColorsUtility.ITALICS_END);
                                    } else {
                                        System.out.println(ColorsUtility.RED + "Please choose another course to add!" + ColorsUtility.RESET);
                                    }
                                } else {
                                    //getting courseId
                                    int courseId = courseList.get(validCourseOption-1).getId(); // start here
                                    System.out.println(courseId);
                                    System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                                            "\nYou have chosen " + courseDao.getCourseById(courseId).get().getCourseName() + "!\n" +
                                            ColorsUtility.ITALICS_END + ColorsUtility.RESET);

                                    // adding teacher as well, so we know what teacher the student is taking for a particular course
                                    enrolledStudents = studentDao.getStudentsEnrolled(courseId, teacherFound.getTeacherId());

                                    // TODO: start from here
                                    int teacherOption = studentMenu();
                                    while(teacherOption == -1) { // This is if user inputs a string
                                        teacherOption = studentMenu();
                                    }
                                    while(teacherOption != 7) { // this is to keep on the student menu
                                        teacherOptionAction(teacherOption, validCourseOption);
                                        teacherOption = studentMenu();
                                    }
                                    enrolledStudents = null; // reset enrolled students list in case teacher wants to choose different course
                                }

                            } else {
                                System.out.println(ColorsUtility.GREEN + "Logging out...\n" + ColorsUtility.RESET);
                                isLogin = false;
                                teacherFound = null; // reset user
                            }
                        }
                    } else {
                        System.out.println(ColorsUtility.BLUE_BOLD + "-------------------------------------");
                        System.out.println(ColorsUtility.RED + "\nCouldn't find that user. Please try again!\n" + ColorsUtility.RESET);
                    }
                    break;
            }
            validOption = mainMenu();
            while(validOption == -1) { // loop here if user enters a string
                validOption = mainMenu();
            }
        }
        System.out.println(ColorsUtility.GREEN_BOLD + "Terminating the program...\n\nBye!");
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
            welcome();
            option = scan.nextInt(); // get the option that user chooses
            if(option != 1 && option != 2 && option != 3) {
                valid = false;
            }
            while(!valid) {
                System.out.println(ColorsUtility.RED + "Please enter a valid option from the menu." + ColorsUtility.RESET); // re-prompting the user
                option = scan.nextInt();
                if(option == 1 || option == 2 || option == 3) {
                    valid = true;
                }
            }
            return option;
        } catch(InputMismatchException e) {
            System.out.print(ColorsUtility.RED + "Input must be a number. Please try again.\n" + ColorsUtility.RESET);
            return -1;
        }  catch(ClassNotFoundException | IOException | SQLException e) {
            System.out.println(ColorsUtility.RED + "ERROR: " + e.getMessage() + ColorsUtility.RESET);
            return -1;
        }
    }

    public static void registrationMenu() {
        System.out.print( ColorsUtility.BLUE_BOLD + "\n\n---------------------------------------------------------" +
                "\nRegistration: Please provide the following details below.\n"
                + ColorsUtility.BLUE_UNDERLINED + "First Name: ");
        Scanner scan = new Scanner(System.in);
        String firstName = scan.next();

        System.out.print("Last Name: ");
        String lastName = scan.next();

        System.out.print("Email: ");
        String email = scan.next();
        // regex for validating email format
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        while(!patternMatches(email, regex)) {
            System.out.print(ColorsUtility.RED + "\nIncorrect email format. Please try again.\n" +
                    ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "\nEmail:");
            email = scan.next();
        }

        System.out.print("Username: ");
        String username = scan.next();

        System.out.print("Password: ");
        String password = scan.next();
        System.out.print("Confirm Password: ");
        String confirmPassword = scan.next();
        while(!password.equals(confirmPassword)) {
            System.out.print(ColorsUtility.RED + "\nPasswords do not match. Please re-confirm your password\n" +
                    ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "\nConfirm Password: ");
            confirmPassword = scan.next();
        }

        //adding the user to the user table
        Teacher user = new Teacher(1, firstName, lastName, email, username, password);
        if(teacherDao.createTeacher(user)) {
            System.out.println(ColorsUtility.RESET);
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nSuccessfully created user " + username + "!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.RED + "Could not create user. Please try again." + ColorsUtility.RESET);
        }

        System.out.println(ColorsUtility.BLUE_BOLD + "---------------------------------------------------------\n\n" + ColorsUtility.RESET);
    }

    private static boolean login() {
        Scanner sc = new Scanner(System.in);

        System.out.println(ColorsUtility.BLUE_BOLD + "\nLogin:\n-------------------------------------");
        System.out.print(ColorsUtility.BLUE_UNDERLINED + "Username: ");
        String username = sc.next();
        System.out.print("Password: " + ColorsUtility.RESET);
        String password = sc.next();

        Optional<Teacher> userToFind = teacherDao.validateTeacher(username, password);

        // check if the optional has something
        if(userToFind.isPresent()) {
            teacherFound = userToFind.get();
            System.out.println(ColorsUtility.BLUE_BOLD + "-------------------------------------");
            System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                    "\nUser login Success! Welcome " + teacherFound.getFirstName() + "!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
            isLogin = true;
            return true;
        } else {
            return false;
        }
    }

    public static int teacherMenu() {
        int courseOption = 0;
        Scanner scan = null;

        try {
            courseList = teacherDao.getTeacherCourses(teacherFound.getTeacherId());

            if (courseList.isEmpty()) {
                System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                        "It looks like you are not currently teaching any courses, please add a course." +
                        ColorsUtility.ITALICS_END + ColorsUtility.RESET);
            } else {
                System.out.println(ColorsUtility.GREEN_BOLD + "\nHere are the courses you are currently teaching:" + ColorsUtility.RESET);
            }

            printTeacherCourses(courseList);

            scan = new Scanner(System.in);
            courseOption = scan.nextInt();

            while (!(courseOption > 0 && courseOption <= courseList.size() + 2)) { // re-prompting user
                System.out.println(ColorsUtility.RED + "Please enter a valid course from the list or exit." + ColorsUtility.RESET);
                courseOption = scan.nextInt();
            }

            return courseOption; // this returns the exit

        } catch (InputMismatchException e) {
            System.out.print(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
            return -1;
        }
    }

    private static int studentMenu() {
        printEnrolledStudents(enrolledStudents, studentDao);
        int option = 0;

        try {
            Scanner scan = new Scanner(System.in);
            option = scan.nextInt();
            while(option != 0 && option!= 1 && option!= 2 && option != 3 && option != 4
                    && option != 5 && option != 6 && option != 7) {
                System.out.println(ColorsUtility.RED + "Please enter a valid option." + ColorsUtility.RESET);
                option = scan.nextInt();
            }
            return option;
        } catch(InputMismatchException e) {
            System.out.print(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
            return -1;
        }
    }

    private static void teacherOptionAction(int teacherOption, int validCourseOption) {
        switch(teacherOption) {
            case 1:
                double avg = teacherDao.getAverageGrade(validCourseOption, teacherFound.getTeacherId());
                System.out.println(ColorsUtility.YELLOW_BRIGHT + ColorsUtility.ITALICS_START +
                        "The average grade for this class is " +  String.format("%.2f", avg) + "\n"
                + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                break;
            case 2:
                List<Double> grades = teacherDao.getGrades(validCourseOption, teacherFound.getTeacherId());
                if(grades.isEmpty()) {
                    System.out.println(ColorsUtility.RED + "Sorry, it looks like you don't have any students enrolled in your course." +
                            ColorsUtility.RESET);
                } else {
                    // source: http://www.java2s.com/example/java-utility-method/median/median-arraylist-double-values-82543.html
                    Collections.sort(grades);

                    if (grades.size() % 2 == 1)
                        System.out.println(ColorsUtility.YELLOW_BRIGHT + ColorsUtility.ITALICS_START +
                                "The median grade for your course is " + grades.get((grades.size() + 1) / 2 - 1) + "\n" +
                                ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                    else {
                        double lower = grades.get(grades.size() / 2 - 1);
                        double upper = grades.get(grades.size() / 2);

                        System.out.println(ColorsUtility.YELLOW_BRIGHT + ColorsUtility.ITALICS_START +
                                "The median grade for your course is " + ((lower + upper) / 2.0) + "\n" +
                                ColorsUtility.ITALICS_END + ColorsUtility.RESET);
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
                            .toList();

                    enrolledStudents = new ArrayList<>();
                    for(Student s: sortedStudents) {
                        enrolledStudents.add(studentDao.getStudentEnrolledByIds(s.getStudentId(), validCourseOption, teacherFound.getTeacherId()).get());
                    }
                    System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                            "Success! Your student list has been updated and sorted by alphabetical order!\n" +
                            ColorsUtility.ITALICS_END + ColorsUtility.RESET);

                } else {
                    enrolledStudents = enrolledStudents.stream()
                            .sorted(Comparator.comparingDouble(Enrolled::getGrade).reversed()) // go in descending order
                            .collect(Collectors.toList());
                    System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                            "Success! Your student list has been updated and sorted by grade!\n" +
                            ColorsUtility.ITALICS_END + ColorsUtility.RESET);
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

                System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "\nAdding...\n\n" +
                        ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                if(teacherDao.addStudent(studentToBeAdded, validCourseOption, teacherFound.getTeacherId())) {
                    System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                            "Congratulations! You have successfully enrolled " + studentDao.getStudentById(studentToBeAdded).get().getFirstName() + " to the course!" +
                            ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                    enrolledStudents = studentDao.getStudentsEnrolled(validCourseOption, teacherFound.getTeacherId());
                } else {
                    System.out.println(ColorsUtility.RED + "Please choose another student to enroll in your class.\n" + ColorsUtility.RESET);
                }
                break;
            case 6: // deleting
                int studentToBeDeleted = deleteStudentMenu();
                while(studentToBeDeleted == -1) {
                    studentToBeDeleted = deleteStudentMenu();
                }
                System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "\nDeleting...\n\n" +
                        ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                if(teacherDao.deleteStudent(studentToBeDeleted, validCourseOption, teacherFound.getTeacherId())) {
                    System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                            "Successful! You successfully removed " + studentDao.getStudentById(studentToBeDeleted).get().getFirstName() + " from the course!" +
                            ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                    enrolledStudents = studentDao.getStudentsEnrolled(validCourseOption, teacherFound.getTeacherId());
                } else {
                    System.out.println(ColorsUtility.RED + "Could not delete student, please try again." + ColorsUtility.RESET);
                }
                break;
        }
    }

    private static int deleteStudentMenu() {
        System.out.println(ColorsUtility.GREEN_BOLD + "Who would you like to remove from your course?" + ColorsUtility.RESET);

        try {
            Scanner scan = new Scanner(System.in);
            int select = scan.nextInt();
            while(!(select > 0 && select <= enrolledStudents.size()) ) {
                System.out.println(ColorsUtility.RED + "Please enter a valid student from the enrollment list." + ColorsUtility.RESET);
                select = scan.nextInt();
            }

            // after choosing a valid student from the list
            return enrolledStudents.get(select-1).getStudentId();
        } catch(InputMismatchException e) {
            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
        }
        return -1;
    }

    private static int addCourseMenu() {
        List<Course> allCourses = courseDao.getAllCourses();
        printAllCourses(allCourses);

        try {
            System.out.println(ColorsUtility.GREEN_BOLD + "\nWhich course would you like to start teaching?" + ColorsUtility.RESET);
            Scanner scan = new Scanner(System.in);
            int courseId = scan.nextInt();

            while(!(courseId > 0 && courseId <= allCourses.size())) {
                System.out.println(ColorsUtility.RED + "Please enter a valid course id" + ColorsUtility.RESET);
                courseId = scan.nextInt();
            }
            return courseId;

        } catch(InputMismatchException e) {
            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
            return -1;
        }
    }

    private static int addStudentMenu() {
        List<Student> allStudents = studentDao.getAllStudents();
        printAllStudents(allStudents);
        try {
            System.out.println(ColorsUtility.GREEN_BOLD + "\nWho would you like to enroll in your course?" + ColorsUtility.RESET);
            Scanner scan = new Scanner(System.in);
            int studentId = scan.nextInt();

            while(!(studentId > 0 && studentId <= allStudents.size())) {
                System.out.println(ColorsUtility.RED + "Please enter a valid student id" + ColorsUtility.RESET);
                studentId = scan.nextInt();
            }
            return studentId;

        } catch(InputMismatchException e) {
            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
            return -1;
        }
    }

    private static void updateStudentGradeMenu(int courseId) {
        System.out.println(ColorsUtility.GREEN_BOLD + "Who's grade would you like to update?" + ColorsUtility.RESET);
        try {
            Scanner scan = new Scanner(System.in);
            int select = scan.nextInt();
            while(!(select > 0 && select <= enrolledStudents.size()) ) {
                System.out.println(ColorsUtility.RED + "Please enter a valid student from the enrollment list." + ColorsUtility.RESET);
                select = scan.nextInt();
            }

            // after choosing a valid student from the list
            int studentId = enrolledStudents.get(select-1).getStudentId();

            System.out.print(ColorsUtility.GREEN_BOLD + "Please upgrade their grade: " + ColorsUtility.RESET);
            double upgradedGrade = scan.nextDouble();

            System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "\nUpdating...\n"
                    + ColorsUtility.ITALICS_END + ColorsUtility.RESET);

            if(teacherDao.updateStudentGrade(upgradedGrade, studentId, courseId, teacherFound.getTeacherId())) {
                System.out.println(ColorsUtility.YELLOW + ColorsUtility.ITALICS_START +
                        "Congratulations! You have successfully updated " + studentDao.getStudentById(studentId).get().getFirstName() + "'s grade!" +
                        ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                enrolledStudents.get(select-1).setGrade(upgradedGrade); // updating the enrollment list
            } else {
                System.out.println(ColorsUtility.RED + "Could not update the grade for you movie. Please try again." + ColorsUtility.RESET);
            }
        } catch(InputMismatchException e) {
            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
        }
    }

    private static int sortOption() {
        printSortOptionMenu();
        int sortOption = 0;
        try {
            Scanner scan = new Scanner(System.in);
            sortOption = scan.nextInt();
            while(sortOption != 1 && sortOption != 2) {
                System.out.println(ColorsUtility.RED + "Please enter a valid sorting option." + ColorsUtility.RESET);
                sortOption = scan.nextInt();
            }
            return sortOption;
        } catch(InputMismatchException e) {
            System.out.print(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
            return -1;
        }
    }

    //Source: https://www.baeldung.com/java-email-validation-regex#:~:text=The%20simplest%20regular%20expression%20to,otherwise%2C%20the%20result%20is%20false.
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
