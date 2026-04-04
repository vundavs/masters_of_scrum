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
 * shared data collections. Subclasses call {@link #initControllers(TestView)}
 * in each test method before exercising the system under test.
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

    protected UserController userController;
    protected EventPerformanceController epController;
    protected BookingController bookingController;

    protected List<Performance> performances;
    protected List<Booking> bookings;
    protected List<Event> events;

    /** Pre-registered student 1 — email: {@code student1@uni.ac.uk}, password: {@code pass1} */
    protected Student student1;
    /** Pre-registered student 2 — email: {@code student2@uni.ac.uk}, password: {@code pass2} */
    protected Student student2;

    /**
     * Initialises all controllers and shared collections with the given view.
     * Pre-registers two students and one admin staff member, and resets the
     * booking number counter so each test starts from a clean state.
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
        userController.setView(view);

        epController = new EventPerformanceController(view, performances);
        bookingController = new BookingController(paymentSystem, view, performances, bookings);

        // Pre-register students and admin (not registered via the public EP flow)
        student1 = new Student("student1@uni.ac.uk", "pass1", "Alice", 111111111);
        student2 = new Student("student2@uni.ac.uk", "pass2", "Bob",   222222222);
        userController.addUser(student1);
        userController.addUser(student2);
        userController.addUser(new AdminStaff("admin@uni.ac.uk", "adminpass", "Admin User"));

        Booking.resetBookingNumberCounter();
    }

    /**
     * Registers a standard entertainment provider and returns it.
     * Leaves the EP logged in on {@code userController}.
     *
     * <p>Credentials: email {@code ep@musicco.com}, password {@code eppass}.
     *
     * @param view the view to supply inputs through
     * @return the newly registered EntertainmentProvider
     */
    protected EntertainmentProvider registerEP(TestView view) {
        userController.setCurrentUser(null);
        // registerEntertainmentProvider() prompts: email, password, orgName,
        // businessNumber (must be 10 chars for MockVerificationService), name, description
        view.addInputs("ep@musicco.com", "eppass", "Music Co", "1234567890",
                "John Smith", "Live music events");
        userController.registerEntertainmentProvider();
        return (EntertainmentProvider) userController.getCurrentUser();
    }

    /**
     * Creates a ticketed future performance, adds it to the shared lists, and
     * returns it.
     *
     * @param ep the entertainment provider who owns the event
     * @return the created Performance
     */
    protected Performance createFuturePerformance(EntertainmentProvider ep) {
        Event event = new Event("Test Concert", EventType.MUSIC, true, ep);
        events.add(event);
        ep.addEvent(event);

        Performance p = new Performance(
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(7).plusHours(2),
                List.of("Band"),
                "Main Hall", 200, false, false, 100, 20.0,
                new ArrayList<>(), event);
        performances.add(p);
        event.addPerformance(p);
        return p;
    }

    /**
     * Creates a ticketed past performance, adds it to the shared lists, and
     * returns it.
     *
     * @param ep the entertainment provider who owns the event
     * @return the created Performance
     */
    protected Performance createPastPerformance(EntertainmentProvider ep) {
        Event event = new Event("Past Concert", EventType.MUSIC, true, ep);
        events.add(event);
        ep.addEvent(event);

        Performance p = new Performance(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(7).plusHours(2),
                List.of("Band"),
                "Main Hall", 200, false, false, 100, 20.0,
                new ArrayList<>(), event);
        performances.add(p);
        event.addPerformance(p);
        return p;
    }

    /**
     * Resets all controllers to guest state, logs in as the given student, and
     * propagates the current user to {@code epCtrl} and {@code bookingCtrl}.
     *
     * @param email       student email
     * @param password    student password
     * @param view        the view to supply credentials through
     * @param epCtrl      the EventPerformanceController to sync
     * @param bookingCtrl the BookingController to sync
     */
    protected void loginAsStudent(String email, String password, TestView view,
                                   EventPerformanceController epCtrl,
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

    /**
     * Resets all controllers to guest state, logs in as the given entertainment
     * provider, and propagates the current user to {@code epCtrl} and
     * {@code bookingCtrl}.
     *
     * @param email       EP email
     * @param password    EP password
     * @param view        the view to supply credentials through
     * @param epCtrl      the EventPerformanceController to sync
     * @param bookingCtrl the BookingController to sync
     */
    protected void loginAsEP(String email, String password, TestView view,
                              EventPerformanceController epCtrl,
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

    /**
     * Resets all controllers to guest state and logs in as the admin staff
     * member with the given credentials. Only syncs {@code userController} and
     * {@code epController} since admin actions go through {@code epController}.
     *
     * @param email    admin email
     * @param password admin password
     * @param view     the view to supply credentials through
     */
    protected void loginAsAdmin(String email, String password, TestView view) {
        userController.setCurrentUser(null);
        epController.setCurrentUser(null);
        bookingController.setCurrentUser(null);

        view.addInputs(email, password);
        userController.login();

        User user = userController.getCurrentUser();
        epController.setCurrentUser(user);
        bookingController.setCurrentUser(user);
    }
}
