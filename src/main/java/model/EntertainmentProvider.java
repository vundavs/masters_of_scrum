package model;
import java.util.ArrayList;

/**
 * Represents an entertainment provider who can create and manage events.
 * Extends User with organisation details and a list of events.
 */
public class EntertainmentProvider extends User {
    private String orgName;
    private String businessNumber;
    private String name;
    private String description;
    private ArrayList<Event> events;

    /**
     * Creates a new EntertainmentProvider with the given details.
     *
     * @param email          the provider's email address
     * @param password       the provider's password
     * @param orgName        the name of the organisation
     * @param businessNumber the organisation's business registration number
     * @param name           the provider's representative name
     * @param description    a description of the organisation
     */
    public EntertainmentProvider(String email, String password, String orgName,
                                 String businessNumber, String name, String description) {
        super(email, password);
        this.orgName = orgName;
        this.businessNumber = businessNumber;
        this.name = name;
        this.description = description;
        this.events = new ArrayList<Event>();
    }

    /**
     * Gets the organisation name.
     *
     * @return the organisation name
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * Sets the organisation name.
     *
     * @param orgName the new organisation name
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * Gets the business registration number
     *
     * @return the business registration number
     */
    public String getBusinessNumber() {
        return businessNumber;
    }

    /**
     * Sets the business registration number.
     *
     * @param businessNumber the new business registration number
     */
    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    /**
     * Gets the representative name.
     *
     * @return the representative name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the representative name.
     *
     * @param name the new representative name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organisation description.
     *
     * @return the organisation description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the organisation description.
     *
     * @param description the new organisation description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adds an event to this provider's list of events.
     *
     * @param event the event to add
     */
    public void addEvent(Event event) {
        events.add(event);
    }
}
