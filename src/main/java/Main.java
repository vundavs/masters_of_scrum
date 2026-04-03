import controller.MenuController;
import external.MockPaymentSystem;
import external.MockVerificationService;
import view.TextUserInterface;

/**
 * Entry point for the events application.
 * Wires up the controllers and starts the main menu loop.
 */
public class Main {

    /**
     * Starts the application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        MenuController menuController = new MenuController(
                new TextUserInterface(),
                new MockPaymentSystem(),
                new MockVerificationService()
        );
        menuController.mainMenu();
    }
}