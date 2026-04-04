import model.EntertainmentProvider;
import model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * System tests for the RegisterEP use case.
 */
public class RegisterEntertainmentProviderSystemTests extends TestSystemBase {
    /**
     * Tests that a new EP can register with valid details and then log back in.
     * Registers an EP, logs out, logs back in, and verifies the user is an EntertainmentProvider.
     */
    @Test
    void testRegisterEPSuccess() {
        TestView view = new TestView();
        initControllers(view);

        // Attempt to register new entertainment provider
        view.addInputs("newep@org.com", "password123", "New Org", "0123456789",
                "Jane Doe", "A new entertainment provider");

        userController.registerEntertainmentProvider();
        assertTrue(view.hasSuccessContaining("Successfully registered Entertainment Provider."));

        // Logout any previous users
        userController.logout();
        assertNull(userController.getCurrentUser());

        // Attempt to login new EP
        view.addInputs("newep@org.com", "password123");
        userController.login();

        assertTrue(view.hasSuccessContaining("Successfully logged in."));
        assertInstanceOf(EntertainmentProvider.class, userController.getCurrentUser());
    }

    /**
     * Tests that registration is rejected when a user is already logged in.
     * Logs in as a student first, then attempts to register an EP.
     */
    @Test
    void testRegisterEPWhileLoggedIn() {
        // Login as a student
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("student1@uni.ac.uk", "pass1");
        userController.login();
        assertInstanceOf(Student.class, userController.getCurrentUser());

        // Attempt to register as an EP
        userController.registerEntertainmentProvider();
        assertTrue(view.hasErrorContaining("Already logged in. Can't register entertainment provider."));
    }


    /**
     * Tests that registration fails when an EP with the same details already exists.
     * Registers an EP, logs out, then attempts to register again with identical details.
     */
    @Test
    void testRegisterEPDuplicateDetails() {
        TestView view = new TestView();
        initControllers(view);

        // Attempt to register new entertainment provider
        view.addInputs("newep@org.com", "password123", "New Org", "0123456789",
                "Jane Doe", "A new entertainment provider");

        userController.registerEntertainmentProvider();
        assertTrue(view.hasSuccessContaining("Successfully registered Entertainment Provider."));

        // Logout any previous users
        userController.logout();
        assertNull(userController.getCurrentUser());

        // Attempt to register with the same details again
        view.addInputs("newep@org.com", "password123", "New Org", "0123456789",
                "Jane Doe", "A new entertainment provider");

        userController.registerEntertainmentProvider();
        assertTrue(view.hasErrorContaining("Entertainment Provider already exists."));
    }

    /**
     * Tests that registration fails when the business number is invalid.
     * Uses an 8-character business number which fails the MockVerificationService check.
     */
    @Test
    void testRegisterEPInvalidBusinessNumber() {
        TestView view = new TestView();
        initControllers(view);

        // Attempt to register new entertainment provider
        view.addInputs("newep@org.com", "password123", "New Org", "01234567",
                "Jane Doe", "A new entertainment provider");
        userController.registerEntertainmentProvider();
        assertTrue(view.hasErrorContaining("Unable to verify entertainment provider."));
    }

    /**
     * Tests that the registered EP has all the correct details stored.
     * Verifies email, password, org name, business number, name, and description.
     */
    @Test
    void testRegisterEPCorrectDetails() {
        TestView view = new TestView();
        initControllers(view);

        // Attempt to register new entertainment provider
        view.addInputs("newep@org.com", "password123", "New Org", "0123456789",
                "Jane Doe", "A new entertainment provider");

        userController.registerEntertainmentProvider();

        EntertainmentProvider ep = (EntertainmentProvider) userController.getCurrentUser();

        assertEquals(ep.getEmail(), "newep@org.com");
        assertEquals(ep.getPassword(), "password123");
        assertEquals(ep.getOrgName(), "New Org");
        assertEquals(ep.getBusinessNumber(), "0123456789");
        assertEquals(ep.getName(),  "Jane Doe");
        assertEquals(ep.getDescription(), "A new entertainment provider");
    }
}