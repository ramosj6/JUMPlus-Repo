package com.cognixia.jumplus.controller;

import com.cognixia.jumplus.dao.*;
import com.cognixia.jumplus.utility.ColorsUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

import static com.cognixia.jumplus.utility.ConsolePrinterUtility.*;

public class ExpenseController {

    private static Scanner scan = new Scanner(System.in);

    // global boolean value to keep track if user logs out
    private static boolean isLogin;

    // Keeps track of the user for the application
    private static User userFound;
    private static UserDao userDao;
    private static ExpenseDao expenseDao;

    // This will be called in the ExpenseTrackingApplication class by the main method
    public static void run(String[] args) {
        int validOption = mainMenu();
        while(validOption == -1){ // loop back again if user enters a string
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
                            int validUserMenuOption = userMenu();
                            while(validUserMenuOption == -1) {
                                validUserMenuOption = userMenu();
                            }
                            switch(validUserMenuOption) {
                                case 1:
                                    addExpense();
                                    break;
                                case 2:
                                    deleteExpense();
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    List<Expense> expenses = expenseDao.getFiveUpcomingExpenses(userFound);
                                    printFiveUpcomingExpenses(expenses);
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    // user already found, don't need to get user again from database
                                    printUserInformation(userFound);
                                    break;
                                case 7:
                                    System.out.println(ColorsUtility.GREEN + "Logging out...\n" + ColorsUtility.RESET);
                                    isLogin = false;
                                    userFound = null; // reset user
                                    break;
                            }
                        }
                    } else {
                        System.out.println(ColorsUtility.RED + "Couldn't find that user. Please try again!" + ColorsUtility.RESET);
                    }
                    break;
            }
            validOption = mainMenu();
            while(validOption == -1) { // loop here if user enters a string
                validOption = mainMenu();
            }
        }
        scan.close();
        System.out.println(ColorsUtility.GREEN_BOLD + "Terminating the program...\n\nBye!");
    }

    private static int mainMenu(){
        int option = 0;
        boolean valid = true; // valid option default set to true
        scan = null;
        // instantiating the Dao classes and setting the connections
        userDao = new UserDaoSql();
        expenseDao = new ExpenseDaoSql();

        try {
            scan = new Scanner(System.in);
            userDao.setConnection();
            expenseDao.setConnection();
            welcome();
            option = scan.nextInt(); // get the option the user chooses
            if(option != 1 && option != 2 && option != 3) {
                valid = false;
            }
            while(!valid){
                System.out.println(ColorsUtility.RED + "Please enter a valid option from the menu."+ ColorsUtility.RESET); // re-prompt the user
                option = scan.nextInt();
                if(option == 1 || option == 2 || option == 3) {
                    valid = true;
                }
            }
            return option;
        } catch(InputMismatchException e){
            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again.\n" + ColorsUtility.RESET);
            return -1;
        } catch(ClassNotFoundException | IOException | SQLException e) {
            System.out.println(ColorsUtility.RED + "ERROR: " + e.getMessage() + ColorsUtility.RESET);
            return -1;
        }
    }

    public static boolean login(){
        try {
            System.out.println(ColorsUtility.BLUE_BOLD + "\nLogin\n-------------------------------------");
            System.out.print(ColorsUtility.BLUE_UNDERLINED + "Username: ");
            String username = scan.next();
            System.out.print("Password: " + ColorsUtility.RESET);
            String password = scan.next();
            System.out.println();

            Optional<User> userToFind = userDao.validateUser(username, password);

            //check if the optional contains something
            if(userToFind.isPresent()){
                userFound = userToFind.get();
                System.out.println(ColorsUtility.BLUE_BOLD + "-------------------------------------");
                System.out.println(ColorsUtility.GREEN_BOLD + "User login Success! Welcome " + userFound.getUsername() + "!" + ColorsUtility.RESET);
                isLogin = true;
                return true;
            } else {
                return false;
            }
        } catch(InputMismatchException e){
            System.out.println(ColorsUtility.RED + "Error: " + e.getMessage() + ColorsUtility.RESET);
            return false;
        }
    }

    public static void registrationMenu() {
        System.out.print(ColorsUtility.BLUE_BOLD + "\n\n---------------------------------------------------------" +
                "\nRegistration: Please provide the following details below.\n"
                + ColorsUtility.BLUE_UNDERLINED + "First Name: ");
        String firstName = scan.next();

        System.out.print("Last Name: ");
        String lastName = scan.next();

        String fullName = firstName + " " + lastName;

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
        User user = new User(1, fullName, email, username, password);
        if(userDao.createUser(user)) {
            System.out.println(ColorsUtility.GREEN + "\nSuccessfully created user " + username + "!" + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.RED + "Could not create user. Please try again." + ColorsUtility.RESET);
        }

        System.out.println(ColorsUtility.BLUE_BOLD + "---------------------------------------------------------\n\n" + ColorsUtility.RESET);
    }

    public static int userMenu(){
        scan = null;
        List<Integer> validOptionsList = new ArrayList<>();
        for(int i = 1; i <= 7; i++){ // will be easier to check if user option is valid
            validOptionsList.add(i);
        }
        int userOption = 0;
        printUserMenu();

        try {
            scan = new Scanner(System.in);
            userOption = scan.nextInt();
            while(!validOptionsList.contains(userOption)){ // re-prompt the user
                System.out.println(ColorsUtility.RED + "Please enter a valid option from the menu or exit" + ColorsUtility.RESET);
                userOption = scan.nextInt();
            }
            return userOption;
        } catch (InputMismatchException e) {
            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again.\n" + ColorsUtility.RESET);
            return -1;
        }
    }

    public static void addExpense(){
        System.out.println(ColorsUtility.BLUE_BOLD + "\n---------------------------\nLet's add a New Expense:\n---------------------------");
        System.out.print(ColorsUtility.BLUE_UNDERLINED + "Category: " + ColorsUtility.RESET);
        String category = scan.next();

        boolean validDate;
        System.out.println(ColorsUtility.BLUE + ColorsUtility.ITALICS_START + "Note: String needs to be of the format y-M-d or yyyy-MM-d" +
                ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        System.out.print(ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "Date: ");
        String expenseDate;
        LocalDate date = null;

        do {
            try {
                // String is of the format y-M-d or yyyy-MM-d
                expenseDate = scan.next();
                // Using parse method to convert the string to LocalDate object
                date = LocalDate.parse(expenseDate, DateTimeFormatter.ISO_DATE);

                validDate = true;

            } // If the String pattern is invalid
            catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println(ColorsUtility.RED + "Error: " + e.getMessage() + "\nPlease try again." + ColorsUtility.RESET);
                System.out.print(ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "Date: ");
                validDate = false;
            }
        } while(!validDate);

        scan = null;
        boolean validAmount;
        double amount = 0.0;
        System.out.print("Amount: ");
        do{
            try{
                scan = new Scanner(System.in);
                amount = scan.nextDouble();
                validAmount = true;
            } catch(InputMismatchException e){
                System.out.println(ColorsUtility.RED + "You must enter a numeric value. Please try again" + ColorsUtility.RESET);
                System.out.print(ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "Amount: ");
                validAmount = false;
            }

        } while(!validAmount);


        System.out.print("Recurring(true/false): ");
        boolean recurring = scan.nextBoolean();

        // Creating the new Expense object;
        Expense expense = new Expense(1, userFound.getUserId(), category, date, amount, recurring);

        if(expenseDao.createExpense(expense)) {
            System.out.println(ColorsUtility.GREEN + "\nSuccessfully created expense for " + userFound.getUsername() + "!" + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.RED + "Could not create expense. Please try again." + ColorsUtility.RESET);
        }
        System.out.println(ColorsUtility.BLUE_BOLD + "---------------------------" + ColorsUtility.RESET);

    }

    public static void deleteExpense(){
        List<Expense> expenses = expenseDao.getAllExpenses(userFound);
        if(expenses.isEmpty()){
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nWhoops. Looks like you have no expenses to delete!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            printUserExpenses(expenses);
            System.out.println(ColorsUtility.GREEN_BOLD + "Which expense would you like to delete?" + ColorsUtility.RESET);

            scan = null;
            boolean validOption;
            int option = 0;

            do{
                try {
                    scan = new Scanner(System.in);
                    option = scan.nextInt();
                    if(!(option > 0 && option <= expenses.size() + 1)){
                        System.out.println(ColorsUtility.RED + "Please enter a valid expense from your expenses list." + ColorsUtility.RESET);
                        validOption = false;
                    } else {
                        validOption = true;
                    }
                } catch(InputMismatchException e){
                    System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
                    validOption = false;
                }
            } while(!validOption);


            // after getting a valid option from the list, we can delete as long as it's not the exit option
            if(option == expenses.size() + 1){
                System.out.println(ColorsUtility.GREEN_BOLD + "Exiting...");
            } else {
                int expenseId = expenses.get(option-1).getExpenseId();
                if(expenseDao.deleteExpense(expenseId, userFound)) {
                    System.out.println(ColorsUtility.GREEN + "\nSuccessfully deleted expense for " + userFound.getUsername() + "!" + ColorsUtility.RESET);
                } else {
                    System.out.println(ColorsUtility.RED + "Could not delete expense. Please try again." + ColorsUtility.RESET);
                }
            }
        }
        System.out.println(ColorsUtility.GREEN_BOLD + "\nLoading back to User Menu..." + ColorsUtility.RESET);
    }

    //Source: https://www.baeldung.com/java-email-validation-regex#:~:text=The%20simplest%20regular%20expression%20to,otherwise%2C%20the%20result%20is%20false.
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
