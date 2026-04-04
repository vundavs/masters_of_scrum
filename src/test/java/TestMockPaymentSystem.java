import external.MockPaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMockPaymentSystem {
    private MockPaymentSystem mockPaymentSystem;

    @BeforeEach
    void setUp() {
        mockPaymentSystem = new MockPaymentSystem();
    }

    //payment use cases

    @Test
    void testPaymentSuccessful() {
        boolean result = mockPaymentSystem.processPayment(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com", 90);
        assertTrue(result, "Payment should be successful with valid inputs");
    }

    @Test
    void testPaymentOneTicket() {
        boolean result = mockPaymentSystem.processPayment(1, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com", 90);
        assertTrue(result, "Payment should be successful with valid inputs (1 ticket)");
    }

    @Test
    void testPaymentNullTitle() {
        boolean result = mockPaymentSystem.processPayment(3, null,
                "student1@uni.ac.uk", 71234, "ep@musicco.com", 90);
        assertFalse(result, "Payment should fail with null event title input");
    }

    @Test
    void testPaymentNullStudentEmail() {
        boolean result = mockPaymentSystem.processPayment(1, "Jazz Show",
                null, 71234, "ep@musicco.com", 90);
        assertFalse(result, "Payment should fail with null student email input");
    }

    @Test
    void testPaymentNullEPEmail() {
        boolean result = mockPaymentSystem.processPayment(1, "Jazz Show",
                "student1@uni.ac.uk", 71234, null, 90);
        assertFalse(result, "Payment should fail with null EP email input");
    }

    @Test
    void testPaymentZeroTickets() {
        boolean result = mockPaymentSystem.processPayment(0, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com", 90);
        assertFalse(result, "Payment should fail with zero tickets");
    }

    @Test
    void testPaymentNegativeTickets() {
        boolean result = mockPaymentSystem.processPayment(-1, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com", 90);
        assertFalse(result, "Payment should fail with negative tickets");
    }

    @Test
    void testPaymentZeroAmount() {
        boolean result = mockPaymentSystem.processPayment(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com", 0);
        assertFalse(result, "Payment should fail with zero transaction amount");
    }

    @Test
    void testPaymentNegativeAmount() {
        boolean result = mockPaymentSystem.processPayment(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com", -90);
        assertFalse(result, "Payment should fail with negative transaction amount");
    }

    //refund use cases

    @Test
    void testRefundSuccess() {
        boolean result = mockPaymentSystem.processRefund(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com",
                90, "Event cancelled due to weather");
        assertTrue(result, "Refund should be successful");
    }

    @Test
    void testRefundSuccessNoMessage() {
        boolean result = mockPaymentSystem.processRefund(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com",
                90, "");
        assertTrue(result, "Refund should be successful with blank message");
    }

    @Test
    void testRefundNullTitle() {
        boolean result = mockPaymentSystem.processRefund(3, null,
                "student1@uni.ac.uk", 71234, "ep@musicco.com",
                90, "Event cancelled due to weather");
        assertFalse(result, "Refund should not be successful with null title input");
    }


    @Test
    void testRefundNullStudentEmail() {
        boolean result = mockPaymentSystem.processRefund(3, "Jazz Show",
                null, 71234, "ep@musicco.com",
                90, "");
        assertFalse(result, "Refund should not be successful with null student email input");
    }

    @Test
    void testRefundNullEPEmail() {
        boolean result = mockPaymentSystem.processRefund(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, null,
                90, "");
        assertFalse(result, "Refund should not be successful with null EP email input");
    }

    @Test
    void testRefundZeroTickets() {
        boolean result = mockPaymentSystem.processRefund(0, "Jazz Show",
                "student1@ed.ac.uk", 71234, "ep@musicco.com",
                90, "");
        assertFalse(result, "Refund should not be successful zero tickets");
    }

    @Test
    void testRefundNegativeTickets() {
        boolean result = mockPaymentSystem.processRefund(-3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com",
                90, "");
        assertFalse(result, "Refund should not be successful negative tickets");
    }

    @Test
    void testRefundZeroAmount() {
        boolean result = mockPaymentSystem.processRefund(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com",
                0, "");
        assertFalse(result, "Refund should not be successful 0 refund amount");
    }

    @Test
    void testRefundNegativeAmount() {
        boolean result = mockPaymentSystem.processRefund(3, "Jazz Show",
                "student1@uni.ac.uk", 71234, "ep@musicco.com",
                -90, "");
        assertFalse(result, "Refund should not be successful with a negatuve refund amount");
    }
}