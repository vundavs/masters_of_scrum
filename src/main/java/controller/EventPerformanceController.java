package controller;

import model.Performance;
import view.View;

import java.util.List;

/**
 * Controller handling event and performance related use cases:
 * search for performances, view performance, create event,
 * cancel performance, and sponsor performance.
 */
public class EventPerformanceController extends Controller {

    private final List<Performance> performances;

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
     * Handles the search for performances use case.
     * TODO: implement full search logic.
     */
    public void searchForPerformances() {
        // TODO: implement search for performances
        view.displayError("Search for performances not yet implemented.");
    }

    /**
     * Handles the view performance use case.
     * TODO: implement full view logic.
     */
    public void viewPerformance() {
        // TODO: implement view performance
        view.displayError("View performance not yet implemented.");
    }

    /**
     * Handles the create event use case for entertainment providers.
     * TODO: implement full creation logic.
     */
    public void createEvent() {
        // TODO: implement create event
        view.displayError("Create event not yet implemented.");
    }

    /**
     * Handles the cancel performance use case for entertainment providers.
     * TODO: implement full cancellation logic.
     */
    public void cancelPerformance() {
        // TODO: implement cancel performance
        view.displayError("Cancel performance not yet implemented.");
    }

    /**
     * Handles the sponsor performance use case for admin staff.
     * TODO: implement full sponsorship logic.
     */
    public void sponsorPerformance() {
        // TODO: implement sponsor performance
        view.displayError("Sponsor performance not yet implemented.");
    }
}
