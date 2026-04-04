import model.EntertainmentProvider;
import model.Event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Create Event use case.
 */
public class CreateEventSystemTests extends TestSystemBase {

    @Test
    void testCreateEventSuccess() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Jazz Show", "MUSIC", "true");
        epController.createEvent();

        assertEquals(1, events.size(),
                "One event should be created");
        assertTrue(view.hasSuccessContaining("successfully"),
                "Success message should be displayed");
    }

    @Test
    void testStudentCannotCreateEvent() {
        TestView view = new TestView();
        initControllers(view);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs("Jazz Show", "MUSIC", "true");
        epController.createEvent();

        assertEquals(0, events.size(),
                "Student should not be able to create an event");
        assertTrue(view.hasErrorContaining("entertainment providers"),
                "Error message should mention entertainment provider requirement");
    }

    @Test
    void testAdminCannotCreateEvent() {
        TestView view = new TestView();
        initControllers(view);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs("Jazz Show", "MUSIC", "true");
        epController.createEvent();

        assertEquals(0, events.size(),
                "Admin should not be able to create an event");
        assertTrue(view.hasErrorContaining("entertainment providers"),
                "Error message should mention entertainment provider requirement");
    }

    @Test
    void testTitleEmptyInput() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("", "MUSIC", "true");
        epController.createEvent();

        assertEquals(0, events.size(),
                "Event should not be made if title is empty");
        assertTrue(view.hasErrorContaining("empty"),
                "Error message should mention empty title");
    }

    @Test
    void testTypeEmptyInput() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Romeo and Juliet", "", "true");

        assertEquals(0, events.size(),
                "Event should not be made if type is empty");
        assertTrue(view.hasErrorContaining("Invalid"),
                "Error message should mention invalid input");
    }

    @Test
    void testTicketedEmptyInput() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Jazz Show", "MUSIC", " ");
        epController.createEvent();

        assertEquals(0, events.size(),
                "Event should not be made if isTicketed is empty");
        assertTrue(view.hasErrorContaining("Invalid"),
                "Error message should mention invalid input");
    }

    @Test
    void testTitleWhitespaceInput() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(" Romeo and Juliet ", "THEATRE", "true");
        epController.createEvent();

        assertEquals(1, events.size(),
                "One event should be created");
        assertTrue(view.hasSuccessContaining("successfully"),
                "Success message should be displayed");
    }

    @Test
    void testMustBeLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("Jazz Show", "MUSIC", "true");
        epController.createEvent();

        assertEquals(0, events.size(),
                "Event should not be created if a user is not logged in");
        assertTrue(view.hasErrorContaining("entertainment providers"),
                "Error message should mention entertainment provider requirement");
    }

    @Test
    void testLowercaseTypePasses() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Black Swan", "dance", "false");
        epController.createEvent();

        assertEquals(1, events.size(),
                "One event should be created");
        assertTrue(view.hasSuccessContaining("successfully"),
                "Success message should be displayed");
    }

    @Test
    void testUppercaseTicketedPasses() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Black Swan", "DANCE", "TRUE");
        epController.createEvent();

        assertEquals(1, events.size(),
                "One event should be created");
        assertTrue(view.hasSuccessContaining("successfully"),
                "Success message should be displayed");
    }

    @Test
    void testCreateFailsInvalidType() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Art Show", "OTHER", "true");
        epController.createEvent();

        assertEquals(0, events.size(),
                "No event should be created: invalid type");
        assertTrue(view.hasErrorContaining("Invalid"),
                "Error message should mention invalid type");
    }

    @Test
    void testCreateFailsInvalidTicketed() {
        TestView view = new TestView();
        initControllers(view);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("Black Swan", "DANCE", "yes");
        epController.createEvent();

        assertEquals(0, events.size(),
                "No event should be created: invalid ticketed");
        assertTrue(view.hasErrorContaining("Invalid"),
                "Error message should mention invalid istTicketed input");
    }
}