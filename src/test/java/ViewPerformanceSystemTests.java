import model.EntertainmentProvider;
import model.Performance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewPerformanceSystemTests extends TestSystemBase {
    @Test
    void testViewPerformanceSuccessfully() {
        //also testing successful use case for logged in student
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        epController.viewPerformance();

        assertTrue(view.hasSuccessContaining("Performance ID"),
                "Performance ID should be part of output");
    }

    @Test
    void testEPLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        epController.viewPerformance();

        assertTrue(view.hasSuccessContaining("Performance ID"),
                "Performance ID should be part of output");
    }

    @Test
    void testAdminLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsAdmin("admin@uni.ac.uk", "adminpass", view);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        epController.viewPerformance();

        assertTrue(view.hasSuccessContaining("Performance ID"),
                "Performance ID should be part of output");
    }

    @Test
    void testNotLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        view.addInputs(String.valueOf(p.getPerformanceId()));
        epController.viewPerformance();

        assertTrue(view.hasErrorContaining("must be logged in"),
                "Error should mention that user must be logged in");
    }

    @Test
    void testInputLetters() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs("abc");
        epController.viewPerformance();

        assertTrue(view.hasErrorContaining("Please enter a number"),
                "Error should mention invalid format");
    }

    @Test
    void testWrongID() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs(String.valueOf("123456"));
        epController.viewPerformance();

        assertTrue(view.hasErrorContaining("Performance not found"),
                "Error mentions performance not found");
    }

    @Test
    void testWhitespaceInput() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(" ");
        epController.viewPerformance();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should mention invalid format");
    }

    @Test
    void testEmptyInput() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs("");
        epController.viewPerformance();

        assertTrue(view.hasErrorContaining("Invalid"),
                "Error should mention invalid format");
    }

    @Test
    void testPerformanceInLaterEvent() {}

    @Test
    void testMultiplePerformances() {}

    @Test
    void testOutputFormat() {
        TestView view = new TestView();
        initControllers(view);

        EntertainmentProvider ep = registerEP(view);
        Performance p = createFuturePerformance(ep);

        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);
        view.addInputs(String.valueOf(p.getPerformanceId()));
        epController.viewPerformance();

        assertTrue(view.hasSuccessContaining("End date and time"),
                "The correct output should contain end date and time");
    }



}