import model.BookingStatus;
import model.EntertainmentProvider;
import model.Performance;
import model.PerformanceStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Cancel Performance use case.
 */
public class CancelPerformanceSystemTests extends TestSystemBase {

    @Test
    void testCancelPerformanceSuccessfully() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        epController.cancelPerformance();

        assertEquals(PerformanceStatus.CANCELLED, p.getStatus(),
                "Performance should be CANCELLED after cancellation");
        assertTrue(view.hasSuccessContaining("Cancellation Successful"),
                "Success message should be displayed");
    }

    @Test
    void testCancelPerformanceWithBookingsRefundsStudents() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        // Student books the performance first
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "2");
        bookingController.bookPerformance();
        assertEquals(1, bookings.size(), "Booking should exist before cancellation");

        // EP cancels the performance
        userController.logout();
        epController.setCurrentUser(null);
        bookingController.setCurrentUser(null);
        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);

        view.addInputs(String.valueOf(p.getPerformanceId()), "Sorry, the event is cancelled.");
        epController.cancelPerformance();

        assertEquals(BookingStatus.CANCELLEDBYPROVIDER, bookings.get(0).getStatus(),
                "Booking should be CANCELLEDBYPROVIDER after performance cancellation");
        assertEquals(PerformanceStatus.CANCELLED, p.getStatus(),
                "Performance should be CANCELLED");
    }

    @Test
    void testCancelPerformanceFailsWhenNotLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        view.addInputs(String.valueOf(p.getPerformanceId()), "Cancelled");
        epController.cancelPerformance();

        assertEquals(PerformanceStatus.ACTIVE, p.getStatus(),
                "Performance should still be ACTIVE");
        assertTrue(view.hasErrorContaining("entertainment provider"),
                "Error should mention EP login requirement");
    }

    @Test
    void testCancelPerformanceFailsWhenStudentLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "Cancelled");
        epController.cancelPerformance();

        assertEquals(PerformanceStatus.ACTIVE, p.getStatus(),
                "Performance should still be ACTIVE when student tries to cancel");
    }

    @Test
    void testCancelPerformanceFailsForWrongEP() {
        TestView view = new TestView();
        initControllers(view);

        // Register two EPs
        EntertainmentProvider ep1 = registerEP(view);
        Performance p = createFuturePerformance(ep1);

        // Register a second EP
        view.addInputs("ep2@other.com", "ep2pass", "Other Co",
                "0987654321", "Jane", "Other events");
        userController.registerEntertainmentProvider();

        // Login as EP2 and try to cancel EP1's performance
        loginAsEP("ep2@other.com", "ep2pass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "Cancelled");
        epController.cancelPerformance();

        assertEquals(PerformanceStatus.ACTIVE, p.getStatus(),
                "Performance should still be ACTIVE when wrong EP tries to cancel");
        assertTrue(view.hasErrorContaining("does not belong"),
                "Error should mention performance does not belong to this EP");
    }

    @Test
    void testCancelPerformanceFailsForNonExistentId() {
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);

        view.addInputs("99999", "Cancelled");
        epController.cancelPerformance();

        assertTrue(view.hasErrorContaining("does not exist"),
                "Error should mention performance not found");
    }

    @Test
    void testCancelPerformanceFailsForAlreadyHappenedPerformance() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "Cancelled");
        epController.cancelPerformance();

        assertTrue(view.hasErrorContaining("already happened"),
                "Error should mention performance has already happened");
    }
}
