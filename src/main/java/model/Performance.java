package model;
import java.time.LocalDateTime;
import java.util.Collection;

public class Performance {
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
        int ticketsLeft;
        ticketsLeft = getNumTicketsTotal() - getNumTicketsSold() - numTicketsToBuy;
        if (ticketsLeft >= 0){
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
     *
     * @return
     */
    public String getOrganiserEmail(){
        return organiser.getUser().getEmail();
                //DK IF MATCHES - organiser?
    }

    public String getEventTitle(){
        return event.getTitle();
    }

    public boolean checkHasNotHappenedYet(){
        return startDateTime.isAfter(LocalDateTime.now());
    }

    public boolean checkCreatedByEP(String email){
        //
        for (EntertainmentProvider e : entertainmentProvider){
            if (e.getUser().getEmail().equals(email)){
                //if email belongs to an EP
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveBookings(){
        if (numTicketsTotal > 0){
            return true;
        }
        return false;
    }

    public String getBookingDetailsForRefund(){
        //NOT SURE IF THIS IS CORRECT
        return getBookingDetailsRefund();
    }

    //check if already sponsored
    //JUSTIFY: a performance can only be sponsored once
    public void sponsor(double amount){
        if (sponsoredAmount > ticketPrice){
            System.out.println("Sponsored amount is greater than ticket price");
        } else {
            isSponsored = true;
            sponsoredAmount = amount;
        }
    }

    public void review(int rating, String comment){
         reviewRatings.add(rating);
         reviewComments.add(comment);
    }

    public void addBooking(Booking b){

    }
}

