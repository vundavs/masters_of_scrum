package controller;

import model.EntertainmentProvider;
import model.Performance;
import model.Event;
import model.EventType;
import view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

/**
 * Controller handling event and performance related use cases:
 * search for performances, view performance, create event,
 * cancel performance, and sponsor performance.
 */
public class EventPerformanceController extends Controller {

    private List<Event> events;
    private List<Performance> performances;

    /**
     * Creates a new EventPerformanceController.
     *
     * @param view         the view for user interaction
     * @param performances shared list of all performances
     */
    public EventPerformanceController(View view, List<Performance> performances) {
        this.view = view;
        this.performances = performances;
    }

    /**
     * Handles the search for performances by date use case.
     */
    public void searchForPerformances() {
        String dateInput = view.getInput("Enter date to search for performances in yyyy-MM-dd format");
        LocalDate searchDate = null;
        try {
            searchDate = LocalDate.parse(dateInput.trim());
        } catch (DateTimeParseException e) {
            view.displayError("Invalid date format. Must be in yyyy-MM-dd format.");
            return;
        }

        if(searchDate.isBefore(LocalDate.now())) {
            view.displayError("This date has already passed.");
        }

        if(searchDate == null) {
            view.displayError("No performances found for given date.");
            return;
        }


        for (Event e : events) {
            Collection<String> results = e.getInfoOfPerformancesOnDate(searchDate);
            for (String s : results) {
                view.displaySuccess(s);
            }
        }
    }

    /**
     * Handles the view performance use case.
     */
    public void viewPerformance() {
        if (checkCurrentUserIsGuest()) {
            view.displayError("You must be logged in to view performances.");
        }

        String input = view.getInput("Enter performance ID to view: ");

        long performanceID;
        try {
            performanceID = Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid ID. Please enter a number.");
            return;
        }

        Performance p = null;
        for (Event e : events) {
            p = e.getPerformanceById(performanceID);
            if (p != null) {
                break;
            }
        }
        if (p == null) {
            view.displayError("Performance not found.");
            return;
        }
        Event e = p.getEvent();
        view.displaySuccess(e.performanceFormat(p));
    }


    /**
     * Handles the create event use case for entertainment providers.
     */
    public Event createEvent() {
        if (!checkCurrentUserIsEntertainmentProvider()) {
            view.displayError("Only entertainment providers can create events.");
            return null;
        }

        String title = (String) view.getInput("Enter event title: ");
        title = title.trim();
        if(title.isEmpty()) {
            view.displayError("Title cannot be empty.");
            return null;
        }

        String typeInput = view.getInput("Enter event type (MUSIC, THEATRE, DANCE, MOVIE, OR SPORTS): ");
        EventType type;
        try {
            type = EventType.valueOf(typeInput.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            view.displayError("Invalid event type. Please enter a valid event type.");
            return null;
        }

        String isTicketedInput = view.getInput("Is the event ticketed? (true/false)");
        isTicketedInput = isTicketedInput.toLowerCase().trim();
        if (!isTicketedInput.equals("true") && !isTicketedInput.equals("false")) {
            view.displayError("Invalid input, please enter 'true' or 'false'.");
        }
        boolean isTicketed = Boolean.parseBoolean(isTicketedInput);

        EntertainmentProvider organiser = (EntertainmentProvider) getCurrentUser();
        Event e = new Event(title, type, isTicketed, organiser);
        events.add(e);
        view.displaySuccess("Event created successfully.");
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
        if (!checkCurrentUserIsAdmin()) {
            view.displayError("Only admin staff can sponsor performances.");
            return;
        }

        String performanceIDInput = view.getInput("Enter performance ID of performance you wish to sponsor:");
        long performanceID;
        try {
            performanceID = Long.parseLong(performanceIDInput.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid ID. Please enter a number.");
            return;
        }

        Performance p = null;
        for (Event e : events) {
            p = e.getPerformanceById(performanceID);
            if (p != null) {
                break;
            }
        }

        if (p == null) {
            view.displayError("Performance not found.");
            return;
        }

        String amountInput = view.getInput("Enter sponsorship amount: ");
        double amount;
        try {
            amount = Double.parseDouble(amountInput.trim());
        } catch (NumberFormatException e) {
            view.displayError("Invalid format. Please enter a number.");
            return;
        }

        if (!checkIfSponsorshipPossible(p, amount)) {
            view.displayError("Sponsorship amount exceeds ticket price.");
            return;
        }

        if (p.isSponsored()) {
            view.displayError("Performance has already been sponsored.");
            return;
        }

        p.sponsor(amount);
        view.displaySuccess("Performance sponsored successfully.");
    }


    private boolean checkIfSponsorshipPossible(Performance performance, double amount) {
        return (amount <= performance.getTicketPrice());
    }

}