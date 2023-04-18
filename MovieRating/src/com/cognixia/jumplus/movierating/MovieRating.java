package com.cognixia.jumplus.movierating;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.cognixia.jumplus.dao.Movie;
import com.cognixia.jumplus.dao.MovieDao;
import com.cognixia.jumplus.dao.MovieDaoSql;
import com.cognixia.jumplus.dao.User;
import com.cognixia.jumplus.dao.UserDao;
import com.cognixia.jumplus.dao.UserDaoSql;

public class MovieRating {
	
	private static MovieDao movieDao;
	
	// global boolean value to keep track if user logs out
	private static boolean isLogin;

	// Keeps track of the user for the application
	private static User userFound;
	private static UserDao userDao;

	public static void main(String[] args) {
		int validOption = mainMenu();
		while(validOption == -1) { // loop here if user enters a string
			validOption = mainMenu();
		}
		
		// branch to other functions when a particular option is chosen
		while(validOption != 4) {
			switch(validOption) {
				case 1:
					registrationMenu();
					break;
				case 2:
					if(login()) {
						while(isLogin) {
							int validMovieOption = userMenu();
							while(validMovieOption == -1) {
								validMovieOption = userMenu();
							}
							
							if(validMovieOption != movieDao.getAllMovies().size()+1) {
								int validRatingOption = ratingMenu(validMovieOption);
								while(validRatingOption == -1) {
									validRatingOption = ratingMenu(validMovieOption);
								}
								if(validRatingOption != 6) {
									// rate the movie using sql
									addMovieRating(validMovieOption, validRatingOption);
								}

							} else {
								isLogin = false;
								userFound = null; // reset user
							}
						}
						
					} else {
						System.out.println("Couldn't find that user. Please try again!");
					}
					break;
				case 3:
					viewMovies(movieDao.getAllMovies());
					break;
			}
			validOption = mainMenu();
			while(validOption == -1) { // loop here if user enters a string
				validOption = mainMenu();
			}
		}
		System.out.println("Terminating the program...");
	}
	
	private static void addMovieRating(int movieId, int rating) {
		if(userDao.addMovieForRating(userFound, movieId, rating)) {
			System.out.println("Congratulations! You have given the movie " + movieDao.getMovieById(movieId).get().getTitle() + " with a rating of " + rating);
			
			// Need to update the average rating for that movie
			double avgRating = userDao.getMovieRatings(movieId).stream()
					.mapToInt(a -> a)
					.average().orElse(0);
			if(!movieDao.updateMovie(avgRating, movieId)) {
				System.out.println("Could not update the average rating for the movie. Sorry!");
			} 
			
		} else {
			System.out.println("It looks like you've given this movie a rating of: " + userDao.getMovieRating(userFound, movieId));
			System.out.println("Would you like to update?(y=yes or n=no)");
			Scanner scan = new Scanner(System.in);
			String option = scan.next();
			while(!option.equals("y") && !option.equals("n")) {
				System.out.println("Please enter 'y' or 'n'");
				option = scan.next();
			}
			if(option.equals("y")) {
				System.out.println("Now updating...");
				if(userDao.updateMovieRating(userFound, movieId, rating)) {
					System.out.println("Congratulations! You have successfully updated the rating for you movie!\n " 
							+ movieDao.getMovieById(movieId).get().getTitle() + ", rating: " + rating);
					// Need to update the average rating for that movie
					double avgRating = userDao.getMovieRatings(movieId).stream()
							.mapToInt(a -> a)
							.average().orElse(0);
					if(!movieDao.updateMovie(avgRating, movieId)) {
						System.out.println("Could not update the average rating for the movie. Sorry!");
					} 
				} else {
					System.out.println("Could not update the rating of your movie");
				}
			} else if(option.equals("n")) {
				System.out.println("You decided not to update!");
			}
		}
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
			
			Optional<User> userToFind = userDao.validateUser(username, password);
			
			// check if the optional has something
			if(userToFind.isPresent()) {
				userFound = userToFind.get();
				System.out.println("----------------------------------------------------------------");
				System.out.println("User login Success! Welcome " + userFound.getFirstName() + "!");
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
	
	public static int userMenu() {
		int movieOption = 0;
		Scanner scan = null;
		
		try {
			List<Movie> moviesList = movieDao.getAllMovies();
			viewMovies(moviesList);
			System.out.println("Please select a movie to rate or exit.");
			scan = new Scanner(System.in);
			movieOption = scan.nextInt();
			
			while(!(movieOption > 0 && movieOption <= moviesList.size() + 1 )) { // re-prompting user
				System.out.println("Please enter a valid movie id from the list or exit.");
				movieOption = scan.nextInt();
			}
			return movieOption;
		} catch(InputMismatchException e) {
			System.out.print("Input must be a number. Please try again.\n");
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
		User user = new User(1, firstName, lastName, email, username, password);
		if(userDao.createUser(user)) {
			System.out.println("Successfully created user!");
		} else {
			System.out.println("Could not create user. Please try again.");
		}
	}
	
	public static int mainMenu() {
		int option = 0;
		boolean valid = true; // valid option default set to true
		Scanner scan = null;
		// instantiating the Dao classes and setting the connections
		userDao = new UserDaoSql();
		movieDao = new MovieDaoSql();
		try {
			userDao.setConnection();
			movieDao.setConnection();
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
		}  catch(ClassNotFoundException | IOException | SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	//Source: https://www.baeldung.com/java-email-validation-regex#:~:text=The%20simplest%20regular%20expression%20to,otherwise%2C%20the%20result%20is%20false.
	public static boolean patternMatches(String emailAddress, String regexPattern) {
	    return Pattern.compile(regexPattern)
	      .matcher(emailAddress)
	      .matches();
	}
	
	private static void viewMovies(List<Movie> moviesList) {		
		// Source: https://stackoverflow.com/questions/15215326/how-can-i-create-table-using-ascii-in-a-console
		// once user is logged in, display the movies in database
		String leftAlignFormat = "| %-40s | %-11s | %-12d |%n";

		System.out.format("+=======================================================================+%n");
		System.out.format("| Movie                                    | Avg. Rating | # of Ratings |%n");
		System.out.format("+------------------------------------------+-------------+--------------+%n");
		int counter = 1;
		for (Movie m: moviesList) {
			String avgRating = "";
			if(userDao.getMovieRateCount(m.getId()) == 0) {
				avgRating = "N/A";
			} else {
				avgRating += m.getAvgRating();
			}
		    System.out.format(leftAlignFormat, counter + ". " + m.getTitle(), avgRating, userDao.getMovieRateCount(m.getId()));
			counter++;
		}
		System.out.format("| " + counter + ". EXIT                                                              |%n");
		System.out.format("+=======================================================================+%n");
	}
}
