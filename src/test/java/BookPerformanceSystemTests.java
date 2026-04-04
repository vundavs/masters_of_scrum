import model.BookingStatus;
import model.EntertainmentProvider;
import model.Performance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Book Performance use case.
 */
public class BookPerformanceSystemTests extends TestSystemBase {

    @Test
    void testBookPerformanceSuccessfully() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "2");
        bookingController.bookPerformance();

        assertEquals(1, bookings.size(),
                "One booking should be created");
        assertTrue(view.hasSuccessContaining("Successful"),
                "Success message should be displayed");
    }

    @Test
    void testBookingReducesAvailableTickets() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);
        int initialTickets = p.getNumTicketsTotal();

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "3");
        bookingController.bookPerformance();

        assertEquals(initialTickets - 3, p.getNumTicketsTotal() - p.getNumTicketsSold(),
                "Available tickets should decrease after booking");
    }

    @Test
    void testBookingCreatedWithActiveStatus() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "1");
        bookingController.bookPerformance();

        assertEquals(BookingStatus.ACTIVE, bookings.get(0).getStatus(),
                "New booking should have ACTIVE status");
    }

    @Test
    void testBookingFailsWhenNotLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        view.addInputs(String.valueOf(p.getPerformanceId()), "1");
        bookingController.bookPerformance();

        assertEquals(0, bookings.size(),
                "No booking should be created when not logged in");
        assertTrue(view.hasErrorContaining("student"),
                "Error should mention student requirement");
    }

    @Test
    void testBookingFailsForNonTicketedEvent() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        // Create a free/non-ticketed performance
        model.Event freeEvent = new model.Event(
                "Free Concert", model.EventType.MUSIC, false, ep);
        events.add(freeEvent);
        ep.addEvent(freeEvent);
        Performance p = new Performance(
                java.time.LocalDateTime.now().plusDays(7),
                java.time.LocalDateTime.now().plusDays(7).plusHours(2),
                java.util.List.of("Band"),
                "City Park", 500, true, false, 0, 0.0,
                java.util.List.of(), freeEvent);
        performances.add(p);
        freeEvent.addPerformance(p);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "1");
        bookingController.bookPerformance();

        assertEquals(0, bookings.size(),
                "Should not book a non-ticketed event");
        assertTrue(view.hasErrorContaining("not ticketed"),
                "Error should mention event is not ticketed");
    }

    @Test
    void testBookingFailsWhenNoTicketsLeft() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        // Create performance with only 2 tickets
        model.Event event = new model.Event("Tiny Gig", model.EventType.MUSIC, true, ep);
        events.add(event);
        ep.addEvent(event);
        Performance p = new Performance(
                java.time.LocalDateTime.now().plusDays(7),
                java.time.LocalDateTime.now().plusDays(7).plusHours(2),
                java.util.List.of("Band"),
                "Tiny Venue", 10, false, false, 2, 20.0,
                java.util.List.of(), event);
        performances.add(p);
        event.addPerformance(p);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "3");
        bookingController.bookPerformance();

        assertEquals(0, bookings.size(),
                "Should not book more tickets than available");
        assertTrue(view.hasErrorContaining("no tickets left"),
                "Error should mention no tickets left");
    }

    @Test
    void testBookingFailsWithInvalidPerformanceId() {
        TestView view = new TestView("student1@uni.ac.uk", "pass1");
        initControllers(view);
        userController.login();
        bookingController.setCurrentUser(userController.getCurrentUser());

        view.addInputs("99999", "1");
        bookingController.bookPerformance();

        assertEquals(0, bookings.size(),
                "No booking should be created for non-existent performance");
        assertTrue(view.hasErrorContaining("does not exist"),
                "Error should mention performance not found");
    }

    @Test
    void testBookingRecordDisplayed() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "1");
        bookingController.bookPerformance();

        assertTrue(!view.getBookingRecords().isEmpty(),
                "Booking record should be displayed after successful booking");
    }

    @Test
    void testEPCannotBookPerformance() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "1");
        bookingController.bookPerformance();

        assertEquals(0, bookings.size(),
                "EP should not be able to book a performance");
        assertTrue(view.hasErrorContaining("student"),
                "Error should mention student requirement");
    }
}
