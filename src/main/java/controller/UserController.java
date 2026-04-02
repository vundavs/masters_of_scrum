package controller;

import external.PaymentSystem;
import external.VerificationService;
import model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main controller for the application.
 * Contains all use case methods.
 */

public class UserController extends Controller {

    private List<User> users = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private final PaymentSystem paymentSystem;
    private final VerificationService verificationService;

    private static final String PREREGISTERED_USERS_FILE_PATH = "src/main/resources/preregistered_users.csv";
    private static final String PREREGISTERED_ADMIN_FILE_PATH = "src/main/resources/preregistered_admins.csv";


    /**
     * Creates a new EventsApp instance.
     *
     * @param paymentSystem       the payment system to use
     * @param verificationService the verification service to use
     */
    public UserController(PaymentSystem paymentSystem, VerificationService verificationService) {
        this.paymentSystem = paymentSystem;
        this.verificationService = verificationService;
        addPreregisteredUsers();
    }

    /**
     * Logs in a user by prompting for email and password via the view.
     * The user must not already be logged in.
     */
    public void login() {
        if (!checkCurrentUserIsGuest()) {
            view.displayError("Already logged in.");
            return;
        }

        String email = view.getInput("Enter email: ");
        String password = view.getInput("Enter password: ");

        for (User user : users) {
            if (email.equals(user.getEmail()) && (password.equals(user.getPassword()))) {
                currentUser = user;
                view.displaySuccess("Successfully logged in.");
                return;
            }
        }

        view.displayError("Incorrect email and/or password.");
    }

    /**
     * Logs out the currently logged-in user.
     * A user must be logged in to perform this action.
     */
    public void logout() {
        if (checkCurrentUserIsGuest()) {
            view.displayError("Not logged in.");
            return;
        }

        currentUser = null;

        view.displaySuccess("Logged out.");
    }

    /**
     * Registers a new entertainment provider by prompting for details via the view.
     * The user must not already be logged in. Verifies the business number
     * and logs the new provider in on success.
     */
    public void registerEntertainmentProvider() {
        if (!checkCurrentUserIsGuest()) {
            view.displayError("Already logged in. Can't register entertainment provider.");
            return;
        }

        String email = view.getInput("Enter email: ");
        String password = view.getInput("Enter password: ");
        String orgName = view.getInput("Enter organisation name: ");
        String businessNumber = view.getInput("Enter business number: ");
        String name = view.getInput("Enter name: ");
        String description = view.getInput("Enter description: ");

        if (EPAccountAlreadyExists(email, orgName, businessNumber)) {
            view.displayError("Entertainment Provider already exists.");
            return;
        }

        if (!verificationService.verifyEntertainmentProvider(businessNumber)) {
            view.displayError("Unable to verify entertainment provider.");
            return;
        }

        EntertainmentProvider ep = new EntertainmentProvider(email, password, orgName, businessNumber, name, description);

        addUser(ep);
        currentUser = ep;
        view.displaySuccess("Successfully registered Entertainment Provider.");
        return;
    }

    /**
     * Checks if an entertainment provider account already exists with the
     * given email, organisation name, or business number.
     *
     * @param email          the email to check
     * @param orgName        the organisation name to check
     * @param businessNumber the business number to check
     * @return true if a matching entertainment provider already exists
     */
    private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
        for (User user : users) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider ep = (EntertainmentProvider) user;
                if (ep.getEmail().equals(email) ||
                        ep.getOrgName().equals(orgName) ||
                        ep.getBusinessNumber().equals(businessNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a user to the system's list of registered users.
     *
     * @param user the user to add
     */
    private void addUser(User user) {
        users.add(user);
        return;
    }


    /**
     * Updates the preferences of the currently logged-in student
     * by prompting for preference keywords via the view.
     */
    public void editPreferences() {
        if (!checkCurrentUserIsStudent()){
            view.displayError("User must be a student to update preferences.");
            return;
        }

        Student student = (Student) currentUser;
        StudentPreferences preferences = student.getPreferences();

        String input = view.getInput("Enter new preferences e.g. (music, dance, movie, sports): ");
        preferences.updatePreferences(input);

        view.displaySuccess("Successfully updated preferences.");
        return;
    }

    /**
     * Reads preregistered students and admin staff from CSV files
     * and adds them to the system's user list.
     */
    private void addPreregisteredUsers() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(PREREGISTERED_USERS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String email = fields[0].trim();
                    String password = fields[1].trim();
                    String name = fields[2].trim();
                    int phoneNumber = Integer.parseInt(fields[3].trim());
                    addUser(new Student(email, password, name, phoneNumber));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load preregistered users: "
                    + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid phone number in users file: "
                    + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(PREREGISTERED_ADMIN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    String email = fields[0].trim();
                    String password = fields[1].trim();
                    String name = fields[2].trim();
                    addUser(new AdminStaff(email, password, name));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load preregistered admins: "
                    + e.getMessage());
        }
    }

    /**
     * Finds the entertainment provider who owns the event with the given number.
     *
     * @param eventNumber the ID of the event to search for
     * @return the EntertainmentProvider who owns the event, or null if not found
     */
    private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
        for (User user : users) {
            if (user instanceof EntertainmentProvider) {
                EntertainmentProvider ep = (EntertainmentProvider) user;

                for (Event event : ep.getEvents()) {
                    if (event.getEventID() == eventNumber) {
                        return ep;
                    }
                }
            }
        }
        return null;
    }

}