package com.cognixia.jumplus.utility;

import com.cognixia.jumplus.dao.Course;
import com.cognixia.jumplus.dao.Enrolled;
import com.cognixia.jumplus.dao.Student;
import com.cognixia.jumplus.dao.StudentDao;

import java.util.List;

public class ConsolePrinterUtility {

    private static ColorsUtility c;

    public static void welcome(){
        String asciiArt = c.CYAN_BOLD_BRIGHT + " ____  _____  _     ____  _____ _      _____    _____ ____  ____  ____  _____ ____  ____  ____  _  __\n" +
                "/ ___\\/__ __\\/ \\ /\\/  _ \\/  __// \\  /|/__ __\\  /  __//  __\\/  _ \\/  _ \\/  __//  _ \\/  _ \\/  _ \\/ |/ /\n" +
                "|    \\  / \\  | | ||| | \\||  \\  | |\\ ||  / \\    | |  _|  \\/|| / \\|| | \\||  \\  | | //| / \\|| / \\||   / \n" +
                "\\___ |  | |  | \\_/|| |_/||  /_ | | \\||  | |    | |_//|    /| |-||| |_/||  /_ | |_\\\\| \\_/|| \\_/||   \\ \n" +
                "\\____/  \\_/  \\____/\\____/\\____\\\\_/  \\|  \\_/    \\____\\\\_/\\_\\\\_/ \\|\\____/\\____\\\\____/\\____/\\____/\\_|\\_\\\n" +
                "                                                                                                      \r\n" + c.RESET;
        String welcome = c.GREEN_BOLD + "\nWELCOME TEACHER, TO THE STUDENT GRADEBOOK APP! Please register as a new teacher or login to get started:\n" + c.RESET;
        String loginMenu = """
                +========================================================+\r
                |  1. NEW TEACHER? REGISTER                            |\r
                |  2. LOGIN                                            |\r
                |  3. EXIT                                             |\r
                +========================================================+""";

        System.out.println(asciiArt + welcome + loginMenu);
    }

    public static void printEnrolledStudents(List<Enrolled> enrolledStudents, StudentDao studentDao) {
        if(enrolledStudents.isEmpty()) {
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nWhoops! It looks like you haven't enrolled anyone in this course yet, please add students to this course."
                    + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.GREEN_BOLD + "\nHere are the students currently enrolled in this course:" + ColorsUtility.RESET);
            System.out.println(ColorsUtility.YELLOW_BOLD + "---------------------------------------------------------------------");
            int counter = 1;
            for(Enrolled e: enrolledStudents) {
                // We know student exists
                Student student = studentDao.getStudentById(e.getStudentId()).get();
                System.out.println(counter + ")   Name: " + student.getFirstName() + " " + student.getLastName()
                        + ", Major: " + student.getMajor() + ", Grade: " + e.getGrade());
                counter++;
            }
            System.out.println("---------------------------------------------------------------------");
        }

        System.out.println(ColorsUtility.GREEN_BOLD + "\nHow would you like to proceed?" + ColorsUtility.RESET);
        System.out.println("+========================================================+\r\n"
                + "|  1. DISPLAY AVERAGE GRADE                            |\r\n"
                + "|  2. DISPLAY MEDIAN GRADE                             |\r\n"
                + "|  3. SORT                                             |\r\n"
                + "|  4. UPDATE                                           |\r\n"
                + "|  5. ADD                                              |\r\n"
                + "|  6. DELETE                                           |\r\n"
                + "|  7. EXIT                                             |\r\n"
                + "+========================================================+");
    }

    public static void printTeacherCourses(List<Course> courseList) {
        // Source: https://stackoverflow.com/questions/15215326/how-can-i-create-table-using-ascii-in-a-console
        // once user is logged in, display the Courses in database
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
        System.out.println(ColorsUtility.GREEN_BOLD + "Which class would you like to select and view?" + ColorsUtility.RESET);
    }

    public static void printAllStudents(List<Student> allStudents){
        System.out.println(ColorsUtility.GREEN_BOLD + "\nHere are the list of all students:\n" + ColorsUtility.RESET
                + ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START + "-----------------------------------------------------------------------");

        for(Student s: allStudents) {
            System.out.println("   " + s.getStudentId() + ") Name: " + s.getFirstName() + " " + s.getLastName()
                    + ", Major: " + s.getMajor());
        }
        System.out.println("-----------------------------------------------------------------------" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
    }

    public static void printAllCourses(List<Course> allCourses){
        System.out.println(ColorsUtility.GREEN_BOLD + "\nHere are the list of all the courses available:\n" + ColorsUtility.RESET
                + ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START + "------------------------------------------------");

        for(Course c: allCourses) {
            System.out.println("   " + c.getId() + ") Course Name: " + c.getCourseName());
        }
        System.out.println("------------------------------------------------" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
    }

    public static void printSortOptionMenu(){
        System.out.println(ColorsUtility.GREEN + "\nHow would you like to sort by?\n" + ColorsUtility.RESET);
        System.out.println(ColorsUtility.PURPLE_BOLD + "+----------------------------+\n"
                + "1. Alphabetical\n"
                + "2. Grade\n"
                + "+----------------------------+" + ColorsUtility.RESET);
    }

}
