package controller;

import external.PaymentSystem;
import external.VerificationService;
import model.Booking;
import model.Performance;
import model.User;
import view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Top-level controller that drives the main application loop and routes the
 * user to the correct role-specific menu.
 *
 * <p>Contains inner enums for each user type's menu options (as shown on the
 * class diagram). Holds references to all sub-controllers (1-1) and to the
 * shared {@link Performance} and {@link Booking} collections that are passed
 * into {@link EventPerformanceController} and {@link BookingController}.
 */
public class MenuController extends Controller {

    // Enums: one per user role

    /** Menu options presented to unauthenticated users. */
    public enum GuestMenuOptions {
        LOGIN,
        REGISTER_EP
    }

    /** Menu options presented to logged-in students. */
    public enum StudentMenuOptions {
        LOGOUT,
        SEARCH_FOR_PERFORMANCES,
        VIEW_PERFORMANCE,
        REVIEW_PERFORMANCE,
        EDIT_PREFERENCES,
        BOOK_EVENT,
        CANCEL_BOOKING
    }

    /** Menu options presented to logged-in entertainment providers. */
    public enum EPMenuOptions {
        LOGOUT,
        SEARCH_FOR_PERFORMANCES,
        VIEW_PERFORMANCE,
        CREATE_EVENT,
        CANCEL_PERFORMANCE
    }

    /** Menu options presented to logged-in admin staff. */
    public enum AdminMenuOptions {
        LOGOUT,
        SEARCH_FOR_PERFORMANCES,
        VIEW_PERFORMANCE,
        SPONSOR_PERFORMANCE
    }

    // Sub-controllers

    private final UserController userController;
    private final EventPerformanceController eventPerformanceController;
    private final BookingController bookingController;

    /**
     * Shared list of performances: initialised here and passed to both
     * EventPerformanceController and BookingController so they operate on
     * the same data.
     */
    private final List<Performance> performances;

    /** Shared list of all bookings in the system. */
    private final List<Booking> bookings;

    // Constructor

    /**
     * Constructs a MenuController, wiring together all sub-controllers with
     * a shared view, payment system, verification service, and shared
     * performance/booking collections.
     *
     * @param view                the view used for all user interaction
     * @param paymentSystem       the payment system for bookings and refunds
     * @param verificationService the verification service for EP registration
     */
    public MenuController(View view, PaymentSystem paymentSystem,
                          VerificationService verificationService) {
        assert view != null : "View must not be null";
        assert paymentSystem != null : "PaymentSystem must not be null";
        assert verificationService != null : "VerificationService must not be null";

        this.view = view;
        this.performances = new ArrayList<>();
        this.bookings = new ArrayList<>();

        // Wire sub-controllers, sharing the same view and collections
        this.userController = new UserController(paymentSystem, verificationService);
        this.userController.view = view;

        this.eventPerformanceController = new EventPerformanceController(view, performances);

        this.bookingController = new BookingController(paymentSystem, view, performances, bookings);
    }

    // Main application loop
    
    /**
     * Starts the main application loop.
     *
     * <p>On each iteration the current user's role is checked and the
     * appropriate handle*Menu() method is called. The loop continues until
     * one of those methods returns {@code false}.
     */
    public void mainMenu() {
        boolean keepRunning = true;
        while (keepRunning) {
            syncCurrentUser();

            if (checkCurrentUserIsGuest()) {
                keepRunning = handleGuestMainMenu();
            } else if (checkCurrentUserIsStudent()) {
                keepRunning = handleStudentMainMenu();
            } else if (checkCurrentUserIsEntertainmentProvider()) {
                keepRunning = handleEntertainmentProviderMainMenu();
            } else if (checkCurrentUserIsAdmin()) {
                keepRunning = handleAdminStaffMainMenu();
            }
        }
    }

    // Menu handlers for each role

    /**
     * Displays the guest menu, reads the user's selection, and delegates to
     * the appropriate use-case method.
     *
     * @return {@code true} to keep the main loop running; {@code false} to exit
     */
    protected boolean handleGuestMainMenu() {
        view.displaySuccess("=== Guest Menu ===");
        List<GuestMenuOptions> options = Arrays.asList(GuestMenuOptions.values());
        int choice = selectFromMenu(options, "Enter option number: ");

        if (choice == -1) {
            view.displayError("Invalid selection. Please try again.");
            return true;
        }

        switch (options.get(choice)) {
            case LOGIN:
                userController.login();
                syncCurrentUser();
                break;
            case REGISTER_EP:
                userController.registerEntertainmentProvider();
                syncCurrentUser();
                break;
            default:
                view.displayError("Unknown option.");
        }

        return true;
    }

