package com.cognixia.jumplus.movierating;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.cognixia.jumplus.dao.Movie;
import com.cognixia.jumplus.dao.MovieDao;
import com.cognixia.jumplus.dao.MovieDaoSql;
import com.cognixia.jumplus.dao.User;
import com.cognixia.jumplus.dao.UserDao;
import com.cognixia.jumplus.dao.UserDaoSql;

public class MovieRating {
	
	private static MovieDao movieDao;

	// Keeps track of the user for the application
	private static User userFound;
	private static UserDao userDao;

	public static void main(String[] args) {
		int validOption = mainMenu();
		while(validOption == -1) { // loop here if user enters a string
			validOption = mainMenu();
		}
		
		// branch to other functions when a particular option is chosen
		switch(validOption) {
			case 1:
				System.out.println("You chose Registering!");
				break;
			case 2:
				if(login()) {
					int validMovieOption = userMenu();
					while(validMovieOption == -1) {
						validMovieOption = userMenu();
					}
					int validRatingOption = ratingMenu(validMovieOption);
					while(validRatingOption == -1) {
						validRatingOption = ratingMenu(validMovieOption);
					}
					// rate the movie using sql
				} else {
					System.out.println("Couldn't find that user. Please try again!");
				}
				break;
			case 3:
				break;
			case 4:
				System.out.println("Terminating the program...");
				break;
		}
		
	}
	
	private static boolean login() {
		userDao = new UserDaoSql();
		try {
			Scanner sc = new Scanner(System.in);
			userDao.setConnection();
			
			System.out.println("\nLogin:\n--------------");
			System.out.print("Username: ");
			String username = sc.next();
			System.out.print("Password: ");
			String password = sc.next();
			System.out.println();
			
			Optional<User> userToFind = userDao.validateUser(username, password);
			
			// check if the optional has something
			if(userToFind.isPresent()) {
				userFound = userToFind.get();
				System.out.println("----------------------------------------------------------------");
				System.out.println("User login Success! Welcome " + userFound.getFirstName() + "!");
				return true;
			} else {
				return false;
			}			
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return false;
		}
		catch(ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static int userMenu() {
		movieDao = new MovieDaoSql();
		int movieOption = 0;
		Scanner scan = null;
		
		try {
			movieDao.setConnection();
			List<Movie> moviesList = movieDao.getAllMovies();
			
			scan = new Scanner(System.in);
			
			// Source: https://stackoverflow.com/questions/15215326/how-can-i-create-table-using-ascii-in-a-console
			// once user is logged in, display the movies in database
			String leftAlignFormat = "| %-40s | %-11s | %-12d |%n";

			System.out.format("+=======================================================================+%n");
			System.out.format("| Movie                                    | Avg. Rating | # of Ratings |%n");
			System.out.format("+------------------------------------------+-------------+--------------+%n");
			int counter = 1;
			for (Movie m: moviesList) {
			    System.out.format(leftAlignFormat, counter + ". " + m.getTitle(), "N/A", 0);
				counter++;
			}
			System.out.format("+=======================================================================+%n");
			System.out.println("Please select a movie to rate.");
			movieOption = scan.nextInt();
			
			while(!(movieOption > 0 && movieOption <= moviesList.size() )) { // re-prompting user
				System.out.println("Please enter a valid movie id from the list.");
				movieOption = scan.nextInt();
			}
			return movieOption;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;		}
		catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
			return -1;		
		}
		
	}
	
	// This method will update the user_rate_menu and movie tables in MySQL
	public static int ratingMenu(int movieId) {
		Optional<Movie> movie = movieDao.getMovieById(movieId);
		// Movie already checked if its valid so no need to check again.
		Movie movieFound = movie.get();
		String leftAlignFormat = "| %-54s |%n";
		System.out.println("+========================================================+");
		System.out.format(leftAlignFormat, " Movie: " + movieFound.getTitle() + ", Average Rating: " + movieFound.getAvgRating());
		System.out.println("|                                                        |\r\n"
				+ "|  Rating:                                               |\r\n"
				+ "|  0. Really Bad                                         |\r\n"
				+ "|  1. Bad                                                |\r\n"
				+ "|  2. Not Good                                           |\r\n"
				+ "|  3. Okay                                               |\r\n"
				+ "|  4. Good                                               |\r\n"
				+ "|  5. Great                                              |\r\n"
				+ "|                                                        |\r\n"
				+ "|  6. EXIT                                               |\r\n"
				+ "+========================================================+");
		System.out.println("Please choose a rating or exit");
		int validRatingOption = 0;
		try {
			Scanner scan = new Scanner(System.in);
			validRatingOption = scan.nextInt();
			while(validRatingOption != 0 && validRatingOption!= 1 && validRatingOption!= 2 &&
					validRatingOption != 3 && validRatingOption != 4 && validRatingOption != 5 && validRatingOption != 6) {
				System.out.println("Please enter a valid rating value.");
				validRatingOption = scan.nextInt();
			}
			return validRatingOption;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;
		}
	}
	
	public static int mainMenu() {
		int option = 0;
		boolean valid = true; // valid option default set to true
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);
			// main menu in console
			System.out.println("\nWELCOME TO THE MOVIE RATING PROGRAM! Please select an option from the menu below to get started.\n");
			System.out.println("+========================================================+\r\n"
					+ "|  1. REGISTER                                         |\r\n"
					+ "|  2. LOGIN                                            |\r\n"
					+ "|  3. VIEW MOVIES                                      |\r\n"
					+ "|  4. EXIT                                             |\r\n"
					+ "+========================================================+");
			option = scan.nextInt(); // get the option that user chooses
			if(option != 1 && option != 2 && option != 3 && option != 4) {
				valid = false;
			}
			while(!valid) {
				System.out.println("Please enter a valid option from the menu."); // re-prompting the user
				option = scan.nextInt();
				if(option == 1 || option == 2 || option == 3 || option == 4) {
					valid = true;
				} 
			}
			return option;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
			return -1;
		}
	}	

}
