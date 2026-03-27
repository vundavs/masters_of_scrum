import java.time.LocalDateTime;

public class Event {
    private long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;

    public long getEventID(Event event){return eventID;}
    public String getTitle(){return title;}
    public EventType getType(){return type;}
    public boolean isTicketed(){return isTicketed;}

    public createPerformance(long performanceID, LocalDateTime startDateTime, LocalDateTime endDateTime, Collection<String> performerNames, String venueAddress, int venueCapacity, boolean venueIsOutdoors, boolean venueAllowsSmoking, int numTickets, double ticketPrice){

    }

}