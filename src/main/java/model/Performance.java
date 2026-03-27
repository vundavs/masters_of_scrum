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

    public long getPerformanceID(Performance perfomance){
        return performanceID
    }

    public void cancel(){
        cancelByProvider();
    }

    public boolean checkIfEventIsTicketed(){
        return event.isTicketed();
    }

    public boolean checkIfTicketsLeft(int numTicketsToBuy){
        int ticketsLeft;
        ticketsLeft = TicketsTotal() - TicketsSold() - numTicketsToBuy;
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
    }

    public String getBookingDetailsForRefund(){
        //NOT SURE IF THIS IS CORRECT
        return getBookingDetailsRefund();
    }

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

