package com.cognixia.jumplus.utility;

import com.cognixia.jumplus.dao.Contact;

import java.util.List;

public class ConsolePrinterUtility {

    private static ColorsUtility c;

    public static void welcome(){
        String asciiArt = c.CYAN_BOLD_BRIGHT + " ____  ____  _      _____  ____  ____  _____    _      ____  _      ____  _____ _____ ____ \n" +
                "/   _\\/  _ \\/ \\  /|/__ __\\/  _ \\/   _\\/__ __\\  / \\__/|/  _ \\/ \\  /|/  _ \\/  __//  __//  __\\\n" +
                "|  /  | / \\|| |\\ ||  / \\  | / \\||  /    / \\    | |\\/||| / \\|| |\\ ||| / \\|| |  _|  \\  |  \\/|\n" +
                "|  \\__| \\_/|| | \\||  | |  | |-|||  \\_   | |    | |  ||| |-||| | \\||| |-||| |_//|  /_ |    /\n" +
                "\\____/\\____/\\_/  \\|  \\_/  \\_/ \\|\\____/  \\_/    \\_/  \\|\\_/ \\|\\_/  \\|\\_/ \\|\\____\\\\____\\\\_/\\_\\\n" +
                "                                                                                                      \r\n" + c.RESET;
        String welcome = c.GREEN_BOLD + "\nWELCOME TO THE CONTACT MANAGER PROGRAM! Please select an option from the menu below to get started:\n" + c.RESET;
        String loginMenu = """
                +========================================================+\r
                |  1. REGISTER                                         |\r
                |  2. LOGIN                                            |\r
                |  3. EXIT                                             |\r
                +========================================================+""";

        System.out.println(asciiArt + welcome + loginMenu);
    }

    public static void printUserMenu(){
        String welcome = c.GREEN_BOLD + "\nPlease choose an option below to continue!\n" + c.RESET;
        String userMenu = """
                +========================================================+\r
                |  1. View Contacts                                      |\r
                |  2. Add Contact                                        |\r
                |  3. Delete Contact                                     |\r
                |  4. Update Contact                                     |\r
                |  5. Sort                                               |\r
                |  6. EXIT/SIGN OUT                                      |\r
                +========================================================+""";

        System.out.println(welcome + userMenu);
    }

    public static void printUserContacts(List<Contact> contacts){
        System.out.println();
        String leftAlignFormat = "| %-4d | %25s | %25s | %15s | %40s |%n";

        System.out.format("+===========================================================================================================================+%n");
        System.out.format("| #    | Name                      | Email                     | Phone #         | Address                                  |%n");
        System.out.format("+------+---------------------------+---------------------------+-----------------+------------------------------------------+%n");

        int counter = 1;
        for(Contact c: contacts){
            System.out.format(leftAlignFormat, counter, c.getName(), c.getEmail(), (c.getPhoneNumber().equals("")) ? "N/A" : c.getPhoneNumber(),
                    (c.getAddress().equals("")) ? "N/A" : c.getAddress());
            counter++;
        }

        System.out.format("| " + counter + ") EXIT                                                                                                                   |%n");
        System.out.format("+===========================================================================================================================+%n");
    }

    public static void printSortOptionMenu(){
        String intro = ColorsUtility.GREEN_BOLD + "\nHow would you like to sort your contacts?\n" + ColorsUtility.RESET;
        String sortMenu = """
                +========================================================+\r
                |  1. Unique ID (default)                                |\r
                |  2. Name (Ascending)                                   |\r
                |  3. Name (Descending)                                  |
                |  4. EXIT                                               |\r
                +========================================================+""";
        System.out.println(intro + sortMenu);
    }

    public static void printUpdateOptionMenu(){
        String updateMenu = """
                +========================================================+\r
                |  1. Name                                               |\r
                |  2. Email                                              |\r
                |  3. Phone Number                                       |
                |  4. Address                                            |\r
                |  5. UPDATE/EXIT                                        |
                +========================================================+""";
        System.out.println(updateMenu);
    }
}
