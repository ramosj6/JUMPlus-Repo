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

public class ContactController {

    private static Scanner scan = new Scanner(System.in);

    // global boolean value to keep track if user logs out
    private static boolean isLogin;

    // will keep track of what sort option we're on.
    // 1 is by unique id or default
    // 2 is sort by name ascending order
    // 3 is sorting by name but descending order
    private static int sort = 1;

    // regex for validating email format
    private static final String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    // regex for validating phone number format
    private static final String regexPhone = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$";;

    // Keeps track of the user for the application
    private static User userFound;
    private static UserDao userDao;

    private static ContactDao contactDao;

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
                                    List<Contact> contacts = contactDao.getAllContacts(userFound);
                                    if(contacts.isEmpty()){
                                        System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                                                "\nWhoops. Looks like you have no contacts. Please add some contacts to view!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                                    } else {
                                        System.out.println(ColorsUtility.GREEN_BOLD + "\nHere are your list of contacts:" + ColorsUtility.RESET);
                                        if(sort == 1){
                                            contacts = contactDao.getAllContacts(userFound);
                                        } else if(sort == 2){
                                            contacts = contactDao.getContactsSortByAlphabet(userFound);
                                        } else if(sort == 3){
                                            contacts = contactDao.getContactsSortByAlphabetReverse(userFound);
                                        }
                                        assert contacts != null;
                                        printUserContacts(contacts);
                                        System.out.println(ColorsUtility.GREEN_BOLD + "\nLoading back to User Menu..." + ColorsUtility.RESET);
                                    }
                                    break;
                                case 2:
                                    addContact();
                                    break;
                                case 3:
                                    deleteContact();
                                    break;
                                case 4:
                                    updateContact();
                                    break;
                                case 5:
                                    sortContacts();
                                    break;
                                case 6:
                                    System.out.println(ColorsUtility.GREEN + "Logging out...\n" + ColorsUtility.RESET);
                                    isLogin = false;
                                    userFound = null; // reset user
                                    sort = 1; // setting to default again
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
        contactDao = new ContactDaoSql();

