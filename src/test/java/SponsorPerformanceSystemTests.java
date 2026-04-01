import model.EntertainmentProvider;
import model.Performance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Sponsor Performance use case.
 */
public class SponsorPerformanceSystemTests extends TestSystemBase {

    @Test
    void testSponsorPerformanceSuccessfully() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()), "25.0");
        epController.sponsorPerformance();

        assertTrue(p.isSponsored(),
                "Performance should be marked as sponsored");
        assertEquals(25.0, p.getSponsoredAmount(),
                "Sponsored amount should be updated to 25.0");
        assertTrue(view.hasSuccessContaining("Successful"),
                "Success message should be displayed");
    }

    @Test
    void testSponsorPerformanceFailsWhenNotLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        view.addInputs(String.valueOf(p.getPerformanceId()), "25.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("admin"),
                "Error should state that admin must be logged in");
    }

    @Test
    void testSponsorPerformanceFailsWhenStudentLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()), "25.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("admin"),
                "Error should state that admin must be logged in");
    }

    @Test
    void testSponsorPerformanceFailsForNonExistentId() {
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);

        view.addInputs("99999", "25.0");
        epController.sponsorPerformance();

        assertTrue(view.hasErrorContaining("does not exist"),
                "Error should state that the performance does not exist");
    }
}
