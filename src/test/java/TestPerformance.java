import model.Booking;
import model.BookingStatus;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;
import model.Performance;
import model.PerformanceStatus;
import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Performance class.
 */
public class TestPerformance {

    private EntertainmentProvider ep;
    private Event ticketedEvent;
    private Event nonTicketedEvent;
    private Performance futurePerformance;
    private Performance pastPerformance;

    @BeforeEach
    void setUp() {
        Performance.resetPerformanceIDCounter();
        Booking.resetBookingNumberCounter();

        ep = new EntertainmentProvider("ep@test.com", "pass", "Music Co",
                "1234567890", "John", "Live music");

        ticketedEvent = new Event("Test Concert", EventType.MUSIC, true, ep);
        nonTicketedEvent = new Event("Free Show", EventType.DANCE, false, ep);

        futurePerformance = new Performance(
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(7).plusHours(2),
                List.of("Band A"),
                "Main Hall", 200, false, false,
                100, 20.0, new ArrayList<>(), ticketedEvent);

        pastPerformance = new Performance(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(7).plusHours(2),
                List.of("Band B"),
                "Old Hall", 100, false, false,
                50, 10.0, new ArrayList<>(), ticketedEvent);
    }

    // --- cancel() ---

    @Test
    void testCancelSetsStatusToCancelled() {
        futurePerformance.cancel();
        assertEquals(PerformanceStatus.CANCELLED, futurePerformance.getStatus(),
                "cancel() should set status to CANCELLED");
    }

    @Test
    void testNewPerformanceIsActive() {
        assertEquals(PerformanceStatus.ACTIVE, futurePerformance.getStatus(),
                "A new performance should have ACTIVE status");
    }

    // --- checkIfEventIsTicketed() ---

    @Test
    void testCheckIfEventIsTicketedReturnsTrue() {
        assertTrue(futurePerformance.checkIfEventIsTicketed(),
                "Should return true for a ticketed event");
    }

    @Test
    void testCheckIfEventIsTicketedReturnsFalse() {
        Performance nonTicketed = new Performance(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                List.of("Dancer"),
                "Studio", 50, false, false,
                50, 0.0, new ArrayList<>(), nonTicketedEvent);
        assertFalse(nonTicketed.checkIfEventIsTicketed(),
                "Should return false for a non-ticketed event");
    }

    // --- checkIfTicketsLeft() ---

    @Test
    void testCheckIfTicketsLeftWhenTicketsAvailable() {
        assertTrue(futurePerformance.checkIfTicketsLeft(1),
                "Should return true when tickets are available");
    }

    @Test
    void testCheckIfTicketsLeftExactAmount() {
        assertTrue(futurePerformance.checkIfTicketsLeft(100),
                "Should return true when requesting exactly all remaining tickets");
    }

    @Test
    void testCheckIfTicketsLeftWhenNoneLeft() {
        Student student = new Student("s@uni.ac.uk", "pass", "Alice", 111111111);
        Booking booking = new Booking(student, futurePerformance, 100, 2000.0);
        futurePerformance.addBooking(booking);
        assertFalse(futurePerformance.checkIfTicketsLeft(1),
                "Should return false when no tickets remain");
    }

    @Test
    void testCheckIfTicketsLeftAfterPartialSale() {
        Student student = new Student("s@uni.ac.uk", "pass", "Alice", 111111111);
        Booking booking = new Booking(student, futurePerformance, 50, 1000.0);
        futurePerformance.addBooking(booking);
        assertTrue(futurePerformance.checkIfTicketsLeft(50),
                "Should return true when exactly half tickets remain");
        assertFalse(futurePerformance.checkIfTicketsLeft(51),
                "Should return false when requesting more than remaining tickets");
    }

    // --- getFinalTicketPrice() ---

    @Test
    void testGetFinalTicketPriceWithoutSponsorship() {
        assertEquals(20.0, futurePerformance.getFinalTicketPrice(),
                "Final price should equal ticket price when not sponsored");
    }

    @Test
    void testGetFinalTicketPriceWithSponsorship() {
        futurePerformance.sponsor(5.0);
        assertEquals(15.0, futurePerformance.getFinalTicketPrice(),
                "Final price should be reduced by sponsored amount");
    }

