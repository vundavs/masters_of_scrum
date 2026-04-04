import model.EntertainmentProvider;
import model.Performance;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchPerformancesSystemTests extends TestSystemBase{
    @Test
    void testSearchPerformancesSuccessfully() {
        //also testing successful use case for logged in student
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(p.getStartDateTime().toLocalDate().toString());
        epController.searchForPerformances();

        assertTrue(view.hasSuccessContaining("Performance ID"),
                "Successful output should contain performance and its ID");
    }

    @Test
    void testEPLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(p.getStartDateTime().toLocalDate().toString());
        epController.searchForPerformances();

        assertTrue(view.hasSuccessContaining("Performance ID"),
                "Successful output should contain performance and its ID");
    }

    @Test
    void testAdminLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(p.getStartDateTime().toLocalDate().toString());
        epController.searchForPerformances();

        assertTrue(view.hasSuccessContaining("Performance ID"),
                "Successful output should contain performance and its ID");
    }

    @Test
    void testNoPerformancesFound() {
            TestView view = new TestView();
            initControllers(view);

            EntertainmentProvider ep = registerEP(view);
            Performance p = createFuturePerformance(ep);

            loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
            view.addInputs(p.getStartDateTime().toLocalDate().minusDays(1).toString());
            epController.searchForPerformances();

            assertTrue(view.hasErrorContaining("No performances found"),
                    "Error should mention no performances found for date");

    }

    @Test
    void testDateInPast() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(p.getStartDateTime().toLocalDate().minusYears(1).toString());
        epController.searchForPerformances();

        assertTrue(view.hasErrorContaining("passed"),
                "Error message should mentioned the date entered has passed");
    }

    @Test
    void testInvalidWrongOrderInput() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatted = p.getStartDateTime().toLocalDate().format(fmt);
        view.addInputs(formatted);
        epController.searchForPerformances();

        assertTrue(view.hasErrorContaining("format"),
                "Error message should mentioned the incorrect format");
    }

    @Test
    void testInvalidStringInput() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs("abcd");
        epController.searchForPerformances();

        assertTrue(view.hasErrorContaining("format"),
                "Error message should mentioned the incorrect format");
    }

    @Test
    void testEmptyInput() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs("");
        epController.searchForPerformances();

        assertTrue(view.hasErrorContaining("format"),
                "Error message should mentioned the incorrect format");
    }
}