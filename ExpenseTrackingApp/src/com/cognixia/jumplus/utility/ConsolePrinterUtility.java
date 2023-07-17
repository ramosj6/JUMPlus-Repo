package com.cognixia.jumplus.utility;

import com.cognixia.jumplus.dao.Expense;
import com.cognixia.jumplus.dao.User;

import java.util.List;

public class ConsolePrinterUtility {
    private static ColorsUtility c;

    public static void welcome(){
        String asciiArt = c.PURPLE_BOLD_BRIGHT + " ____  ____    ________  _ ____  _____ _      ____  _____   _____  ____  ____  ____  _  __ _____ ____ \n" +
                "/  _ \\/  __\\  /  __/\\  \\///  __\\/  __// \\  /|/ ___\\/  __/  /__ __\\/  __\\/  _ \\/   _\\/ |/ //  __//  __\\\n" +
                "| | \\|| | //  |  \\   \\  / |  \\/||  \\  | |\\ |||    \\|  \\      / \\  |  \\/|| / \\||  /  |   / |  \\  |  \\/|\n" +
                "| |_/|| |_\\\\  |  /_  /  \\ |  __/|  /_ | | \\||\\___ ||  /_     | |  |    /| |-|||  \\_ |   \\ |  /_ |    /\n" +
                "\\____/\\____/  \\____\\/__/\\\\\\_/   \\____\\\\_/  \\|\\____/\\____\\    \\_/  \\_/\\_\\\\_/ \\|\\____/\\_|\\_\\\\____\\\\_/\\_\\\n" +
                "                                                                                                      \r\n" + c.RESET;
        String welcome = c.GREEN_BOLD + "\nWELCOME TO THE DOLLARS BANK EXPENSE TRACKER PROGRAM! Please select an option from the menu below to get started:\n" + c.RESET;
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
                |  1. Add expense                                         |\r
                |  2. Remove expense                                      |\r
                |  3. Set budget                                          |\r
                |  4. View 5 upcoming expenses                            |\r
                |  5. Display and Compare                                 |\r
                |  6. Account information                                 |\r
                |  7. EXIT/SIGN OUT                                       |\r
                +========================================================+""";

        System.out.println(welcome + userMenu);
    }

    public static void printUserExpenses(List<Expense> expenses){
        System.out.println();
        String leftAlignFormat = "| %-4d | %15s | %15s | %10s | %10s |%n";

        System.out.format("+====================================================================+%n");
        System.out.format("| #    | Category        | Date            | Amount     | Recurring  |%n");
        System.out.format("+------+-----------------+-----------------+------------+------------+%n");

        int counter = 1;
        for(Expense e: expenses){
            System.out.format(leftAlignFormat, counter, e.getCategory(), e.getExpenseDate(), "$" + e.getAmount(), e.isRecurring());
            counter++;
        }

        System.out.format("| " + counter + ")    EXIT                                                        |%n");
        System.out.format("+====================================================================+%n");
    }

    public static void printFiveUpcomingExpenses(List<Expense> expenses){
        if(expenses.isEmpty()){
            System.out.println(ColorsUtility.YELLOW_BOLD + ColorsUtility.ITALICS_START +
                    "\nHooray! Looks like you have no future expenses!" + ColorsUtility.ITALICS_END + ColorsUtility.RESET);
        } else {
            System.out.println();
            System.out.println(ColorsUtility.GREEN_BOLD + "Here are your 5(or less) upcoming expenses:" + ColorsUtility.RESET);
            String leftAlignFormat = "| %-4d | %15s | %15s | %10s | %10s |%n";

            System.out.format("+====================================================================+%n");
            System.out.format("| #    | Category        | Date            | Amount     | Recurring  |%n");
            System.out.format("+------+-----------------+-----------------+------------+------------+%n");

            int counter = 1;
            for(Expense e: expenses){
                System.out.format(leftAlignFormat, counter, e.getCategory(), e.getExpenseDate(), "$" + e.getAmount(), e.isRecurring());
                counter++;
            }

            System.out.format("+====================================================================+%n");
        }
        System.out.println(ColorsUtility.GREEN_BOLD + "\nLoading back to User Menu..." + ColorsUtility.RESET);
    }

    public static void printUserInformation(User user){
        System.out.println(ColorsUtility.CYAN_BOLD + "\nHere is your Account Information:");
        System.out.println("+===========================================+");
        System.out.println(ColorsUtility.CYAN_UNDERLINED + "Customer ID: " + ColorsUtility.RESET + ColorsUtility.CYAN_BOLD + user.getUserId());
        System.out.println(ColorsUtility.CYAN_UNDERLINED + "Name: " + ColorsUtility.RESET + ColorsUtility.CYAN_BOLD + user.getName());
        System.out.println(ColorsUtility.CYAN_UNDERLINED + "Email: " + ColorsUtility.RESET + ColorsUtility.CYAN_BOLD + user.getEmail());
        System.out.println(ColorsUtility.CYAN_UNDERLINED + "Username: " + ColorsUtility.RESET + ColorsUtility.CYAN_BOLD + user.getUsername());
        System.out.println("+===========================================+");
        System.out.println(ColorsUtility.GREEN_BOLD + "\nLoading back to User Menu...\n" + ColorsUtility.RESET);

    }
}
