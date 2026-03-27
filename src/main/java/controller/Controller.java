package controller;

import model.*;

/**
 * Abstract base controller providing shared state and helper methods
 * for all controllers in the system.
 */
public abstract class Controller {

    protected User currentUser;
    protected view.View view;

    /**
     * Checks whether the current user is a guest (not logged in).
     *
     * @return true if no user is logged in
     */
    protected boolean checkCurrentUserIsGuest() {
        return currentUser == null;
    }

    /**
     * Checks whether the current user is an admin staff member.
     *
     * @return true if current user is AdminStaff
     */
    protected boolean checkCurrentUserIsAdmin() {
        return currentUser instanceof AdminStaff;
    }

    /**
     * Checks whether the current user is a student.
     *
     * @return true if current user is Student
     */
    protected boolean checkCurrentUserIsStudent() {
        return currentUser instanceof Student;
    }

    /**
     * Checks whether the current user is an entertainment provider.
     *
     * @return true if current user is EntertainmentProvider
     */
    protected boolean checkCurrentUserIsEntertainmentProvider() {
        return currentUser instanceof EntertainmentProvider;
    }

    /**
     * Sets the current logged-in user.
     *
     * @param user the user to set as current
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Returns the current logged-in user.
     *
     * @return the current user, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
}