    // --- getOrganiserEmail() ---

    @Test
    void testGetOrganiserEmail() {
        assertEquals("ep@test.com", futurePerformance.getOrganiserEmail(),
                "Should return the EP's email address");
    }

    // --- getEventTitle() ---

    @Test
    void testGetEventTitle() {
        assertEquals("Test Concert", futurePerformance.getEventTitle(),
                "Should return the event title");
    }

    // --- checkHasNotHappenedYet() ---

    @Test
    void testCheckHasNotHappenedYetForFuturePerformance() {
        assertTrue(futurePerformance.checkHasNotHappenedYet(),
                "Should return true for a future performance");
    }

    @Test
    void testCheckHasNotHappenedYetForPastPerformance() {
        assertFalse(pastPerformance.checkHasNotHappenedYet(),
                "Should return false for a past performance");
    }

    // --- hasActiveBookings() ---

    @Test
    void testHasActiveBookingsWhenNone() {
        assertFalse(futurePerformance.hasActiveBookings(),
                "Should return false when there are no bookings");
    }

    @Test
    void testHasActiveBookingsWhenActiveBookingExists() {
        Student student = new Student("s@uni.ac.uk", "pass", "Alice", 111111111);
        Booking booking = new Booking(student, futurePerformance, 2, 40.0);
        futurePerformance.addBooking(booking);
        assertTrue(futurePerformance.hasActiveBookings(),
                "Should return true when an active booking exists");
    }

    @Test
    void testHasActiveBookingsWhenBookingCancelled() {
        Student student = new Student("s@uni.ac.uk", "pass", "Alice", 111111111);
        Booking booking = new Booking(student, futurePerformance, 2, 40.0);
        futurePerformance.addBooking(booking);
        booking.cancelByStudent();
        assertFalse(futurePerformance.hasActiveBookings(),
                "Should return false when all bookings are cancelled");
    }

    // --- sponsor() ---

    @Test
    void testSponsorSetsIsSponsoredTrue() {
        futurePerformance.sponsor(10.0);
        assertTrue(futurePerformance.getIsSponsored(),
                "isSponsored should be true after sponsoring");
    }

    @Test
    void testSponsorSetsSponsoredAmount() {
        futurePerformance.sponsor(10.0);
        assertEquals(10.0, futurePerformance.getSponsoredAmount(),
                "Sponsored amount should be set correctly");
    }

    // --- review() ---

    @Test
    void testReviewAddsRating() {
        futurePerformance.review(4, "Great show!");
        assertEquals(1, futurePerformance.getReviewRatings().size(),
                "Review ratings list should have one entry");
        assertEquals(4, futurePerformance.getReviewRatings().get(0),
                "Rating should be 4");
    }

    @Test
    void testReviewAddsComment() {
        futurePerformance.review(4, "Great show!");
        assertEquals(1, futurePerformance.getReviewComments().size(),
                "Review comments list should have one entry");
        assertEquals("Great show!", futurePerformance.getReviewComments().get(0),
                "Comment should match");
    }

    @Test
    void testMultipleReviews() {
        futurePerformance.review(3, "Decent");
        futurePerformance.review(5, "Amazing!");
        assertEquals(2, futurePerformance.getReviewRatings().size(),
                "Should have two review ratings");
        assertEquals(2, futurePerformance.getReviewComments().size(),
                "Should have two review comments");
    }

    // --- addBooking() ---

    @Test
    void testAddBookingIncreasesTicketsSold() {
        Student student = new Student("s@uni.ac.uk", "pass", "Alice", 111111111);
        Booking booking = new Booking(student, futurePerformance, 3, 60.0);
        futurePerformance.addBooking(booking);
        assertEquals(3, futurePerformance.getNumTicketsSold(),
                "Tickets sold should increase by the number of tickets in the booking");
    }

    @Test
    void testAddBookingAppearsInActiveBookings() {
        Student student = new Student("s@uni.ac.uk", "pass", "Alice", 111111111);
        Booking booking = new Booking(student, futurePerformance, 2, 40.0);
        futurePerformance.addBooking(booking);
        assertTrue(futurePerformance.hasActiveBookings(),
                "Performance should show active bookings after addBooking");
    }
}