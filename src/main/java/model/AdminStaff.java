package model;

/**
 * Represents an administrative staff member at the University of Hindeburgh.
 * Admin staff can manage the system through the admin menu.
 */
public class AdminStaff extends User {
    private String name;

    /**
     * Creates a new AdminStaff with the given credentials and name.
     *
     * @param email the staff member's email address
     * @param password the staff member's password
     * @param name the staff member's name
     */
    public AdminStaff(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    /**
     * Gets the staff member's name.
     *
     * @return the staff member's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the staff member's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
}