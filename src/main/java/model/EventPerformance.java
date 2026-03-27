import java.time.LocalDateTime;

public class EventPerformance {
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

    }

    public String getEventTitle(){
        return event.getTitle;
    }

    public boolean checkHasNotHappenedYet(){
        return startDateTime.isAfter(LocalDateTime.now());
    }

    public boolean checkCreatedByEP(String email){

    }

}

