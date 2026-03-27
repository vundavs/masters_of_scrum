package model;

/**
 * Represents the possible states of a booking.
 */
public enum BookingStatus {
    ACTIVE,
    CANCELLEDBYSTUDENT,
    CANCELLEDBYPROVIDER,
    PAYMENTFAILED
}