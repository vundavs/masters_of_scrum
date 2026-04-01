import controller.BookingController;
import controller.EventPerformanceController;
import controller.UserController;
import external.MockPaymentSystem;
import external.MockVerificationService;
import model.AdminStaff;
import model.Booking;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;
import model.Performance;
import model.Student;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all system tests.
 *
 * <p>Initialises the shared controller instances, pre-registered users, and
 * shared data collections.  Subclasses call {@link #initControllers(TestView)}
 * in each test method (or in a {@code @BeforeEach} setup) before exercising the
 * system under test.
 *
 * <p>Pre-registered accounts available in every test:
 * <ul>
 *   <li>Student 1 — email: {@code student1@uni.ac.uk}, password: {@code pass1}
 *   <li>Student 2 — email: {@code student2@uni.ac.uk}, password: {@code pass2}
 *   <li>Admin     — email: {@code admin@uni.ac.uk},    password: {@code adminpass}
 * </ul>
 *
 * <p>An entertainment provider can be registered via {@link #registerEP(TestView)},
 * which produces:
 * <ul>
 *   <li>EP email: {@code ep@musicco.com}, password: {@code eppass}
 * </ul>
 */
public abstract class TestSystemBase {

    // -----------------------------------------------------------------------
    // Shared state (accessible to all test subclasses)
    // -----------------------------------------------------------------------

    protected UserController userController;
    protected EventsPerformanceController epController;
    protected BookingController bookingController;

    protected List<Performance> performances;
    protected List<Booking> bookings;
    protected List<Event> events;

    // -----------------------------------------------------------------------
    // Setup
    // -----------------------------------------------------------------------

    /**
     * Initialises all controllers and shared collections, wiring them together
     * with the given view.  Pre-registers two students and one admin staff
     * member.  Also resets the booking number counter so tests are independent.
     *
     * @param view the TestView to attach to all controllers
     */
    protected void initControllers(TestView view) {
        performances = new ArrayList<>();
        bookings = new ArrayList<>();
        events = new ArrayList<>();

        MockPaymentSystem paymentSystem = new MockPaymentSystem();
        MockVerificationService verificationService = new MockVerificationService();

        userController = new UserController(paymentSystem, verificationService);
        userController.view = view;

        epController = new EventsPerformanceController(view, performances);
        bookingController = new BookingController(paymentSystem, view, performances, bookings);

        // Pre-register users (students and admin are not registered via the EP flow)
        userController.addUser(new Student("student1@uni.ac.uk", "pass1", "Alice", 1111111111));
        userController.addUser(new Student("student2@uni.ac.uk", "pass2", "Bob",   2222222222));
        userController.addUser(new AdminStaff("admin@uni.ac.uk", "adminpass", "Admin User"));

        // Ensure booking numbers start from 1 for each test
        Booking.resetBookingNumberCounter();
    }

    // -----------------------------------------------------------------------
    // Reusable setup helpers
    // -----------------------------------------------------------------------

    /**
     * Registers a standard entertainment provider and returns it.
     * Leaves the EP logged in as the current user of {@code userController}.
     *
     * <p>Credentials: email {@code ep@musicco.com}, password {@code eppass}.
     *
     * @param view the view to supply inputs through
     * @return the newly registered EntertainmentProvider
     */
    protected EntertainmentProvider registerEP(TestView view) {
        // UserController.registerEntertainmentProvider() prompts in this order:
        // email, password, orgName, businessNumber (must be 10 chars), name, description
        userController.setCurrentUser(null); // ensure guest state before registering
        view.addInputs("ep@musicco.com", "eppass", "Music Co", "1234567890",
                "John Smith", "Live music events");
        userController.registerEntertainmentProvider();
        return (EntertainmentProvider) userController.getCurrentUser();
    }

    /**
     * Creates a ticketed performance that has not yet happened, adds it to the
     * shared {@code performances} and {@code events} lists, and returns it.
     *
     * @param ep the entertainment provider who owns the event
     * @return the created Performance
     */
    protected Performance createFuturePerformance(EntertainmentProvider ep) {
        Event event = new Event("Test Concert", EventType.MUSIC, true, ep);
        events.add(event);
        ep.addEvent(event);

        Performance p = new Performance(
                event,
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(7).plusHours(2),
                List.of("Band"),
                "Main Hall", 200, false, false, 100, 20.0);
        performances.add(p);
        event.addPerformance(p);
        return p;
    }

    /**
     * Creates a ticketed performance that has already happened, adds it to the
     * shared lists, and returns it.
     *
     * @param ep the entertainment provider who owns the event
     * @return the created Performance
     */
    protected Performance createPastPerformance(EntertainmentProvider ep) {
        Event event = new Event("Past Concert", EventType.MUSIC, true, ep);
        events.add(event);
        ep.addEvent(event);

        Performance p = new Performance(
                event,
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(7).plusHours(2),
                List.of("Band"),
                "Main Hall", 200, false, false, 100, 20.0);
        performances.add(p);
        event.addPerformance(p);
        return p;
    }

    /**
     * Resets all controllers to guest state, then logs in as the student with
     * the given credentials and propagates the current user to all controllers.
     *
     * @param email           student email
     * @param password        student password
     * @param view            the view to supply credentials through
     * @param epCtrl          the EventsPerformanceController to sync
     * @param bookingCtrl     the BookingController to sync
     */
    protected void loginAsStudent(String email, String password, TestView view,
                                   EventsPerformanceController epCtrl,
                                   BookingController bookingCtrl) {
        // Reset to guest state without triggering error messages
        userController.setCurrentUser(null);
        epCtrl.setCurrentUser(null);
        bookingCtrl.setCurrentUser(null);

        view.addInputs(email, password);
        userController.login();

        User user = userController.getCurrentUser();
        epCtrl.setCurrentUser(user);
        bookingCtrl.setCurrentUser(user);
    }

    /**
     * Resets all controllers to guest state, then logs in as the entertainment
     * provider with the given credentials and propagates the current user to all
     * controllers.
     *
     * @param email           EP email
     * @param password        EP password
     * @param view            the view to supply credentials through
     * @param epCtrl          the EventsPerformanceController to sync
     * @param bookingCtrl     the BookingController to sync
     */
    protected void loginAsEP(String email, String password, TestView view,
                              EventsPerformanceController epCtrl,
                              BookingController bookingCtrl) {
        userController.setCurrentUser(null);
        epCtrl.setCurrentUser(null);
        bookingCtrl.setCurrentUser(null);

        view.addInputs(email, password);
        userController.login();

        User user = userController.getCurrentUser();
        epCtrl.setCurrentUser(user);
        bookingCtrl.setCurrentUser(user);
    }
}
