package model;
import java.time.LocalDateTime;

public class Event {
    private long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;

    public long getEventID(){return eventID;}
    public String getTitle(){return title;}
    public EventType getType(){return type;}
    public boolean isTicketed(){return isTicketed;}

    //performances list?
}