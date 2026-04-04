package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.time.LocalDate;


public class Event {
    private long nextEventId = 1;

    private final long eventID;
    private String title;
    private EventType type;
    private boolean isTicketed;
    private final EntertainmentProvider organiser;
    private ArrayList<Performance> performances;


    /**
     * Creates a new event with the given details
     *
     * @param title      the title of the event
     * @param type       what type of event it is
     * @param isTicketed whether the event is ticketed
     */
    public Event(String title, EventType type, boolean isTicketed,
                 EntertainmentProvider organiser) {
        assert title != null : "Title cannot be null";
        assert type != null : "Type cannot be null";
        assert organiser != null : "Organiser cannot be null";

        this.eventID = nextEventId++;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        this.organiser = organiser;
        this.performances = new ArrayList<>();
    }

    /**
     * get the event's ID
     *
     * @return event ID
     */
    public long getEventID() {
        return eventID;
    }

    /**
     * get the title of the event
     *
     * @return event's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * get the event's type
     *
     * @return event type
     */
    public EventType getType() {
        return type;
    }

    /**
     * return whether the event is ticketed
     *
     * @return if event is ticketed
     */
    public boolean isTicketed() {
        return isTicketed;
    }


    /**
     * creates new performance using given data
     *
     * @param startDateTime      date and time performance starts
     * @param endDateTime        date and time performance ends
     * @param performerNames     names of the performers
     * @param venueAddress       address of venue
     * @param venueCapacity      capacity of the venue
     * @param venueIsOutdoors    whether the venue is outdoors
     * @param venueAllowsSmoking whether the venue allows smoking
     * @param numTickets         number of tickets available to buy
     * @param ticketPrice        price of the tickets
     * @return the performance added
     */
    public Performance createPerformance(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                         List<String> performerNames,
                                         String venueAddress, int venueCapacity, boolean venueIsOutdoors,
                                         boolean venueAllowsSmoking, int numTickets, double ticketPrice, Event event) {
        assert performerNames != null : "PerformerNames cannot be null";
        assert venueAddress != null : "VenueAddress cannot be null";
        assert !hasPerformancesAtSameTimes(startDateTime, endDateTime) :
                "A performance in this event already ahs the same starr and date time";
        assert startDateTime.isBefore(endDateTime) : "Start date cannot be after end date";
        assert venueCapacity > 0 : "VenueCapacity cannot be negative";
        assert numTickets > 0 : "Number of tickets cannot be negative";
        assert ticketPrice > 0 : "Ticket price cannot be negative";


        Performance p = new Performance(startDateTime, endDateTime,
                performerNames, venueAddress, venueCapacity, venueIsOutdoors,
                venueAllowsSmoking, numTickets, ticketPrice, new ArrayList<>(), this);
        performances.add(p);
        return p;
    }

    /**
     * get the performance using ID
     *
     * @param performanceID ID of performance
     * @return the performance that matches ID
     */
    public Performance getPerformanceById(long performanceID) {
        for (Performance p : performances) {
            if (p.getPerformanceId() == performanceID) {
                return p;
            }
        }
        return null;
    }

    /**
     * get the details for all performances that are on specified date
     *
     * @param searchDate the date they want to all performances that are on
     * @return list of all performances on that day
     */
    public Collection<String> getInfoOfPerformancesOnDate(LocalDate searchDate) {
        Collection<String> result = new ArrayList<>();
        for (Performance p : performances) {
            if (p.getStartDateTime().toLocalDate().equals(searchDate)) {
                result.add(performanceFormat(p));
            }
        }
        return result;
    }

    /**
     * get the organisers name
     *
     * @return organiser's name
     */
    private String getOrganiserName() {
        return organiser.getOrgName();
    }

    /**
     * gets the event organiser/EP's email
     *
     * @return organiser's email
     */
    public String getOrganiserEmail() {
        return organiser.getEmail();
    }

    /**
     * gets the average rating of the performances
     *
     * @return average rating of performances
     */
    public double getAverageRatingOfPerformances() {
        if (performances.isEmpty()) return 0.0;
        double total = 0.0;
        int count = 0;
        for (Performance p : performances) {
            for (int rating : p.getReviewRatings()) {
                total += rating;
                count++;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return (total / count);
    }


    /**
     * gets all review comments for event
     *
     * @return all review comments for event
     */
    public Collection<String> getAllPerformanceReviews() {
        Collection<String> result = new ArrayList<>();
        for (Performance p : performances) {
            result.addAll(p.getReviewComments());
        }
        return result;
    }

    /**
     * checks if the event has a performance with the same start and end dateTime
     *
     * @param startDateTime start time of performance
     * @param endDateTime   end time of performance
     * @return whether a performance already exists with same timings
     */
    private boolean hasPerformancesAtSameTimes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        for (Performance p : performances) {
            if (p.getStartDateTime().equals(startDateTime)
                    && p.getEndDateTime().equals(endDateTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * add a performance
     */
    public void addPerformance(Performance p){
        performances.add(p);
    }

    /**
     * Resets the event ID counter. For testing purposes only.
     */
    public static void resetEventIDCounter() {
        nextEventId = 1;
    }

    /**
     * formats and get all relevant information
     *
     * @return string in correct format
     */
    public String performanceFormat(Performance p) {
        if (p == null) return "No performance found.";
        if (p.getStatus().equals("CANCELLED")) {
            return "This performance has been cancelled.";
        }

        if (p.checkIfEventIsTicketed()) {
            return "Performance ID: " + p.getPerformanceId() + "\nStart date and time: " + p.getStartDateTime()
                    + " End date and time: " + p.getEndDateTime() + "\nPerformer's names: " + p.getPerformerNames()
                    + "\nVenue address: " + p.getVenueAddress() + " Venue capacity: " + p.getVenueCapacity()
                    + "\nIs venue outdoors: " + p.isVenueIsOutdoors() + " Does the venue allows smoking: " + p.doesVenueAllowsSmoking()
                    + "\nTicket price: " + p.getFinalTicketPrice()
                    + "\nThe average rating for this event: " + this.getAverageRatingOfPerformances();
        }

        //else performance is not ticketed
        return "Performance ID: " + p.getPerformanceId() + "\nStart date and time: " + p.getStartDateTime()
                + " End date and time: " + p.getEndDateTime() + "\nPerformer's names: " + p.getPerformerNames()
                + "\nVenue address: " + p.getVenueAddress() + " Venue capacity: " + p.getVenueCapacity()
                + "\nIs venue outdoors: " + p.isVenueIsOutdoors() + " Does the venue allows smoking: " + p.doesVenueAllowsSmoking()
                + "\nThis performance is now tickets, bookings cannot be made."
                + "\nThe average rating for this event: " + this.getAverageRatingOfPerformances();
    }
}