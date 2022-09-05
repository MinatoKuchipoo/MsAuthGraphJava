package graphtutorial;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class App {

	public static void main(String[] args) {
	    System.out.println("Java Graph Tutorial");
	    System.out.println();

	    final Properties oAuthProperties = new Properties();
	    try {
	        oAuthProperties.load(App.class.getResourceAsStream("../neonOauth.properties"));
	    } catch (IOException e) {
	        System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
	        return;
	    }

	    initializeGraph(oAuthProperties);

	    greetUser();

	    Scanner input = new Scanner(System.in);

	    int choice = -1;

	    while (choice != 0) {
	        System.out.println("Please choose one of the following options:");
	        System.out.println("0. Exit");
	        System.out.println("1. Display access token");
	        System.out.println("2. List my inbox");
	        System.out.println("3. Send mail");
	        System.out.println("4. List users (required app-only)");
	        System.out.println("5. Make a Graph call");

	        try {
	            choice = input.nextInt();
	        } catch (InputMismatchException ex) {
	            // Skip over non-integer input
	        }

	        input.nextLine();

	        // Process user choice
	        switch(choice) {
	            case 0:
	                // Exit the program
	                System.out.println("Goodbye...");
	                break;
	            case 1:
	                // Display access token
	                displayAccessToken();
	                break;
	            case 2:
	                // List emails from user's inbox
	                listInbox();
	                break;
	            case 3:
	                // Send an email message
	                sendMail();
	                break;
	            case 4:
	                // List users
	                listUsers();
	                break;
	            case 5:
	                // Run any Graph code
	                makeGraphCall();
	                break;
	            default:
	                System.out.println("Invalid choice");
	        }
	    }

	    input.close();
	}
	
	private static void initializeGraph(Properties properties) {
	    try {
	        Graph.initializeGraphForUserAuth(properties,
	            challenge -> System.out.println(challenge.getMessage()));
	    } catch (Exception e)
	    {
	        System.out.println("Error initializing Graph for user auth");
	        System.out.println(e.getMessage());
	    }
	}

	private static void greetUser() {
	    // TODO
	}

	private static void displayAccessToken() {
	    try {
	        final String accessToken = Graph.getUserToken();
	        System.out.println("Access token: " + accessToken);
	    } catch (Exception e) {
	        System.out.println("Error getting access token");
	        System.out.println(e.getMessage());
	    }
	}

	private static void listInbox() {
	    // TODO
	}

	private static void sendMail() {
	    // TODO
	}

	private static void listUsers() {
	    // TODO
	}

	private static void makeGraphCall() {
	    // TODO
	}
}
