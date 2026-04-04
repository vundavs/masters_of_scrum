import model.Booking;
import model.BookingStatus;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;
import model.Performance;
import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Booking class.
 */
public class TestBooking {

    private Student student;
    private Performance performance;

    @BeforeEach
    void setUp() {
        Booking.resetBookingNumberCounter();
        Performance.resetPerformanceIDCounter();
        Event.resetEventIDCounter();

        student = new Student("student@uni.ac.uk", "pass", "Alice", 712345678);
        EntertainmentProvider ep = new EntertainmentProvider(
                "ep@org.com", "pass", "Music Co", "1234567890", "Bob", "Concerts");
        Event event = new Event("Jazz Night", EventType.MUSIC, true, ep);
        performance = new Performance(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                List.of("Jazz Trio"),
                "Edinburgh Venue", 100, false, false, 50, 20.0,
                new java.util.ArrayList<>(), event);
    }

    @Test
    void testBookingCreatedWithActiveStatus() {
        Booking booking = new Booking(student, performance, 2, 40.0);
        assertEquals(BookingStatus.ACTIVE, booking.getStatus(),
                "New booking should have ACTIVE status");
    }

    @Test
    void testBookingNumberAutoIncrement() {
        Booking b1 = new Booking(student, performance, 1, 20.0);
        Booking b2 = new Booking(student, performance, 1, 20.0);
        assertEquals(b1.getBookingNumber() + 1, b2.getBookingNumber(),
                "Booking numbers should auto-increment");
    }

    @Test
    void testGetNumTickets() {
        Booking booking = new Booking(student, performance, 3, 60.0);
        assertEquals(3, booking.getNumTickets(),
                "Should return correct number of tickets");
    }

    @Test
    void testGetAmountPaid() {
        Booking booking = new Booking(student, performance, 2, 40.0);
        assertEquals(40.0, booking.getAmountPaid(), 0.001,
                "Should return correct amount paid");
    }

    @Test
    void testGetStudent() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        assertEquals(student, booking.getStudent(),
                "Should return the correct student");
    }

    @Test
    void testGetPerformance() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        assertEquals(performance, booking.getPerformance(),
                "Should return the correct performance");
    }

    @Test
    void testCancelByStudent() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        booking.cancelByStudent();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus(),
                "Status should be CANCELLEDBYSTUDENT after student cancellation");
    }

    @Test
    void testCancelByProvider() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        booking.cancelByProvider();
        assertEquals(BookingStatus.CANCELLEDBYPROVIDER, booking.getStatus(),
                "Status should be CANCELLEDBYPROVIDER after provider cancellation");
    }

    @Test
    void testCancelPaymentFailed() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        booking.cancelPaymentFailed();
        assertEquals(BookingStatus.PAYMENTFAILED, booking.getStatus(),
                "Status should be PAYMENTFAILED after payment failure");
    }

    @Test
    void testCheckBookedByStudentCorrectEmail() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        assertTrue(booking.checkBookedByStudent("student@uni.ac.uk"),
                "Should return true for the correct student email");
    }

    @Test
    void testCheckBookedByStudentWrongEmail() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        assertFalse(booking.checkBookedByStudent("other@uni.ac.uk"),
                "Should return false for a different email");
    }

    @Test
    void testGetStudentDetails() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        String details = booking.getStudentDetails();
        assertTrue(details.contains("student@uni.ac.uk"),
                "Student details should contain email");
        assertTrue(details.contains("712345678"),
                "Student details should contain phone number");
    }

    @Test
    void testGenerateBookingRecord() {
        Booking booking = new Booking(student, performance, 2, 40.0);
        String record = booking.generateBookingRecord();
        assertNotNull(record, "Booking record should not be null");
        assertTrue(record.contains("student@uni.ac.uk"),
                "Record should contain student email");
        assertTrue(record.contains("Jazz Night"),
                "Record should contain event title");
        assertTrue(record.contains("2"),
                "Record should contain number of tickets");
    }

    @Test
    void testBookingDateTimeSet() {
        Booking booking = new Booking(student, performance, 1, 20.0);
        assertNotNull(booking.getBookingDateTime(),
                "Booking date time should be set on creation");
    }

    @Test
    void testZeroAmountPaidAllowed() {
        Booking booking = new Booking(student, performance, 1, 0.0);
        assertEquals(0.0, booking.getAmountPaid(), 0.001,
                "Zero amount paid should be allowed (free event)");
    }
}
