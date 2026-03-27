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

    //performances list?
>>>>>>> d58e7821eee867117738e323e01fd2ae2a2ddd29
}