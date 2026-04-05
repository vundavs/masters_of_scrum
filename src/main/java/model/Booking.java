package model;

import java.time.LocalDateTime;

/**
 * Represents a booking made by a student for a performance.
 */
public class Booking {

    private final long bookingNumber;
    private final Student student;
    private final Performance performance;
    private final int numTickets;
    private final double amountPaid;
    private final LocalDateTime bookingDateTime;
    private BookingStatus status;

    /**
     * Creates a new booking for a student.
     *
     * @param student     the student making the booking
     * @param performance the performance being booked
     * @param numTickets  the number of tickets booked
     * @param amountPaid  the total amount paid
     */
    public Booking(long bookingNumber, Student student, Performance performance,
                   int numTickets, double amountPaid) {
        assert student != null : "Student cannot be null";
        assert performance != null : "Performance cannot be null";
        assert numTickets > 0 : "Number of tickets must be positive";
        assert amountPaid >= 0 : "Amount paid cannot be negative";

        this.bookingNumber = bookingNumber;
        this.student = student;
        this.performance = performance;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = LocalDateTime.now();
        this.status = BookingStatus.ACTIVE;
    }

    /**
     * Returns the unique booking number.
     *
     * @return booking number
     */
    public long getBookingNumber() {
        return bookingNumber;
    }

    /**
     * Returns the student who made this booking.
     *
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the performance this booking is for.
     *
     * @return the performance
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Returns the number of tickets booked.
     *
     * @return number of tickets
     */
    public int getNumTickets() {
        return numTickets;
    }

    /**
     * Returns the amount paid for this booking.
     *
     * @return amount paid
     */
    public double getAmountPaid() {
        return amountPaid;
    }

    /**
     * Returns the date and time the booking was made.
     *
     * @return booking date and time
     */
    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    /**
     * Returns the current status of this booking.
     *
     * @return booking status
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Cancels this booking at the student's request.
     */
    public void cancelByStudent() {
        assert status == BookingStatus.ACTIVE : "Booking is not active";
        this.status = BookingStatus.CANCELLEDBYSTUDENT;
    }

    /**
     * Cancels this booking due to a payment failure.
     */
    public void cancelPaymentFailed() {
        this.status = BookingStatus.PAYMENTFAILED;
    }

    /**
     * Cancels this booking at the provider's request.
     */
    public void cancelByProvider() {
        assert status == BookingStatus.ACTIVE : "Booking is not active";
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }

    /**
     * Checks whether this booking was made by the student with the given email.
     *
     * @param email the email to check
     * @return true if this booking belongs to that student
     */
    public boolean checkBookedByStudent(String email) {
        assert email != null : "Email cannot be null";
        return student.getEmail().equals(email);
    }

    /**
     * Returns a string containing the student's email and phone number.
     *
     * @return student details string
     */
    public String getStudentDetails() {
        return student.getEmail() + "," + student.getPhoneNumber();
    }

    /**
     * Generates a full booking record string for display.
     *
     * @return booking record string
     */
    public String generateBookingRecord() {
        return "Booking #" + bookingNumber
                + " | Student: " + student.getEmail()
                + " | Performance: " + performance.getEventTitle()
                + " | Tickets: " + numTickets
                + " | Amount paid: " + amountPaid
                + " | Status: " + status
                + " | Booked: " + bookingDateTime;
    }

}