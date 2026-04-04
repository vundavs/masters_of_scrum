import model.EntertainmentProvider;
import model.Event;
import model.EventType;
import model.Performance;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Sponsor Performance use case.
 *
 * <p>Only an admin staff member may sponsor a performance. The performance
 * must exist, its event must be ticketed, and the sponsorship amount must be
 * greater than zero and no greater than the performance's ticket price.
 */
public class SponsorPerformanceSystemTests extends TestSystemBase {

    /**
     * Creates a ticketed future performance whose ticket price is 20.0.
     * Delegates to the base-class helper.
     */
    private Performance futureTicketedPerformance(EntertainmentProvider ep) {
        return createFuturePerformance(ep);
    }

    /**
     * Creates a non-ticketed future performance and adds it to the shared lists.
     *
     * @param ep the entertainment provider who owns the event
     * @return the created Performance
     */
    private Performance createNonTicketedPerformance(EntertainmentProvider ep) {
        Event event = new Event("Free Concert", EventType.MUSIC, false, ep);
        events.add(event);
        ep.addEvent(event);

        Performance p = new Performance(
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(7).plusHours(2),
                List.of("Band"),
                "Main Hall", 200, false, false, 100, 20.0,
                new ArrayList<>(), event);
        performances.add(p);
        event.addPerformance(p);
        return p;
    }

    // "Correct" path

    @Test
    void testSponsorPerformanceSuccessfully() {
        // Admin sponsors a ticketed performance with a valid amount (less than eq to ticket price).
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep); // ticketPrice = 20.0

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "15.0");
        epController.sponsorPerformance();

        assertTrue(p.getIsSponsored(),
                "Performance should be marked as sponsored");
        assertEquals(15.0, p.getSponsoredAmount(),
                "Sponsored amount should be set to 15.0");
        assertTrue(view.hasSuccessContaining("Successful"),
                "A success message should be displayed");
    }

    @Test
    void testSponsorReducesFinalTicketPrice() {
        // After sponsorship the final ticket price should be reduced by the sponsored amount.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep); // ticketPrice = 20.0

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "5.0");
        epController.sponsorPerformance();

        assertEquals(15.0, p.getFinalTicketPrice(), 0.001,
                "Final ticket price should equal ticketPrice minus sponsoredAmount");
    }

    // Authentication/authorisation failures

    @Test
    void testSponsorPerformanceFailsWhenNotLoggedIn() {
        // No user logged in — sponsorship should be rejected immediately.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep);

        epController.setCurrentUser(null);
        view.addInputs(String.valueOf(p.getPerformanceId()), "15.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("admin"),
                "Error should state that admin must be logged in");
    }

    @Test
    void testSponsorPerformanceFailsWhenStudentLoggedIn() {
        // Students are not permitted to sponsor performances.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "15.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("admin"),
                "Error should state that admin must be logged in");
    }

    @Test
    void testSponsorPerformanceFailsWhenEPLoggedIn() {
        // Entertainment providers are not permitted to sponsor performances.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep);

        // EP is already logged in from registerEP; set it on epController
        epController.setCurrentUser(ep);
        view.addInputs(String.valueOf(p.getPerformanceId()), "15.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("admin"),
                "Error should state that admin must be logged in");
    }

    // Performance not found

    @Test
    void testSponsorPerformanceFailsForNonExistentId() {
        // Performance ID 99999 does not exist in the system.
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);

        view.addInputs("99999", "15.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("does not exist"),
                "Error should state that the performance does not exist");
    }

    @Test
    void testSponsorPerformanceFailsForNonNumericId() {
        // Non-numeric performance ID should be rejected.
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);

        view.addInputs("abc", "15.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should state that the performance ID is invalid");
    }

    // Non-ticketed performance

    @Test
    void testSponsorPerformanceFailsForNonTicketedPerformance() {
        // Non-ticketed performances cannot be sponsored.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createNonTicketedPerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "15.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("non ticketed"),
                "Error should state that the performance cannot be sponsored");
        assertFalse(p.getIsSponsored(),
                "Non-ticketed performance should not be marked as sponsored");
    }

    // Amount validation

    @Test
    void testSponsorPerformanceFailsWhenAmountExceedsTicketPrice() {
        // Sponsorship amount greater than the ticket price (20.0) is invalid.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep); // ticketPrice = 20.0

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "25.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("invalid"),
                "Error should state that the amount is invalid");
        assertFalse(p.getIsSponsored(),
                "Performance should not be sponsored when amount exceeds ticket price");
    }

    @Test
    void testSponsorPerformanceFailsWhenAmountIsZero() {
        // A sponsorship amount of zero is not allowed.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "0.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("invalid"),
                "Error should state that the amount is invalid");
        assertFalse(p.getIsSponsored(),
                "Performance should not be sponsored with a zero amount");
    }

    @Test
    void testSponsorPerformanceFailsWhenAmountIsNegative() {
        // A negative sponsorship amount is not allowed.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "-10.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("invalid"),
                "Error should state that the amount is invalid");
        assertFalse(p.getIsSponsored(),
                "Performance should not be sponsored with a negative amount");
    }

    @Test
    void testSponsorPerformanceFailsForNonNumericAmount() {
        // Non-numeric amount input should be rejected.
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = futureTicketedPerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "abc");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should state that the sponsorship amount is invalid");
        assertFalse(p.getIsSponsored(),
                "Performance should not be sponsored after invalid amount input");
    }
}
