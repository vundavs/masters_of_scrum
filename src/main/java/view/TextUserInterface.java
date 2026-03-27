package model;

import java.util.Collection;
import java.util.Scanner;

public class TextUserInterface implements View {
    
    // The Scanner listens to System.in (your keyboard)
    private Scanner scanner;

    public TextUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getInput(String inputPrompt) {
        // Print the prompt (like "Enter sponsorship info: ") without a new line
        System.out.print(inputPrompt); 
        // Wait for the user to type something and press Enter, then return it
        return scanner.nextLine(); 
    }

    @Override
    public void displaySuccess(String successMessage) {
        System.out.println("SUCCESS: " + successMessage);
    }

    @Override
    public void displayError(String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
    }

    @Override
    public void displayListofPerformances(Collection<String> listOfPerformanceInfo) {
        System.out.println("--- Performances ---");
        for (String info : listOfPerformanceInfo) {
            System.out.println(info);
        }
    }

    @Override
    public void displaySpecificPerformance(String performanceInfo) {
        System.out.println(performanceInfo);
    }

    @Override
    public void displayBookingRecord(String bookingRecord) {
        System.out.println("--- Booking Record ---");
        System.out.println(bookingRecord);
    }
}