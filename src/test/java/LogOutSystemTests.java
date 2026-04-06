import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Student;
import model.AdminStaff;
import model.EntertainmentProvider;
import org.junit.jupiter.api.Test;

/**
 * System tests for the Log Out use case.
 */
public class LogOutSystemTests extends TestSystemBase {

    /**
     * Tests that a logged-in student can successfully log out.
     * Verifies a success message is displayed and no user is logged in afterwards.
     */
    @Test
    void testLogoutStudentSuccess() {
        TestView view = new TestView();
        initControllers(view);

        // Login first
        view.addInputs("student1@uni.ac.uk", "pass1");
        userController.login();

        // Then logout
        userController.logout();
        assertTrue(view.hasSuccessContaining("Logged out."),
                "User should be able to logout.");
        assertNull(userController.getCurrentUser(),
                "Current user should be null.");
    }

    /**
     * Tests that a logged-in admin can successfully log out.
     * Verifies a success message is displayed and no user is logged in afterwards.
     */
    @Test
    void testLogoutAdminSuccess() {
        TestView view = new TestView();
        initControllers(view);

        // Login first
        view.addInputs("admin@uni.ac.uk", "adminpass");
        userController.login();

        // Then logout
        userController.logout();
        assertTrue(view.hasSuccessContaining("Logged out."),
                "User should bne able to logout.");
        assertNull(userController.getCurrentUser(),
                "Current user should be null.");
    }

    /**
     * Tests that a logged-in entertainment provider can successfully log out.
     * Registers an EP, logs out, logs back in, then logs out again.
     * Verifies a success message is displayed and no user is logged in afterwards.
     */
    @Test
    void testLogoutEPSuccess() {
        TestView view = new TestView();
        initControllers(view);

        // register EP
        registerEP(view);
        userController.setCurrentUser(null);

        // Login as EP
        view.addInputs("ep@musicco.com", "eppass");
        userController.login();

        // Then logout
        userController.logout();
        assertTrue(view.hasSuccessContaining("Logged out."),
                "EP should be able to logout.");
        assertNull(userController.getCurrentUser(),
                "Current user should be null.");
    }

    /**
     * Tests that logging out while not logged in produces an error.
     * Verifies an error message is displayed.
     */
    @Test
    void testLogoutWhilstNotLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        userController.setCurrentUser(null);
        userController.logout();
        assertTrue(view.hasErrorContaining("Not logged in."),
                "User should not be able to logout whilst not logged in.");
    }

    /**
     * Tests that logging out twice in a row produces an error on the second attempt.
     * Verifies the first logout succeeds and the second produces an error.
     */
    @Test
    void testLogoutTwice() {
        TestView view = new TestView();
        initControllers(view);

        // Login first
        view.addInputs("student1@uni.ac.uk", "pass1");
        userController.login();

        // Then logout
        userController.logout();

        // Attempt logout again
        userController.logout();
        assertTrue(view.hasErrorContaining("Not logged in."),
                "User should not be able to logout whilst not logged in.");
    }
}
