package model;
import java.util.ArrayList;

/**
 * Represents a student user in the system.
 * Students can search for performances, book performances, and manage their bookings.
 */
public class Student extends User {
    private String name;
    private int phoneNumber;
    private ArrayList<Booking> bookings;
    private StudentPreferences preferences;

    /**
     * Creates a new Student with the given details.
     *
     * @param email the student's email address
     * @param password the student's password
     * @param name the student's name
     * @param phoneNumber the student's phone number
     */
    public Student(String email, String password, String name, int phoneNumber) {
        super(email, password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bookings = new ArrayList<Booking>();
        this.preferences = new StudentPreferences(false, false, false, false, false);
    }

    /**
     * Gets the student's name.
     *
     * @return the student's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the student's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the student's phone number.
     *
     * @return the student's phone number
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the student's phone number.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }



    /**
     * Gets the student's list of bookings.
     *
     * @return the list of bookings
     */
    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    /**
     * Adds a booking to the student's list of bookings.
     *
     * @param booking the booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Gets the student's preferences.
     *
     * @return the student's preferences
     */
    public StudentPreferences getPreferences() {
        return preferences;
    }
}