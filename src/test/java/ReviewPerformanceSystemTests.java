import model.Booking;
import model.BookingStatus;
import model.EntertainmentProvider;
import model.Performance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Review Performance use case.
 *
 * <p>A student may review a performance only if:
 * <ul>
 *   <li>they are logged in as a student,
 *   <li>the performance exists,
 *   <li>the performance has already happened,
 *   <li>they have an ACTIVE booking for that performance,
 *   <li>the rating is between 1 and 5 (inclusive), and
 *   <li>the comment is non-empty.
 * </ul>
 */
public class ReviewPerformanceSystemTests extends TestSystemBase {

    /**
     * Books a past performance as student1 with 1 ticket and returns the booking.
     * Leaves student1 logged in.
     */
    private Booking bookPastPerformanceAsStudent1(TestView view, Performance p) {
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "1");
        bookingController.bookPerformance();
        return bookings.get(0);
    }

    // -----------------------------------------------------------------------
    // Happy-path tests
    // -----------------------------------------------------------------------

    @Test
    void testReviewPerformanceSuccessfully() {
        // Student with an active booking for a past performance submits a
        // valid review — expects a success message.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "4", "Great show!");
        bookingController.reviewPerformance();

        assertTrue(view.hasSuccessContaining("submitted"),
                "Success message should be displayed after review submission");
    }

    @Test
    void testReviewAddsRatingToPerformance() {
        // After a successful review, the rating should be recorded on the performance.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "5", "Excellent!");
        bookingController.reviewPerformance();

        assertFalse(p.getReviewRatings().isEmpty(),
                "Performance should have at least one rating after review");
        assertEquals(5, p.getReviewRatings().get(0),
                "Stored rating should match the submitted rating");
    }

    @Test
    void testReviewAddsCommentToPerformance() {
        // After a successful review, the comment should be recorded on the performance.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "3", "It was okay.");
        bookingController.reviewPerformance();

        assertFalse(p.getReviewComments().isEmpty(),
                "Performance should have at least one comment after review");
        assertEquals("It was okay.", p.getReviewComments().get(0),
                "Stored comment should match the submitted comment");
    }

    // -----------------------------------------------------------------------
    // Authentication / authorisation failures
    // -----------------------------------------------------------------------

    @Test
    void testReviewFailsWhenNotLoggedIn() {
        // No user logged in — review should be rejected immediately.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);

        // Ensure no user is logged in
        bookingController.setCurrentUser(null);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("student"),
                "Error should state that a student must be logged in");
    }

    @Test
    void testReviewFailsWhenEPLoggedIn() {
        // Entertainment providers cannot submit reviews.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("student"),
                "Error should state that a student must be logged in");
    }

    // -----------------------------------------------------------------------
    // Invalid performance ID
    // -----------------------------------------------------------------------

    @Test
    void testReviewFailsForNonExistentPerformanceId() {
        // Performance ID 99999 does not exist.
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);

        view.addInputs("99999");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("does not exist"),
                "Error should state that the performance does not exist");
    }

    @Test
    void testReviewFailsForNonNumericPerformanceId() {
        // Non-numeric input for performance ID should be rejected.
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);

        view.addInputs("abc");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should state that the performance ID is invalid");
    }

    // -----------------------------------------------------------------------
    // Performance timing
    // -----------------------------------------------------------------------

    @Test
    void testReviewFailsForFuturePerformance() {
        // A performance that has not yet happened cannot be reviewed.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("not happened yet"),
                "Error should state that the performance has not happened yet");
    }

    // -----------------------------------------------------------------------
    // Booking requirement
    // -----------------------------------------------------------------------

    @Test
    void testReviewFailsWhenStudentHasNoBooking() {
        // Student2 tries to review a performance that only student1 has booked.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p); // student1 books

        // Log in as student2 (no booking for this performance)
        loginAsStudent("student2@uni.ac.uk", "pass2", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("active booking"),
                "Error should state that the student has no active booking");
    }

    @Test
    void testReviewFailsWhenBookingIsCancelled() {
        // Student1 cancels their booking then tries to review — should be rejected
        // because the booking is no longer ACTIVE.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        Booking booking = bookPastPerformanceAsStudent1(view, p);

        // Cancel the booking
        view.addInputs(String.valueOf(booking.getBookingNumber()));
        bookingController.cancelBooking();
        assertEquals(BookingStatus.CANCELLEDBYSTUDENT, booking.getStatus(),
                "Booking should be cancelled before attempting review");

        // Now try to review
        view.addInputs(String.valueOf(p.getPerformanceId()));
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("active booking"),
                "Error should state that the student has no active booking");
    }

    // -----------------------------------------------------------------------
    // Rating validation
    // -----------------------------------------------------------------------

    @Test
    void testReviewFailsWithRatingBelowMinimum() {
        // Rating of 0 is below the allowed minimum of 1.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "0");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("between 1 and 5"),
                "Error should state that the rating must be between 1 and 5");
        assertTrue(p.getReviewRatings().isEmpty(),
                "No rating should be stored after a failed review");
    }

    @Test
    void testReviewFailsWithRatingAboveMaximum() {
        // Rating of 6 exceeds the allowed maximum of 5.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "6");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("between 1 and 5"),
                "Error should state that the rating must be between 1 and 5");
        assertTrue(p.getReviewRatings().isEmpty(),
                "No rating should be stored after a failed review");
    }

    @Test
    void testReviewFailsWithNonNumericRating() {
        // Non-numeric rating input should be rejected.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "abc");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should state that the rating is invalid");
        assertTrue(p.getReviewRatings().isEmpty(),
                "No rating should be stored after a failed review");
    }

    // -----------------------------------------------------------------------
    // Comment validation
    // -----------------------------------------------------------------------

    @Test
    void testReviewFailsWithEmptyComment() {
        // An empty comment should be rejected.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "3", "");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("empty"),
                "Error should state that the comment cannot be empty");
        assertTrue(p.getReviewComments().isEmpty(),
                "No comment should be stored after a failed review");
    }

    @Test
    void testReviewFailsWithBlankWhitespaceComment() {
        // A comment containing only whitespace should be rejected.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createPastPerformance(ep);
        bookPastPerformanceAsStudent1(view, p);

        view.addInputs(String.valueOf(p.getPerformanceId()), "3", "   ");
        bookingController.reviewPerformance();

        assertTrue(view.hasErrorContaining("empty"),
                "Error should state that the comment cannot be empty");
        assertTrue(p.getReviewComments().isEmpty(),
                "No comment should be stored after a failed review");
    }
}