        try {
            scan = new Scanner(System.in);
            userDao.setConnection();
            contactDao.setConnection();
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

        while(!patternMatches(email, regexEmail)) {
            System.out.print(ColorsUtility.RED + "\nIncorrect email format. Please try again.\n" +
                    ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "\nEmail:");
            email = scan.next();
        }

        System.out.print("Phone #: ");
        String phoneNumber = scan.next();

        while(!patternMatches(phoneNumber, regexPhone)) {
            System.out.print(ColorsUtility.RED + "\nIncorrect phone number format. Please try again.\n" +
                    ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "\nPhone #: ");
            phoneNumber = scan.next();
        }

        System.out.print("Address: ");
        String address = scan.nextLine();
        address = scan.nextLine();

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
        User user = new User(1, fullName, email, phoneNumber, address, username, password);
        if(userDao.createUser(user)) {
            System.out.println(ColorsUtility.RESET);
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nSuccessfully created user " + username + "!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
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

    public static void addContact(){
        System.out.println(ColorsUtility.BLUE_BOLD + "\n--------------------------------------------------" +
                "\nLet's add a New Contact:\n--------------------------------------------------");
        System.out.print(ColorsUtility.BLUE_UNDERLINED + "First Name: ");
        String firstName = scan.next();

        System.out.print("Last Name: ");
        String lastName = scan.next();

        String fullName = firstName + " " + lastName;

        System.out.print("Email: ");
        String email = scan.next();

        while(!patternMatches(email, regexEmail)) {
            System.out.print(ColorsUtility.RED + "\nIncorrect email format. Please try again.\n" +
                    ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "\nEmail:");
            email = scan.next();
        }

        System.out.print("Phone #: ");
        String phoneNumber = scan.nextLine();
        phoneNumber = scan.nextLine();

        if(!phoneNumber.equals("")){
            while(!phoneNumber.equals("") && !patternMatches(phoneNumber, regexPhone)) {
                System.out.print(ColorsUtility.RED + "\nIncorrect phone number format. Please try again.\n" +
                        ColorsUtility.BLUE_BOLD + ColorsUtility.BLUE_UNDERLINED + "\nPhone #: ");
                phoneNumber = scan.nextLine();
            }
        }

        System.out.print("Address: ");
        String address = scan.nextLine();

        // Creating the new Contact object;
        Contact contact = new Contact(1, userFound.getUserId(), fullName, email, phoneNumber, address);

        if(contactDao.addContact(contact)) {
            System.out.println(ColorsUtility.RESET);
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START
                    + "\nSuccessfully created contact for " + userFound.getUsername() + "!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.RED + "Could not create contact. Please try again." + ColorsUtility.RESET);
        }
        System.out.println(ColorsUtility.BLUE_BOLD + "--------------------------------------------------" + ColorsUtility.RESET);

    }

    public static void deleteContact(){
        List<Contact> contacts = contactDao.getAllContacts(userFound);
        if(contacts.isEmpty()){
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nWhoops. Looks like you have no contacts to delete. Please add some contacts!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.GREEN_BOLD + "\nHere is your list of Contacts:" + ColorsUtility.RESET);
            printUserContacts(contacts);
            System.out.println(ColorsUtility.GREEN_BOLD + "Which contact would you like to delete from your list?" + ColorsUtility.RESET);

            scan = null;
            boolean validOption;
            int option = 0;

            do{
                try {
                    scan = new Scanner(System.in);
                    option = scan.nextInt();
                    if(!(option > 0 && option <= contacts.size() + 1)){
                        System.out.println(ColorsUtility.RED + "Please enter a valid contact from your contacts list." + ColorsUtility.RESET);
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
            if(option == contacts.size() + 1){
                System.out.println(ColorsUtility.GREEN_BOLD + "Exiting...");
            } else {
                int contactId = contacts.get(option-1).getContactId();
                if(contactDao.deleteContact(contactId, userFound)) {
                    System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START
                            + "\nSuccessfully deleted contact for " + userFound.getUsername() + "!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                } else {
                    System.out.println(ColorsUtility.RED + "Could not delete contact. Please try again." + ColorsUtility.RESET);
                }
            }
        }
        System.out.println(ColorsUtility.GREEN_BOLD + "\nLoading back to User Menu..." + ColorsUtility.RESET);
    }

    public static void sortContacts(){
        scan = null;
        boolean validOption;
        int option = 0;

        printSortOptionMenu();

        do{
            try {
                scan = new Scanner(System.in);
                option = scan.nextInt();
                if(!(option > 0 && option <= 4)){
                    System.out.println(ColorsUtility.RED + "Please enter a valid contact from your contacts list." + ColorsUtility.RESET);
                    validOption = false;
                } else {
                    validOption = true;
                }
            } catch(InputMismatchException e){
                System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
                validOption = false;
            }
        } while(!validOption);

        // after getting a valid option from the sort options menu, we can delete as long as it's not the exit option
        // Need to get the list of contacts first to check if its not empty
        if(contactDao.getAllContacts(userFound).isEmpty() && option != 4){
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nWhoops. Looks like you have no contacts. Please add some contacts!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START);
            if(option == 1){
                sort = 1;
                System.out.println("Success! You have sorted your contacts by Unique ID!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                printUserContacts(contactDao.getAllContacts(userFound));
            } else if(option == 2){
                sort = 2;
                System.out.println("Success! You have sorted your contacts by Alphabetical Ascending order!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                printUserContacts(contactDao.getContactsSortByAlphabet(userFound));
            } else if(option == 3){
                sort = 3;
                System.out.println("Success! You have sorted your contacts by Alphabetical Descending order!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                printUserContacts(contactDao.getContactsSortByAlphabetReverse(userFound));
            } else {
                System.out.println(ColorsUtility.GREEN_BOLD + "Exiting...");
            }
        }

        System.out.println(ColorsUtility.GREEN_BOLD + "\nLoading back to User Menu..." + ColorsUtility.RESET);

    }

    public static void updateContact(){
        List<Contact> contacts = contactDao.getAllContacts(userFound);
        if(contacts.isEmpty()){
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nWhoops. Looks like you have no contacts to update. Please add some contacts!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println(ColorsUtility.GREEN_BOLD + "\nContacts List:" + ColorsUtility.RESET);
            printUserContacts(contacts);
            System.out.println(ColorsUtility.GREEN_BOLD + "Which contact would you like to update?" + ColorsUtility.RESET);

            scan = null;
            boolean validOption;
            int option = 0;

            do{
                try {
                    scan = new Scanner(System.in);
                    option = scan.nextInt();
                    if(!(option > 0 && option <= contacts.size() + 1)){
                        System.out.println(ColorsUtility.RED + "Please enter a valid contact from your contacts list." + ColorsUtility.RESET);
                        validOption = false;
                    } else {
                        validOption = true;
                    }
                } catch(InputMismatchException e){
                    System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
                    validOption = false;
                }
            } while(!validOption);

            // Now that we have a valid contact option, we need to ask what they want to update
            // and keep looping back until they're done updating the contact information
            if(option == contacts.size() + 1){
                System.out.println(ColorsUtility.GREEN_BOLD + "Exiting...");
            } else {
                Contact contact = contacts.get(option-1);
                System.out.println(ColorsUtility.GREEN_BOLD + "\nWhat would you like to update for " + contact.getName() + "?" + ColorsUtility.RESET);

                boolean isUpdating = true;
                boolean update = false;
                do {
                    printUpdateOptionMenu();
                    boolean updateOption;
                    option = 0;
                    scan = null;

                    do {
                        try {
                            scan = new Scanner(System.in);
                            option = scan.nextInt();
                            if (!(option > 0 && option <= 5)) {
                                System.out.println(ColorsUtility.RED + "Please enter a valid option from the menu." + ColorsUtility.RESET);
                                updateOption = false;
                            } else {
                                updateOption = true;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println(ColorsUtility.RED + "Input must be a number. Please try again." + ColorsUtility.RESET);
                            updateOption = false;
                        }
                    } while (!updateOption);

                    // we got a valid update option value
                    switch (option) {
                        case 1:
                            System.out.println(ColorsUtility.PURPLE_BOLD_BRIGHT + "\n----------------------------------------------------------------------" +
                                    "\nUpdating Name:\n----------------------------------------------------------------------");
                            System.out.print("First Name: ");
                            String firstName = scan.next();

                            System.out.print("Last Name: ");
                            String lastName = scan.next();

                            String fullName = firstName + " " + lastName;
                            contact.setName(fullName);
                            update = true;
                            System.out.println("\nSuccessfully changed the name for the contact. Pending Update...");
                            System.out.println("----------------------------------------------------------------------\n" + ColorsUtility.RESET);
                            System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "Anything else to update? If not, just exit and update if you changed anything!" +
                                    ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                            break;
                        case 2:
                            System.out.println(ColorsUtility.PURPLE_BOLD_BRIGHT + "\n----------------------------------------------------------------------" +
                                    "\nUpdating Email:\n----------------------------------------------------------------------");
                            System.out.print("Email: ");
                            String email = scan.next();

                            while (!patternMatches(email, regexEmail)) {
                                System.out.print(ColorsUtility.RED + "\nIncorrect email format. Please try again.\n" +
                                        ColorsUtility.PURPLE_BOLD_BRIGHT + "\nEmail:");
                                email = scan.next();
                            }
                            contact.setEmail(email);
                            update = true;

                            System.out.println("\nSuccessfully changed the email for the contact. Pending Update...");
                            System.out.println("----------------------------------------------------------------------\n" + ColorsUtility.RESET);
                            System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "Anything else to update? If not, just exit and update if you changed anything!" +
                                    ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                            break;
                        case 3:
                            System.out.println(ColorsUtility.PURPLE_BOLD_BRIGHT + "\n----------------------------------------------------------------------" +
                                    "\nUpdating Phone Number:\n----------------------------------------------------------------------");
                            System.out.print("Phone #: ");
                            String phoneNumber = scan.nextLine();
                            phoneNumber = scan.nextLine();

                            if (!phoneNumber.equals("")) {
                                while (!phoneNumber.equals("") && !patternMatches(phoneNumber, regexPhone)) {
                                    System.out.print(ColorsUtility.RED + "\nIncorrect phone number format. Please try again.\n" +
                                            ColorsUtility.PURPLE_BOLD_BRIGHT + "\nPhone #: ");
                                    phoneNumber = scan.nextLine();
                                }
                            }
                            contact.setPhoneNumber(phoneNumber);
                            update = true;
                            System.out.println("\nSuccessfully changed the phone number for the contact. Pending Update...");
                            System.out.println("----------------------------------------------------------------------\n" + ColorsUtility.RESET);
                            System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "Anything else to update? If not, just exit and update if you changed anything!" +
                                    ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                            break;
                        case 4:
                            System.out.println(ColorsUtility.PURPLE_BOLD_BRIGHT + "\n----------------------------------------------------------------------" +
                                    "\nUpdating Address:\n----------------------------------------------------------------------");
                            System.out.print("Address: ");
                            String address = scan.nextLine();
                            address = scan.nextLine();
                            contact.setAddress(address);
                            update = true;
                            System.out.println("\nSuccessfully changed the address for the contact. Pending Update...");
                            System.out.println("----------------------------------------------------------------------\n" + ColorsUtility.RESET);
                            System.out.println(ColorsUtility.GREEN_BOLD + ColorsUtility.ITALICS_START + "Anything else to update? If not, just exit and update!" +
                                    ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                            break;
                        case 5:
                            // we want to update once we exit
                            // we must update if user did actually update something
                            if(update){
                                if(contactDao.updateContact(contact)) {
                                    System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                                            "\nCongratulations, " + userFound.getUsername() + "! You successfully updated contact information for "
                                            + contact.getName() + "!\n" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                                } else {
                                    System.out.println(ColorsUtility.RED + "Could not update contact. Please try again." + ColorsUtility.RESET);
                                }
                            } else {
                                System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START + "You did not update any information of your contact " +
                                        contact.getName() + "! Please try updating something next time!\n" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
                            }
                            System.out.println(ColorsUtility.GREEN_BOLD + "Exiting...");
                            isUpdating = false;
                    }
                } while(isUpdating);
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
