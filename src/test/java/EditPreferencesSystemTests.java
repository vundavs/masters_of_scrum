import model.StudentPreferences;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System tests for the Edit Preferences use case.
 */
public class EditPreferencesSystemTests extends TestSystemBase {

    @Test
    void testEditPreferencesSuccessfully() {
        TestView view = new TestView("student1@uni.ac.uk", "pass1");
        initControllers(view);
        userController.login();

        view.addInputs("music dance");
        userController.editPreferences();

        assertNotNull(student1.getPreferences(),
                "Student preferences should be set");
        assertTrue(view.hasSuccessContaining("updated"),
                "Success message should be displayed");
    }

    @Test
    void testEditPreferencesMusicIsSet() {
        TestView view = new TestView("student1@uni.ac.uk", "pass1");
        initControllers(view);
        userController.login();

        view.addInputs("music");
        userController.editPreferences();

        assertTrue(student1.getPreferences().preferMusicEvents,
                "Music preference should be set to true");
    }

    @Test
    void testEditPreferencesMultipleCategories() {
        TestView view = new TestView("student1@uni.ac.uk", "pass1");
        initControllers(view);
        userController.login();

        view.addInputs("music sports movie");
        userController.editPreferences();

        StudentPreferences prefs = student1.getPreferences();
        assertTrue(prefs.preferMusicEvents, "Music preference should be set");
        assertTrue(prefs.preferSportsEvents, "Sports preference should be set");
        assertTrue(prefs.preferMovieEvents, "Movie preference should be set");
    }

    @Test
    void testEditPreferencesFailsWhenNotLoggedIn() {
        TestView view = new TestView();
        initControllers(view);

        view.addInputs("music");
        userController.editPreferences();

        assertTrue(view.hasErrorContaining("student"),
                "Error should be shown when not logged in as student");
    }

    @Test
    void testEditPreferencesFailsForEP() {
        TestView view = new TestView();
        initControllers(view);

        registerEP(view);
        loginAsEP("ep@musicco.com", "eppass", view, epController, bookingController);

        view.addInputs("music");
        userController.editPreferences();

        assertTrue(view.hasErrorContaining("student"),
                "Error should be shown when EP tries to edit student preferences");
    }

    @Test
    void testEditPreferencesFailsWithEmptyInput() {
        TestView view = new TestView("student1@uni.ac.uk", "pass1");
        initControllers(view);
        userController.login();

        view.addInputs("   ");
        userController.editPreferences();

        assertTrue(view.hasErrorContaining("empty"),
                "Error should be shown for empty preference input");
    }

    @Test
    void testPreferencesAffectSearchResults() {
        TestView view = new TestView();
        initControllers(view);

        // Create a MUSIC performance and a SPORTS performance
        model.EntertainmentProvider ep = registerEP(view);
        model.Event musicEvent = new model.Event(
                nextTestEventId++, "Jazz Night", model.EventType.MUSIC, true, ep);
        events.add(musicEvent);
        ep.addEvent(musicEvent);
        model.Performance musicPerf = new model.Performance(
                nextTestPerfId++,
                java.time.LocalDateTime.now().plusDays(7),
                java.time.LocalDateTime.now().plusDays(7).plusHours(2),
                java.util.List.of("Jazz Trio"),
                "Venue A", 100, false, false, 50, 20.0,
                new java.util.ArrayList<>(), musicEvent);
        performances.add(musicPerf);
        musicEvent.addPerformance(musicPerf);

        model.Event sportsEvent = new model.Event(
                nextTestEventId++, "Football Match", model.EventType.SPORTS, true, ep);
        events.add(sportsEvent);
        ep.addEvent(sportsEvent);
        model.Performance sportsPerf = new model.Performance(
                nextTestPerfId++,
                java.time.LocalDateTime.now().plusDays(7),
                java.time.LocalDateTime.now().plusDays(7).plusHours(2),
                java.util.List.of("Team A vs Team B"),
                "Stadium", 5000, true, false, 1000, 15.0,
                new java.util.ArrayList<>(), sportsEvent);
        performances.add(sportsPerf);
        sportsEvent.addPerformance(sportsPerf);

        // Login and set music-only preference
        loginAsStudent("student1@uni.ac.uk", "pass1", view, epController, bookingController);
        view.addInputs("music");
        userController.editPreferences();

        view.addInputs(musicPerf.getStartDateTime().toLocalDate().toString());
        epController.searchForPerformances();

        // Should only see music events
        boolean allMusic = view.getPerformanceDisplays().stream()
                .allMatch(d -> d.contains("Jazz Night"));
        assertTrue(allMusic,
                "Search should only return music performances when preference is set to music");
    }
}
