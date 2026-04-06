import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import model.Student;
import model.AdminStaff;
import model.EntertainmentProvider;

/**
 * System tests for the Log In use case.
 */
public class LogInSystemTests extends TestSystemBase {

    /**
     * Tests that a pre-registered student can log in with valid credentials.
     * Verifies a success message is displayed and the current user is a Student.
     */
    @Test
    void testLoginStudentSuccess() {
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("student1@uni.ac.uk", "pass1");
        userController.login();
        assertTrue(view.hasSuccessContaining("Successfully logged in."),
                "User should have successfully logged in");
        assertInstanceOf(Student.class, userController.getCurrentUser(),
                "Current user should be a Student.");
    }

    /**
     * Tests that a pre-registered admin can log in with valid credentials.
     * Verifies a success message is displayed and the current user is an AdminStaff.
     */
    @Test
    void testLoginAdminSuccess() {
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("admin@uni.ac.uk", "adminpass");
        userController.login();
        assertTrue(view.hasSuccessContaining("Successfully logged in."),
                "User should have successfully logged in");
        assertInstanceOf(AdminStaff.class, userController.getCurrentUser(),
                "Current user should be of type AdminStaff");
    }

    /**
     * Tests that login fails when a correct email but incorrect password is provided.
     * Verifies an error message is displayed and no user is logged in.
     */
    @Test
    void testLoginWrongPassword() {
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("student1@uni.ac.uk", "wrongpassword");
        userController.login();
        assertTrue(view.hasErrorContaining("Incorrect email and/or password."),
                "User should not have been able to login.");
        assertNull(userController.getCurrentUser(),
                "Current user should be null.");
    }

    /**
     * Tests that login fails when an unregistered email is provided.
     * Verifies an error message is displayed and no user is logged in.
     */
    @Test
    void testLoginWrongEmail() {
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("wrongemail@uni.ac.uk", "pass1");
        userController.login();
        assertTrue(view.hasErrorContaining("Incorrect email and/or password."),
                "User should not have been able to login.");
        assertNull(userController.getCurrentUser(),
                "Current user should be null.");
    }

    /**
     * Tests that login is rejected when a user is already logged in.
     * Verifies an error message is displayed indicating the user is already logged in.
     */
    @Test
    void testLoginWhileAlreadyLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        // Login first
        view.addInputs("student1@uni.ac.uk", "pass1");
        userController.login();

        // Try to login again
        userController.login();
        assertTrue(view.hasErrorContaining("Already logged in."),
                "User should not be able to login twice.");
    }

    /**
     * Tests that a registered entertainment provider can log in after registration.
     * Registers an EP, logs out, then logs back in with the EP credentials.
     * Verifies a success message is displayed and the current user is an EntertainmentProvider.
     */
    @Test
    void testLoginEP() {
        TestView view = new TestView();
        initControllers(view);

        // register EP
        registerEP(view);
        userController.setCurrentUser(null);

        // Login as EP
        view.addInputs("ep@musicco.com", "eppass");
        userController.login();
        assertTrue(view.hasSuccessContaining("Successfully logged in."),
                "EP should be able to log in.");
        assertInstanceOf(EntertainmentProvider.class, userController.getCurrentUser(),
                "Current user should be of type EntertainmentProvider");
    }
}
