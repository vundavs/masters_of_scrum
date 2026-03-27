package model;

/**
 * Stores a student's event type preferences, indicating which categories
 * of events they are interested in attending.
 */
public class StudentPreferences {
    public boolean preferMusicEvents;
    public boolean preferTheaterEvents;
    public boolean preferDanceEvents;
    public boolean preferMovieEvents;
    public boolean preferSportsEvents;

    /**
     * @param preferMusicEvents  whether the student prefers music events
     * @param preferTheaterEvents whether the student prefers theatre events
     * @param preferDanceEvents  whether the student prefers dance events
     * @param preferMovieEvents  whether the student prefers movie events
     * @param preferSportsEvents whether the student prefers sports events
     */
    public StudentPreferences(boolean preferMusicEvents, boolean preferTheaterEvents,
                              boolean preferDanceEvents, boolean preferMovieEvents,
                              boolean preferSportsEvents) {
        this.preferMusicEvents = preferMusicEvents;
        this.preferTheaterEvents = preferTheaterEvents;
        this.preferDanceEvents = preferDanceEvents;
        this.preferMovieEvents = preferMovieEvents;
        this.preferSportsEvents = preferSportsEvents;
    }


    /**
     * Updates preferences based on a raw string containing event type keywords.
     * Recognised keywords (case-insensitive): music, theater, sports, dance, movie.
     *
     * @param studentRawStringPreferences a string containing preference keywords
     * @return true after preferences have been updated
     */
    public boolean updatePreferences(String studentRawStringPreferences) {
        studentRawStringPreferences = studentRawStringPreferences.toLowerCase();

        if (studentRawStringPreferences.contains("music"))
            this.preferMusicEvents = true;

        if (studentRawStringPreferences.contains("theater"))
            this.preferTheaterEvents = true;

        if (studentRawStringPreferences.contains("sports"))
            this.preferSportsEvents = true;

        if (studentRawStringPreferences.contains("dance"))
            this.preferDanceEvents = true;

        if (studentRawStringPreferences.contains("movie"))
            this.preferMovieEvents = true;

        return true;
    }

}