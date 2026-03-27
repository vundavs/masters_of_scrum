import model.*;
import controller.*;
import external.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;

public class SponsorPerformanceSystemTests {

    private EventPerformanceController epController;
    private TextUserInterface mockUI;
    private Performance testPerformance;

    @BeforeEach
    public void setUp() {
        mockUI = mock(TextUserInterface.class);
        
        epController = new EventPerformanceController();


        Event testEvent = new Event(1L, "Spring Concert", EventType.MUSIC, true);

        testPerformance = new Performance(101L, LocalDateTime.now().plusDays(5), 
                                          LocalDateTime.now().plusDays(5).plusHours(2), 
                                          null, "Main Hall", 100, false, false, 100, 0, 50.0);

        testEvent.addPerformance(testPerformance);
        epController.addEvent(testEvent);
        epController.addPerformance(testPerformance);
    }

    @Test
    public void testSponsorPerformance_Success() {

        when(mockUI.getInput(anyString()))
            .thenReturn("101")   // Simulates typing the performance ID
            .thenReturn("25.0"); // Simulates typing the valid sponsorship amount

        epController.sponsorPerformance();

        assertTrue(testPerformance.isSponsored(), "The performance should be marked as sponsored.");
        assertEquals(25.0, testPerformance.getSponsoredAmount(), "The sponsored amount should be updated to 25.0.");
        
        verify(mockUI).displaySuccess("Sponsorship Successful!");
    }
}