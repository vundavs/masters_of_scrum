import java.time.LocalDateTime;

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
    private Collection<int> reviewRatings;
    private Collection<String> reviewComments;
    private PerformanceStatus status;

    public void cancel(){

    }

    public boolean checkIfEventIsTicketed(){
        return event.isTicketed();
    }

    public boolean checkIfTicketsLeft(int numTicketsToBuy){
        int ticketsLeft;
        ticketsLeft = event.getTicketsTotal() - event.getNumTicketsSold() - numTicketsToBuy;
        if (ticketsLeft >= 0){
            return true;
        }
        return false;
    }

    public double getFinalTicketPrice(){
        if (isSponsored){
            return (ticketPrice - sponsoredAmount);
        }
        return ticketPrice;
    }

    public String getOrganiserEmail(){
        user.getEmail()
                //DK IF MATCHES
        //also is user not the student signed in??
    }

    public String getEventTitle(){
        return event.getTitle;
    }

    public boolean checkHasNotHappenedYet(){
        return startDateTime.isAfter(LocalDateTime.now());
    }

    public boolean checkCreatedByEP(String email){

    }

    public boolean hasActiveBookings(){

    }

    getBookingDetailsForRefund(){

    }

    public void sponsor(double amount){

    }

    public void review(in rating, String comment){

    }

    public void addBooking(Booking b){

    }

    public String toString(){

    }
}

