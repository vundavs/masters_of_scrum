package controller;

import model.EntertainmentProvider;
import model.Performance;
import model.Event;
import model.EventType;
import model.Booking;
import model.BookingStatus;
import external.PaymentSystem;
import view.View;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

/**
 * Controller handling event and performance related use cases:
 * search for performances, view performance, create event,
 * cancel performance, and sponsor performance.
 */
public class EventPerformanceController extends Controller {

    private long nextEventID = 1;
    private long nextPerformanceID = 1;

    private List<Event> events;
    private List<Performance> performances;

    private PaymentSystem paymentSystem;

    /**
     * Creates a new EventPerformanceController.
     *
     * @param view         the view for user interaction
     * @param events       shared list of all events
     * @param performances shared list of all performances
     * @param paymentSystem the payment system for processing refunds
     */
    public EventPerformanceController(View view, List<Event> events, List<Performance> performances, PaymentSystem paymentSystem) {
        this.view = view;
        this.events = events;
        this.performances = performances;
        this.paymentSystem = paymentSystem;
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


        boolean found = false;
        for (Event e : events) {
            Collection<String> results = e.getInfoOfPerformancesOnDate(searchDate);
            for (String s : results) {
                view.displaySuccess(s);
                found = true;
            }
        }
        if (!found) {
            view.displayError("No performances found for given date.");
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

        Performance p = getPerformanceByID(performanceID);
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
            return null;
        }
        boolean isTicketed = Boolean.parseBoolean(isTicketedInput);

        EntertainmentProvider organiser = (EntertainmentProvider) getCurrentUser();
        Event e = new Event(nextEventID++, title, type, isTicketed, organiser);
        organiser.addEvent(e);
        events.add(e);
        view.displaySuccess("Event created successfully.");
        return e;
    }

    /**
     * Adds an existing event to the shared events list.
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Adds an existing performance to the shared performances list.
     *
     * @param p the performance to add
     */
    public void addPerformance(Performance p) {
        performances.add(p);
    }

    /**
     * Returns the event with the given ID, or null if not found.
     *
     * @param eventID the event ID to search for
     * @return the matching Event, or null
     */
    public Event getEventByID(long eventID) {
        for (Event e : events) {
            if (e.getEventID() == eventID) {
                return e;
            }
        }
        return null;
    }

    /**
     * Returns the first event whose title matches the given string (case-insensitive),
     * or null if not found.
     *
     * @param title the event title to search for
     * @return the matching Event, or null
     */
    public Event getEventByTitle(String title) {
        for (Event e : events) {
            if (e.getTitle().equalsIgnoreCase(title)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Returns the performance with the given ID, or null if not found.
     *
     * @param performanceID the performance ID to search for
     * @return the matching Performance, or null
     */
    public Performance getPerformanceByID(long performanceID) {
        for (Event e : events) {
            Performance p = e.getPerformanceById(performanceID);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    /**
     * Handles the cancel performance use case for entertainment providers.
     * Loops on invalid performance ID format; returns on semantic errors.
     */
    public void cancelPerformance() {
        if (!checkCurrentUserIsEntertainmentProvider()) {
            view.displayError("You must be logged in as an entertainment provider.");
            return;
        }

        long performanceID = -1;
        while (true) {
            String input = view.getInput("Enter performance ID to cancel:");
            try {
                performanceID = Long.parseLong(input.trim());
                break;
            } catch (NumberFormatException e) {
                view.displayError("Invalid performance ID.");
            }
        }

        Performance performance = getPerformanceByID(performanceID);
        if (performance == null) {
            view.displayError("Performance with given number does not exist.");
            return;
        }

        EntertainmentProvider ep = (EntertainmentProvider) currentUser;
        if (!performance.checkCreatedByEP(ep.getEmail())) {
            view.displayError("This performance does not belong to you.");
            return;
        }

        if (!performance.checkHasNotHappenedYet()) {
            view.displayError("Performance can't be cancelled as it has already happened.");
            return;
        }

        if (performance.hasActiveBookings()) {
            String organiserMessage = view.getInput("Enter a cancellation message for affected students:");
            while (organiserMessage == null || organiserMessage.trim().isEmpty()) {
                view.displayError("Please provide a non-empty message for the students.");
                organiserMessage = view.getInput("Enter a cancellation message for affected students:");
            }

            for (Booking b : performance.getBookings()) {
                if (b.getStatus() == BookingStatus.ACTIVE) {
                    boolean refundSuccessful = paymentSystem.processRefund(
                            b.getNumTickets(),
                            performance.getEventTitle(),
                            b.getStudent().getEmail(),
                            b.getStudent().getPhoneNumber(),
                            performance.getOrganiserEmail(),
                            b.getAmountPaid(),
                            organiserMessage);

                    if (!refundSuccessful) {
                        view.displayError("There was an issue with a refund. The performance cannot be cancelled.");
                        return;
                    }
                    b.cancelByProvider();
                }
            }
        }

        performance.cancel();
        view.displaySuccess("Cancellation Successful!");
    }

    /**
     * Handles the sponsor performance use case for admin staff.
     * Loops on invalid performance ID or amount format; returns on semantic errors.
     */
    public void sponsorPerformance() {
        if (!checkCurrentUserIsAdmin()) {
            view.displayError("Only admin staff can sponsor performances.");
            return;
        }

        long performanceID = -1;
        while (true) {
            String performanceIDInput = view.getInput("Enter performance ID of performance you wish to sponsor:");
            try {
                performanceID = Long.parseLong(performanceIDInput.trim());
                break;
            } catch (NumberFormatException e) {
                view.displayError("Invalid ID. Please enter a number.");
            }
        }

        Performance p = getPerformanceByID(performanceID);

        if (p == null) {
            view.displayError("Performance does not exist.");
            return;
        }

        if (!p.checkIfEventIsTicketed()) {
            view.displayError("Cannot sponsor a non ticketed performance.");
            return;
        }

        double amount = 0;
        while (true) {
            String amountInput = view.getInput("Enter sponsorship amount: ");
            try {
                amount = Double.parseDouble(amountInput.trim());
                break;
            } catch (NumberFormatException e) {
                view.displayError("Invalid format. Please enter a number.");
            }
        }

        if (amount <= 0) {
            view.displayError("Sponsorship amount is invalid.");
            return;
        }

        if (!checkIfSponsorshipPossible(p, amount)) {
            view.displayError("Sponsorship amount is invalid.");
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
