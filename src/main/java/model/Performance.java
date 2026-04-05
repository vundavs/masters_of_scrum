package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Performance {

    private long performanceId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<String> performerNames;
    private String venueAddress;
    private int venueCapacity;
    private boolean venueIsOutdoors;
    private boolean venueAllowsSmoking;
    private int numTicketsTotal;
    private int numTicketsSold;
    private double ticketPrice;
    private boolean isSponsored;
    private double sponsoredAmount;
    private List<Integer> reviewRatings = new ArrayList<>();
    private List<String> reviewComments = new ArrayList<>();
    private PerformanceStatus status;
    private List<Booking> bookings;
    private Event event;

    /**
     * creates a new performance with the given details
     *
     * @param startDateTime         start time and date of the event
     * @param endDateTime           end time and date of the event
     * @param performerNames        all performer's names
     * @param venueAddress          address of the venue
     * @param venueCapacity         capacity of the venue
     * @param venueIsOutdoors       whether the venue is outdoors or not
     * @param venueAllowsSmoking    if the venue allows smoking
     * @param numTicketsTotal       number of tickets to be put on sale
     * @param ticketPrice           price of the ticket
     * @param bookings              initial list of bookings (may be empty)
     * @param event                 the event the performance belongs to
     */
    public Performance(long performanceId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                       List<String> performerNames, String venueAddress, int venueCapacity,
                       boolean venueIsOutdoors, boolean venueAllowsSmoking, int numTicketsTotal,
                       double ticketPrice, List<Booking> bookings, Event event) {
        assert venueAddress != null : "Venue address cannot be null";
        assert endDateTime.isAfter(startDateTime) : "End time must be after start time";
        assert venueCapacity > 0 : "Venue capacity must be greater than 0";
        assert numTicketsTotal <= venueCapacity
                : "Number of tickets must be less than or equal to venueCapacity";

        this.performanceId = performanceId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.venueAddress = venueAddress;
        this.venueCapacity = venueCapacity;
        this.venueIsOutdoors = venueIsOutdoors;
        this.venueAllowsSmoking = venueAllowsSmoking;
        this.numTicketsTotal = numTicketsTotal;
        this.ticketPrice = ticketPrice;
        this.status = PerformanceStatus.ACTIVE;
        this.bookings = new ArrayList<>();
        this.event = event;
    }

    /**
     * Returns the unique performance Id.
     *
     * @return performance Id
     */
    public long getPerformanceId() {
        return performanceId;
    }

    /**
     * Returns the start date and time for a performance.
     *
     * @return the start date and start time
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Returns the end date and time for a performance.
     *
     * @return the end date and end time
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Returns the names of all performers.
     *
     * @return performers names
     */
    public List<String> getPerformerNames() {
        return performerNames;
    }

    /**
     * Returns the address of the venue.
     *
     * @return the venue's address
     */
    public String getVenueAddress() {
        return venueAddress;
    }

    /**
     * Returns capacity of the venue.
     *
     * @return venue capacity
     */
    public int getVenueCapacity() {
        return venueCapacity;
    }

    /**
     * Returns whether or not the venue is outdoors.
     *
     * @return if the venue is outdoors or not
     */
    public boolean isVenueIsOutdoors() {
        return venueIsOutdoors;
    }

    /**
     * Returns whether or not the venue allows smoking.
     *
     * @return if venue allows smoking
     */
    public boolean doesVenueAllowsSmoking() {
        return venueAllowsSmoking;
    }

    /**
     * Returns the number of total tickets for performance.
     *
     * @return total number of tickets
     */
    public int getNumTicketsTotal() {
        return numTicketsTotal;
    }

    /**
     * Returns the number of tickets sold.
     *
     * @return the number of tickets sold
     */
    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    /**
     * Returns the original price of ticket.
     *
     * @return original ticket price
     */
    public double getTicketPrice() {
        return ticketPrice;
    }

    /**
     * Returns whether or not the performance is sponsored.
     *
     * @return if the performance is sponsored
     */
    public boolean isSponsored() {
        return isSponsored;
    }

    /**
     * Returns the amount a performance is sponsored by.
     *
     * @return sponsored amount
     */
    public double getSponsoredAmount() {
        return sponsoredAmount;
    }

    /**
     * Returns all the review ratings.
     *
     * @return all review ratings
     */
    public List<Integer> getReviewRatings() {
        return reviewRatings;
    }

    /**
     * Returns all the review comments.
     *
     * @return all review comments
     */
    public List<String> getReviewComments() {
        return reviewComments;
    }

    /**
     * Returns the status of the performance.
     *
     * @return performance status
     */
    public PerformanceStatus getStatus() {
        return status;
    }

    /**
     * Returns the event this performance belongs to.
     *
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    // METHODS

    /**
     * Cancels this performance and changes status.
     */
    public void cancel() {
        this.status = PerformanceStatus.CANCELLED;
    }

    /**
     * checks if the event is ticketed
     *
     * @return whether the event is ticketed
     */
    public boolean checkIfEventIsTicketed() {
        return event.isTicketed();
    }

    /**
     * checks if there is enough tickets left to buy
     *
     * @param numTicketsToBuy   the number of tickets the student wants to buy
     * @return if there is enough tickets (t/f)
     */
    public boolean checkIfTicketsLeft(int numTicketsToBuy) {
        assert numTicketsToBuy > 0 : "Number of tickets to buy must be greater than 0";
        return getNumTicketsTotal() - getNumTicketsSold() - numTicketsToBuy >= 0;
    }

    /**
     * gets the 'true'/final ticket price taking sponsors into account
     *
     * @return the final ticket price
     */
    public double getFinalTicketPrice() {
        if (isSponsored()) {
            return getTicketPrice() - getSponsoredAmount();
        }
        return getTicketPrice();
    }

    /**
     * gets the organiser/EP's email
     *
     * @return organiser's email
     */
    public String getOrganiserEmail() {
        return event.getOrganiserEmail();
    }

    /**
     * gets the event title
     *
     * @return event title
     */
    public String getEventTitle() {
        return event.getTitle();
    }

    /**
     * checks if the event has already happened
     *
     * @return if event has already started
     */
    public boolean checkHasNotHappenedYet() {
        return startDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * checks if the performance was made by an EP
     *
     * @param email     the email of creator of performance
     * @return whether event was created by an EP
     */
    public boolean checkCreatedByEP(String email) {
        return event.getOrganiserEmail().equals(email);
    }

    /**
     * checks if a performance has any active bookings
     *
     * @return whether an event has any active bookings
     */
    public boolean hasActiveBookings() {
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns booking details for all active bookings, for use in refund processing.
     *
     * @return concatenated booking records for active bookings
     */
    public String getBookingDetailsForRefund() {
        StringBuilder result = new StringBuilder();
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                result.append(b.generateBookingRecord()).append("\n");
            }
        }
        return result.toString();
    }

    /**
     * Returns the list of bookings for this performance.
     *
     * @return list of bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * sponsor event
     *
     * @param amount    amount admin would like to sponsor by
     */
    public void sponsor(double amount) {
        isSponsored = true;
        sponsoredAmount = amount;
    }

    /**
     * add review and its comment
     *
     * @param rating    review number rating
     * @param comment   review comment
     */
    public void review(int rating, String comment) {
        reviewRatings.add(rating);
        reviewComments.add(comment);
    }

    /**
     * adds a booking
     *
     * @param b the booking
     */
    public void addBooking(Booking b) {
        bookings.add(b);
        numTicketsSold += b.getNumTickets();
    }

    /**
     * overrides java's toString() method for better formatting
     *
     * @return string in correct format
     */
    @Override
    public String toString() {
        return "Performance{id=" + performanceId + ", venue='" + venueAddress
                + "', start=" + startDateTime + ", status=" + status + "}";
    }
}
