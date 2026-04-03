package controller;

import external.PaymentSystem;
import model.Booking;
import model.BookingStatus;
import model.Performance;
import model.Student;
import view.View;

import java.util.List;

/**
 * Controller handling booking-related use cases:
 * book performance, cancel booking, and review performance.
 */
public class BookingController extends Controller {

    private final List<Booking> bookings;
    private final PaymentSystem paymentSystem;
    private final List<Performance> performances;

    /**
     * Creates a new BookingController.
     *
     * @param paymentSystem the payment system to use
     * @param view          the view for user interaction
     * @param performances  shared list of all performances
     * @param bookings      shared list of all bookings
     */
    public BookingController(PaymentSystem paymentSystem, View view,
                             List<Performance> performances,
                             List<Booking> bookings) {
        this.paymentSystem = paymentSystem;
        this.view = view;
        this.performances = performances;
        this.bookings = bookings;
    }

    /**
     * Handles the book performance use case for a logged-in student.
     * Gets performance ID and ticket count, processes payment, creates a booking.
     */
    public void bookPerformance() {
        assert checkCurrentUserIsStudent() : "Must be logged in as a student";
        if (!checkCurrentUserIsStudent()) {
            view.displayError("You must be logged in as a student to book.");
            return;
        }

        String performanceInput = view.getInput("Enter performance ID:");
        long performanceID;
        try {
            performanceID = Long.parseLong(performanceInput.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid performance ID.");
            return;
        }

        String ticketInput = view.getInput("Enter number of tickets:");
        int numTicketsRequested;
        try {
            numTicketsRequested = Integer.parseInt(ticketInput.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid number of tickets.");
            return;
        }

        if (numTicketsRequested <= 0) {
            view.displayError("Number of tickets must be at least 1.");
            return;
        }

        Performance performance = getPerformanceByID(performanceID);
        if (performance == null) {
            view.displayError("Performance with given number does not exist.");
            return;
        }

        if (!performance.checkIfEventIsTicketed()) {
            view.displayError(
                    "The requested performance's event is not ticketed. "
                            + "There is no need to book it.");
            return;
        }

        if (!performance.checkIfTicketsLeft(numTicketsRequested)) {
            view.displayError("Requested performance has no tickets left.");
            return;
        }

        Student student = (Student) currentUser;
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = performance.getOrganiserEmail();
        String eventTitle = performance.getEventTitle();
        double ticketPrice = performance.getFinalTicketPrice();
        double transactionAmount = ticketPrice * numTicketsRequested;

        Booking booking = new Booking(
                student, performance, numTicketsRequested, transactionAmount);

        boolean paymentSuccessful = paymentSystem.processPayment(
                numTicketsRequested, eventTitle,
                studentEmail, studentPhone,
                epEmail, transactionAmount);

        if (!paymentSuccessful) {
            booking.cancelPaymentFailed();
            view.displayError("There was an issue with payment.");
            return;
        }

        performance.addBooking(booking);
        student.addBooking(booking);
        bookings.add(booking);

        view.displayBookingRecord(booking.generateBookingRecord());
        view.displaySuccess("Booking Successful!");
    }

    /**
     * Handles the cancel booking use case for a logged-in student.
     * Gets booking number from user, processes refund, cancels booking.
     */
    public void cancelBooking() {
        assert checkCurrentUserIsStudent() : "Must be logged in as a student";
        if (!checkCurrentUserIsStudent()) {
            view.displayError("You must be logged in as a student.");
            return;
        }

        String input = view.getInput("Enter booking number to cancel:");
        long bookingNumber;
        try {
            bookingNumber = Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid booking number.");
            return;
        }

        Booking booking = getBookingByNumber(bookingNumber);
        if (booking == null) {
            view.displayError("Booking with given number does not exist.");
            return;
        }

        Student student = (Student) currentUser;
        if (!booking.checkBookedByStudent(student.getEmail())) {
            view.displayError("This booking does not belong to you.");
            return;
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            view.displayError("This booking is not active and cannot be cancelled.");
            return;
        }

        Performance performance = booking.getPerformance();
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = performance.getOrganiserEmail();
        String eventTitle = performance.getEventTitle();
        double transactionAmount = booking.getAmountPaid();

        boolean refundSuccessful = paymentSystem.processRefund(
                booking.getNumTickets(), eventTitle,
                studentEmail, studentPhone,
                epEmail, transactionAmount,
                "Cancelled by student");

        if (!refundSuccessful) {
            view.displayError("There was an issue with the refund.");
            return;
        }

        booking.cancelByStudent();
        view.displaySuccess("Booking cancelled successfully.");
    }

    /**
     * Handles the review performance use case for a logged-in student.
     * Student must have an active booking for a past performance.
     */
    public void reviewPerformance() {
        assert checkCurrentUserIsStudent() : "Must be logged in as a student";
        if (!checkCurrentUserIsStudent()) {
            view.displayError("You must be logged in as a student.");
            return;
        }

        String input = view.getInput("Enter performance ID to review:");
        long performanceID;
        try {
            performanceID = Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid performance ID.");
            return;
        }

        Performance performance = getPerformanceByID(performanceID);
        if (performance == null) {
            view.displayError("Performance with given number does not exist.");
            return;
        }

        if (performance.checkHasNotHappenedYet()) {
            view.displayError(
                    "You cannot review a performance that has not happened yet.");
            return;
        }

        Student student = (Student) currentUser;
        boolean hasBooking = false;
        for (Booking b : bookings) {
            if (b.checkBookedByStudent(student.getEmail())
                    && b.getPerformance().equals(performance)
                    && b.getStatus() == BookingStatus.ACTIVE) {
                hasBooking = true;
                break;
            }
        }

        if (!hasBooking) {
            view.displayError(
                    "You do not have an active booking for this performance.");
            return;
        }

        String ratingInput = view.getInput("Enter rating (1-5):");
        int rating;
        try {
            rating = Integer.parseInt(ratingInput.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid rating.");
            return;
        }

        if (rating < 1 || rating > 5) {
            view.displayError("Rating must be between 1 and 5.");
            return;
        }

        String comment = view.getInput("Enter your review comment:");
        if (comment == null || comment.trim().isEmpty()) {
            view.displayError("Review comment cannot be empty.");
            return;
        }

        performance.review(rating, comment);
        view.displaySuccess("Review submitted successfully.");
    }

    /**
     * Returns the performance with the given ID, or null if not found.
     *
     * @param performanceID the performance ID to search for
     * @return the matching Performance, or null
     */
    private Performance getPerformanceByID(long performanceID) {
        for (Performance p : performances) {
            if (p.getPerformanceId() == performanceID) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the booking with the given number, or null if not found.
     *
     * @param bookingNumber the booking number to search for
     * @return the matching Booking, or null
     */
    private Booking getBookingByNumber(long bookingNumber) {
        for (Booking b : bookings) {
            if (b.getBookingNumber() == bookingNumber) {
                return b;
            }
        }
        return null;
    }

    /**
     * Adds a booking to the shared bookings list.
     *
     * @param b the booking to add
     */
    private void addBooking(Booking b) {
        bookings.add(b);
    }

    /**
     * Checks whether a booking is possible for the given performance and ticket count.
     *
     * @param performance the performance to check
     * @param numTickets  the number of tickets requested
     * @return true if the performance is ticketed and has enough tickets left
     */
    private boolean checkIfBookingPossible(Performance performance, int numTickets) {
        return performance.checkIfEventIsTicketed()
                && performance.checkIfTicketsLeft(numTickets);
    }

    /**
     * Finds all bookings associated with the given event ID.
     *
     * @param eventID the event ID to search for
     * @return a collection of matching bookings
     */
    private java.util.Collection<Booking> findBookingsByEventID(long eventID) {
        java.util.List<Booking> result = new java.util.ArrayList<>();
        for (Booking b : bookings) {
            if (b.getPerformance().getPerformanceId() == eventID) {
                result.add(b);
            }
        }
        return result;
    }
}

