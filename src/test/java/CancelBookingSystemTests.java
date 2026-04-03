import model.Booking;
import model.BookingStatus;
import model.EntertainmentProvider;
import model.Performance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Cancel Booking use case.
 */
public class CancelBookingSystemTests extends TestSystemBase {

    // Helper: book a performance as student1 and return the created booking.
    private Booking bookAsStudent1(TestView view, Performance p) {
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "2");
        bookingController.bookPerformance();
        return bookings.get(0);
    }

    @Test
    void testCancelBookingSuccessfully() {
        // Books a performance then cancels it; expects CANCELLEDBYSTUDENT status
        // and a success message.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);
        Booking booking = bookAsStudent1(view, p);

        view.addInputs(String.valueOf(booking.getBookingNumber()));
        bookingController.cancelBooking();

        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus(),
                "Booking status should be CANCELLEDBYSTUDENT after cancellation");
        assertTrue(view.hasSuccessContaining("cancelled successfully"),
                "Success message should be displayed");
    }

    @Test
    void testCancelledBookingIsNoLongerActive() {
        // After cancellation the booking must not have ACTIVE status.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);
        Booking booking = bookAsStudent1(view, p);

        view.addInputs(String.valueOf(booking.getBookingNumber()));
        bookingController.cancelBooking();

        assertTrue(booking.getStatus() != BookingStatus.ACTIVE,
                "Booking should not be ACTIVE after cancellation");
    }

    // Authentication / authorisation failures

    @Test
    void testCancelBookingFailsWhenNotLoggedIn() {
        // No user logged in — cancel should be rejected immediately.
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("1");
        bookingController.cancelBooking();

        assertTrue(view.hasErrorContaining("student"),
                "Error should state that a student must be logged in");
    }

    @Test
    void testCancelBookingFailsWhenEPLoggedIn() {
        // Entertainment providers are not allowed to cancel bookings.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("1");
        bookingController.cancelBooking();

        assertTrue(view.hasErrorContaining("student"),
                "Error should state that a student must be logged in");
    }

    @Test
    void testCancelBookingFailsForOtherStudentsBooking() {
        // student2 should not be able to cancel a booking that belongs to student1.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        // student1 books the performance
        Booking booking = bookAsStudent1(view, p);

        // logout student1, login student2
        userController.logout();
        epController.setCurrentUser(null);
        bookingController.setCurrentUser(null);
        loginAsStudent("student2@uni.ac.uk", "pass2", view, epController, bookingController);

        view.addInputs(String.valueOf(booking.getBookingNumber()));
        bookingController.cancelBooking();

        assertEquals(BookingStatus.ACTIVE, booking.getStatus(),
                "Booking should remain ACTIVE when another student tries to cancel it");
        assertTrue(view.hasErrorContaining("does not belong"),
                "Error should state that the booking does not belong to this student");
    }

    // Invalid booking number
    
    @Test
    void testCancelBookingFailsForNonExistentBookingNumber() {
        // Booking number 99999 does not exist in the system.
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);

        view.addInputs("99999");
        bookingController.cancelBooking();

        assertTrue(view.hasErrorContaining("does not exist"),
                "Error should state that the booking number does not exist");
    }

    @Test
    void testCancelBookingFailsWithNonNumericInput() {
        // Entering a non-numeric booking number should be rejected with an error.
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);

        view.addInputs("abc");
        bookingController.cancelBooking();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should state that the booking number is invalid");
    }

    // Already-cancelled booking

    @Test
    void testCancelBookingFailsWhenAlreadyCancelled() {
        // Attempting to cancel an already-cancelled booking should be rejected.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);
        Booking booking = bookAsStudent1(view, p);

        // First cancellation — should succeed
        view.addInputs(String.valueOf(booking.getBookingNumber()));
        bookingController.cancelBooking();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus(),
                "First cancellation should succeed");

        // Second cancellation — should fail
        view.addInputs(String.valueOf(booking.getBookingNumber()));
        bookingController.cancelBooking();

        assertTrue(view.hasErrorContaining("not active"),
                "Error should state that the booking is no longer active");
    }
}
