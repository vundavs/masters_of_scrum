package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Performance {
    private static long nextPerformanceID = 1;

    private long performanceID;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Collection<String> performerNames;
    private String venueAddress;
    private int venueCapacity;
    private boolean venueIsOutdoors;
    private boolean venueAllowsSmoking;
    private int numTicketsTotal;
    private int numTicketsSold;
    private double ticketPrice;
    private boolean isSponsored;
    private double sponsoredAmount;
    private Collection<Integer> reviewRatings;
    private Collection<String> reviewComments;
    private PerformanceStatus status;

    private Event event;

    /**
     * creates a new performance with the given details
     *
     * @param startDateTime         start time and date of the event
     * @param endDateTime           end time and dtae of the event
     * @param performerNames        all performer's names
     * @param venueAddress          address of the venue
     * @param venueCapacity         capacity of the venue
     * @param venueIsOutdoors       whether the venue is outdoors or not
     * @param venueAllowsSmoking    if the venue allows smoking
     * @param numTicketsTotal       number of tickets to be put on sale
     * @param ticketPrice           price of the ticket
     * @param event                 the event the performance belongs to
     */
    public Performance(LocalDateTime startDateTime, LocalDateTime endDateTime,
                       Collection<String> performerNames, String venueAddress, int venueCapacity,
                       boolean venueIsOutdoors, boolean venueAllowsSmoking, int numTicketsTotal,
                       double ticketPrice, Event event) {
        assert venueAddress != null : "Venue address cannot be null";
        assert venueCapacity > 0 : "Venue capacity must be greater than 0";
        assert numTicketsTotal <= venueCapacity : "Number of tickets must be less than or equal to venueCapacity";

        this.performanceId = nextPerformanceID++;
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
        this.event = event;
    }

    /**
     * Returns the unique performance ID.
     *
     * @return performance ID
     */
    public long getPerformanceID(){
        return performanceID;
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
     * @return the end date and ens time
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Returns the names of all performers.
     *
     * @return performers names
     */
    public Collection<String> getPerformerNames() {
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
     * @return orginal ticket price
     */
    public double getTicketPrice() {
        return ticketPrice;
    }

    /**
     * Returns whether or not the performance is sponsored.
     *
     * @return if the
     */
    public boolean getIsSponsored() {
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
    public Collection<Integer> getReviewRatings() {
        return reviewRatings;
    }

    /**
     * Returns all the review comments.
     *
     * @return all review comments
     */
    public Collection<String> getReviewComments() {
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

    //METHODS

    /**
     * Cancels this performance and changes status
     */
    public void cancel(){
        this.status = PerformanceStatus.CANCELLED;
    }

    /**
     * checks if the event is ticketed
     *
     * @return whether the event is ticketed
     */
    public boolean checkIfEventIsTicketed(){
        return event.isTicketed();
    }

    /**
     * checks if there is enough tickets left to buy
     * @param numTicketsToBuy   the number of tickets the student wants to buy
     * @return if there is enough tickets (t/f)
     */
    public boolean checkIfTicketsLeft(int numTicketsToBuy){
        assert numTicketsToBuy > 0 : "Number of tickets to buy must be greater than 0";
        if (getNumTicketsTotal() - getNumTicketsSold() - numTicketsToBuy >= 0){
            return true;
        }
        return false;
    }

    /**
     * gets the 'true'/final ticket price taking sponsors into account
     *
     * @return the final ticket price
     */
    public double getFinalTicketPrice(){
        if (getIsSponsored()) {
            return (getTicketPrice() - getSponsoredAmount());
        }
        return getTicketPrice();
    }

    /**
     * gets the organiser/EP's email
     *
     * @return organiser's email
     */
    public String getOrganiserEmail(){
        return event.getOrganiserEmail();
    }

    /**
     * gets the event titls
     *
     * @return event title
     */
    public String getEventTitle(){
        return event.getTitle();
    }

    /**
     * checks if the event has already happend
     *
     * @return if event has already started
     */
    public boolean checkHasNotHappenedYet(){
        return startDateTime.isAfter(LocalDateTime.now());
    }


    /**
     * checks if the performance was made by an EP
     *
     * @param email     the email of creator of performance
     *
     * @return whether event was created by an EP
     */
    public boolean checkCreatedByEP(String email){
        return event.getOrganiserEMail().equals(email);
    }

    /**
     * checks if a perfomance has any active bookings
     *
     * @return whether an event has any active bookings
     */
    public boolean hasActiveBookings(){
        for(Booking b: bookings){
            if (b.getStatus() != BookingStatus.CANCELLED){
                return true;
            }
        }
        return false;
    }

    public String getBookingDetailsForRefund(){
        StringBuilder result = new StringBuilder();
        for (Booking b : bookings) {
            if (b.getStatus() != BookingStatus.CANCELLED) {
                result.append(b.generateBookingRecord()).append("\n");
            }
        }
        return result.toString();
    }

    /**
     * sponsor event
     *
     * @param amount    amount admin would like to sponsor by
     */
    public void sponsor(double amount){
            isSponsored = true;
            sponsoredAmount = amount;
    }

    /**
     * add review and its comment
     *
     * @param rating    review number rating
     * @param comment   review comment
     */
    public void review(int rating, String comment){
         reviewRatings.add(rating);
         reviewComments.add(comment);
    }

    /**
     * adds a booking
     *
     * @param b the booking
     */
    public void addBooking(Booking b){
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
        return "Performance{id=" + performanceID + ", venue='" + venueAddress +
                "', start=" + startDateTime + ", status=" + status + "}";
    }

}

