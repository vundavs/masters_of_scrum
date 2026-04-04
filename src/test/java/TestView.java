import view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A test-only implementation of {@link View} that uses a pre-loaded queue of
 * strings in place of real keyboard input, and records every message that would
 * otherwise be printed to the console.
 *
 * <p>Use {@link #addInputs(String...)} to enqueue the answers a simulated user
 * would type.  Use {@link #hasSuccessContaining} / {@link #hasErrorContaining}
 * to assert that the right messages were displayed.
 */
public class TestView implements View {

    private final Queue<String> inputs = new ArrayDeque<>();
    private final List<String> successMessages = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();
    private final List<String> bookingRecords = new ArrayList<>();
    private final List<String> performanceDisplays = new ArrayList<>();

    /** Creates a TestView with no pre-loaded inputs. */
    public TestView() {}

    /**
     * Creates a TestView with initial inputs already queued.
     *
     * @param initialInputs the inputs to pre-load, in order
     */
    public TestView(String... initialInputs) {
        Collections.addAll(inputs, initialInputs);
    }

    /**
     * Enqueues additional inputs to be consumed by subsequent {@link #getInput}
     * calls.
     *
     * @param moreInputs the inputs to add, in order
     */
    public void addInputs(String... moreInputs) {
        Collections.addAll(inputs, moreInputs);
    }

    // -----------------------------------------------------------------------
    // View implementation
    // -----------------------------------------------------------------------

    /**
     * Returns the next queued input.
     *
     * @throws NoSuchElementException if the queue is empty (test has not
     *                                provided enough inputs)
     */
    @Override
    public String getInput(String prompt) {
        if (inputs.isEmpty()) {
            throw new NoSuchElementException(
                    "No more test inputs available for prompt: \"" + prompt + "\"");
        }
        return inputs.poll();
    }

    @Override
    public void displaySuccess(String message) {
        successMessages.add(message);
    }

    @Override
    public void displayError(String message) {
        errorMessages.add(message);
    }

    @Override
    public void displayListofPerformances(Collection<String> list) {
        performanceDisplays.addAll(list);
    }

    /**
     * Returns the list of performance strings that were displayed via
     * {@link #displayListofPerformances}.
     *
     * @return displayed performance strings
     */
    public List<String> getPerformanceDisplays() {
        return performanceDisplays;
    }

    @Override
    public void displaySpecificPerformance(String info) {
        // Not needed for assertion purposes; intentionally empty.
    }

    @Override
    public void displayBookingRecord(String record) {
        bookingRecords.add(record);
    }

    // -----------------------------------------------------------------------
    // Assertion helpers
    // -----------------------------------------------------------------------

    /**
     * Returns true if any recorded success message contains the given text
     * (case-insensitive).
     *
     * @param text the substring to look for
     * @return true if a match is found
     */
    public boolean hasSuccessContaining(String text) {
        String lower = text.toLowerCase();
        return successMessages.stream().anyMatch(m -> m.toLowerCase().contains(lower));
    }

    /**
     * Returns true if any recorded error message contains the given text
     * (case-insensitive).
     *
     * @param text the substring to look for
     * @return true if a match is found
     */
    public boolean hasErrorContaining(String text) {
        String lower = text.toLowerCase();
        return errorMessages.stream().anyMatch(m -> m.toLowerCase().contains(lower));
    }

    /**
     * Returns the list of booking records that were displayed.
     *
     * @return booking record strings
     */
    public List<String> getBookingRecords() {
        return bookingRecords;
    }
}
