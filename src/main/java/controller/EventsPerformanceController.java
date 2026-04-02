package controller;

import jdk.vm.ci.meta.Local;
import model.Performance;
import view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller handling event and performance related use cases:
 * search for performances, view performance, create event,
 * cancel performance, and sponsor performance.
 */
public class EventsPerformanceController extends Controller {

    private final List<Performance> performances;

    /**
     * Creates a new EventsPerformanceController.
     *
     * @param view         the view for user interaction
     * @param performances shared list of all performances
     */
    public EventsPerformanceController(View view, List<Performance> performances) {
        this.view = view;
        this.performances = performances;
    }

    /**
     * Handles the search for performances by date use case.
     */
    public void searchForPerformances() {
        LocalDate date searchDateTime = (LocalDateTime) view.getInput("Enter date to search for performances");
        for (Event e : events) {
            Collection<String> results = e.getInfoOfPerformancesOnDate(LocalDateTime searchDateTime);
            for (String s : results) {
                System.out.println(s);
            }
        }
    }

    /**
     * Handles the view performance use case.
     */
    public void viewPerformance() {
        long performanceID = (long) view.getInput("Enter performance ID to view: ");
        Performance p = getPerformanceByID(performanceID);
        if (p == null) {
            view.displayError("Performance not found.");
            return;
        }
        System.out.println(p.toString());
    }

    /**
     * Handles the create event use case for entertainment providers.
     */
    public Event createEvent() {
        if(!checkCurrentUserIsEntertainmentProvider()) {
            view.displayError("Only entertainment providers can create events.");
            return null;
        }
        String title = (String) view.getInput("Enter event title: ");
        EventType type = (EventType) view.getInput("Enter event type: ");
        boolean isTicketed = (boolean) view.getInput("Is the event ticketed? (true/false)");

        EntertainmentProvider organiser = (EntertainmentProvider) getCurrentUser();
        Event e = new Event(title, type, isTicketed, organiser);
        events.add(e);
        return e;
    }

    /**
     * Handles the cancel performance use case for entertainment providers.
     * TODO: paste in from sahasra
     */
    public void cancelPerformance() {
        // TODO: implement cancel performance
        view.displayError("Cancel performance not yet implemented.");
    }

    /**
     * Handles the sponsor performance use case for admin staff.
     */
    public void sponsorPerformance() {
        if(!checkCurrentUserIsAdmin()) {
            view.displayError("Only admin staff can sponsor performances.");
            return;
        }

        long performanceID = (long) view.getInput("Enter performance ID to sponsor: ");
        Performance p = getPerformanceByID(performanceID);

        if (p == null) {
            view.displayError("Performance not found.");
            return;
        }

        double amount = (double) view.getInput("Enter sponsorship amount: ");

        if (!checkIfSponsorshipPossible(Performance p, int amount)) {
            view.displayError("Sponsorship amount exceeds ticket price.");
            return;
        }

        if(getIsSponsored()) {
            view.displayError("Performance has already been sponsored.");
            return;
        }

        p.sponsor(amount);
        System.out.println("Performance sponsored successfully.");
    }
    }


    private boolean checkIfSponsorshipPossible(Performance performance, int amount) {
        return (amount <= performance.getTicketPrice();
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