    /**
     * Displays the student menu, reads the user's selection, and delegates to
     * the appropriate use-case method.
     *
     * @return {@code true} to keep the main loop running; {@code false} to exit
     */
    protected boolean handleStudentMainMenu() {
        view.displaySuccess("=== Student Menu ===");
        List<StudentMenuOptions> options = Arrays.asList(StudentMenuOptions.values());
        int choice = selectFromMenu(options, "Enter option number: ");

        if (choice == -1) {
            view.displayError("Invalid selection. Please try again.");
            return true;
        }

        switch (options.get(choice)) {
            case LOGOUT:
                userController.logout();
                syncCurrentUser();
                break;
            case SEARCH_FOR_PERFORMANCES:
                eventPerformanceController.searchForPerformances();
                break;
            case VIEW_PERFORMANCE:
                eventPerformanceController.viewPerformance();
                break;
            case REVIEW_PERFORMANCE:
                bookingController.reviewPerformance();
                break;
            case EDIT_PREFERENCES:
                userController.editPreferences();
                break;
            case BOOK_EVENT:
                bookingController.bookPerformance();
                break;
            case CANCEL_BOOKING:
                bookingController.cancelBooking();
                break;
            default:
                view.displayError("Unknown option.");
        }

        return true;
    }

    /**
     * Displays the entertainment provider menu, reads the user's selection,
     * and delegates to the appropriate use-case method.
     *
     * @return {@code true} to keep the main loop running; {@code false} to exit
     */
    protected boolean handleEntertainmentProviderMainMenu() {
        view.displaySuccess("=== Entertainment Provider Menu ===");
        List<EPMenuOptions> options = Arrays.asList(EPMenuOptions.values());
        int choice = selectFromMenu(options, "Enter option number: ");

        if (choice == -1) {
            view.displayError("Invalid selection. Please try again.");
            return true;
        }

        switch (options.get(choice)) {
            case LOGOUT:
                userController.logout();
                syncCurrentUser();
                break;
            case SEARCH_FOR_PERFORMANCES:
                eventPerformanceController.searchForPerformances();
                break;
            case VIEW_PERFORMANCE:
                eventPerformanceController.viewPerformance();
                break;
            case CREATE_EVENT:
                eventPerformanceController.createEvent();
                break;
            case CANCEL_PERFORMANCE:
                eventPerformanceController.cancelPerformance();
                break;
            default:
                view.displayError("Unknown option.");
        }

        return true;
    }

    /**
     * Displays the admin staff menu, reads the user's selection, and delegates
     * to the appropriate use-case method.
     *
     * @return {@code true} to keep the main loop running; {@code false} to exit
     */
    protected boolean handleAdminStaffMainMenu() {
        view.displaySuccess("=== Admin Staff Menu ===");
        List<AdminMenuOptions> options = Arrays.asList(AdminMenuOptions.values());
        int choice = selectFromMenu(options, "Enter option number: ");

        if (choice == -1) {
            view.displayError("Invalid selection. Please try again.");
            return true;
        }

        switch (options.get(choice)) {
            case LOGOUT:
                userController.logout();
                syncCurrentUser();
                break;
            case SEARCH_FOR_PERFORMANCES:
                eventPerformanceController.searchForPerformances();
                break;
            case VIEW_PERFORMANCE:
                eventPerformanceController.viewPerformance();
                break;
            case SPONSOR_PERFORMANCE:
                eventPerformanceController.sponsorPerformance();
                break;
            default:
                view.displayError("Unknown option.");
        }

        return true;
    }

    // Helper methods

    /**
     * Propagates the current user (maintained authoritatively by
     * {@link UserController}) to this controller and all sub-controllers so
     * that role checks remain consistent across the system.
     */
    private void syncCurrentUser() {
        User user = userController.getCurrentUser();
        this.currentUser = user;
        eventPerformanceController.setCurrentUser(user);
        bookingController.setCurrentUser(user);
    }

    /**
     * Adds a user directly to the UserController's registered-user list.
     *
     * <p>Used to pre-register students and admin staff (who are not
     * registered through the public registration flow).
     *
     * @param user the user to register
     */
    public void addUser(User user) {
        assert user != null : "User must not be null";
        userController.addUser(user);
    }

    /**
     * Returns the shared list of performances.
     *
     * @return the shared performance list
     */
    public List<Performance> getPerformances() {
        return performances;
    }

    /**
     * Returns the shared list of bookings.
     *
     * @return the shared booking list
     */
    public List<Booking> getBookings() {
        return bookings;
    }
}
