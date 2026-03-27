package controller;

import external.PaymentSystem;
import external.VerificationService;
import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main controller for the application.
 * Contains all use case methods.
 */

public class UserController {

    private List<User> users = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private User currentUser = null;

    private final PaymentSystem paymentSystem;
    private final VerificationService verificationService;

    /**
     * Creates a new EventsApp instance.
     *
     * @param paymentSystem       the payment system to use
     * @param verificationService the verification service to use
     */
    public UserController(PaymentSystem paymentSystem, VerificationService verificationService) {
        this.paymentSystem = paymentSystem;
        this.verificationService = verificationService;
    }


    /**
     * Books a performance for the currently logged-in consumer.
     *
     * @param performance the performance to book
     * @param numTickets  the number of tickets to book
     * @param paymentAcct the consumer's payment account number
     * @return the new Booking if successful, null otherwise
     */
    public Booking bookPerformance(
            EventPerformance performance, int numTickets, String paymentAcct) {

        assert currentUser instanceof Consumer : "Must be logged in as a consumer";
        if (!(currentUser instanceof Consumer)) return null;
        if (performance == null) return null;
        if (numTickets <= 0) return null;
        if (paymentAcct == null) return null;

        Consumer consumer = (Consumer) currentUser;

        // Check consumer has not already booked this performance
        for (Booking b : bookings) {
            if (b.getConsumer().equals(consumer)
                    && b.getEventPerformance().equals(performance)
                    && b.getStatus() == BookingStatus.ACTIVE) {
                return null;
            }
        }

        // Check capacity
        if (performance.getCapacityLimit() > 0
                && performance.getNumBookings() + numTickets > performance.getCapacityLimit()) {
            return null;
        }

        // Process payment
        double totalCost = performance.getTicketPrice() * numTickets;
        boolean paid = paymentSystem.processPayment(
                paymentAcct,
                performance.getEvent().getEntertainmentProvider().getPaymentAccountNumber(),
                totalCost);
        if (!paid) return null;

        // Create and store booking
        Booking booking = new Booking(consumer, performance, numTickets);
        bookings.add(booking);
        performance.addBooking(booking);
        return booking;
    }

    /**
     * Updates the preferences of the currently logged-in consumer.
     *
     * @param newPreferences the updated preferences
     * @return the updated Consumer, or null on failure
     */
    public Consumer editPreferences(ConsumerPreferences newPreferences) {
        assert currentUser instanceof Consumer : "Must be logged in as a consumer";
        if (!(currentUser instanceof Consumer)) return null;
        if (newPreferences == null) return null;

        Consumer consumer = (Consumer) currentUser;
        consumer.setPreferences(newPreferences);
        return consumer;
    }

    /**
     * Cancels a performance and refunds all active bookings.
     *
     * @param performance the performance to cancel
     * @return the cancelled EventPerformance, or null on failure
     */
    public EventPerformance cancelPerformance(EventPerformance performance) {
        assert currentUser instanceof EntertainmentProvider
                : "Must be logged in as an entertainment provider";
        if (!(currentUser instanceof EntertainmentProvider)) return null;
        if (performance == null) return null;

        EntertainmentProvider ep = (EntertainmentProvider) currentUser;

        // Check performance belongs to this EP
        if (!performance.getEvent().getEntertainmentProvider().equals(ep)) return null;

        // Refund all active bookings
        for (Booking b : performance.getBookings()) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                double refund = b.getNumTickets() * performance.getTicketPrice();
                paymentSystem.processRefund(
                        ep.getPaymentAccountNumber(),
                        b.getConsumer().getPaymentAccountNumber(),
                        refund);
                b.setStatus(BookingStatus.CANCELLED_BY_PROVIDER);
            }
        }

        performance.setStatus(PerformanceStatus.CANCELLED);
        return performance;
    }

}