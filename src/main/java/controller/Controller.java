package controller;

import model.AdminStaff;
import model.EntertainmentProvider;
import model.Student;
import model.User;
import view.View;

/**
 * Abstract base controller providing shared state and helper methods
 * for all controllers in the system.
 */
public abstract class Controller {

    protected User currentUser;
    protected View view;

    /**
     * Checks whether no user is currently logged in.
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
     * Sets the current logged-in user. Called by MenuController after login/logout.
     *
     * @param user the user to set, or null to log out
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

    /**
     * Displays a numbered list of options and returns the index of the
     * user's chosen item.
     *
     * @param <T>     the type of items in the collection
     * @param options the collection of options to display
     * @param prompt  the prompt to show the user
     * @return the zero-based index of the chosen option,
     *         or -1 if the input is invalid
     */
    public <T> int selectFromMenu(java.util.Collection<T> options, String prompt) {
        assert options != null : "Options cannot be null";
        assert prompt != null : "Prompt cannot be null";

        int i = 1;
        for (T option : options) {
            view.displaySuccess(i + ". " + option.toString());
            i++;
        }

        String input = view.getInput(prompt);
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 1 && choice <= options.size()) {
                return choice - 1;
            }
        } catch (NumberFormatException e) {
            view.displayError("Invalid selection.");
        }
        return -1;
    }
}
