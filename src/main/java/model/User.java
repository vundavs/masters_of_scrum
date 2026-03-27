package model;

/**
 * Abstract base class for all users in the system.
 */
public abstract class User {
    private String email;
    private String password;

    /**
     * Creates a new User with the given email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     */
    protected User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the user's email address.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
