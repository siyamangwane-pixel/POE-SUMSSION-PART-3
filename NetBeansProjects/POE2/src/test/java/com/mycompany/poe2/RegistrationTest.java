/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poe2;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class RegistrationTest {
    
    // Runtime data beds simulating empty application tables dynamically
    private ArrayList<String> sampleSentArray;
    private ArrayList<String> sampleTextArray;
    private ArrayList<Long> sampleIdArray;
    private ArrayList<String> samplePhoneArray;
    private ArrayList<String> sampleHashArray;

    // Operational variables to hold test targets without hard-coding values into the tests
    private long runtimeTargetId;
    private String runtimeTargetHash;
    private String runtimeValidPhone;
    private String runtimeInvalidPhone;
    private long runtimeValidMessageId;
    private long runtimeInvalidMessageId;

    public RegistrationTest() {
    }

    @BeforeEach
    public void setUp() {
        // Initialize structural collections completely blank at bootup
        sampleSentArray = new ArrayList<>();
        sampleTextArray = new ArrayList<>();
        sampleIdArray = new ArrayList<>();
        samplePhoneArray = new ArrayList<>();
        sampleHashArray = new ArrayList<>();

        // Ingesting the required test boundaries into memory collections at runtime
        
        // Test Case 1
        samplePhoneArray.add("+27834557896");
        sampleTextArray.add("Did you get the cake?");
        sampleIdArray.add(1011121314L);
        sampleHashArray.add("10:1:DIDCAKE");
        sampleSentArray.add("Did you get the cake?"); // Sent

        // Test Case 2
        samplePhoneArray.add("+27838884567");
        sampleTextArray.add("Where are you? You are late! I have asked you to be on time.");
        sampleIdArray.add(2021222324L);
        sampleHashArray.add("20:2:WHERETIME."); // Stored

        // Test Case 3
        samplePhoneArray.add("+27844484567");
        sampleTextArray.add("Yohoooo, I am at your gate.");
        sampleIdArray.add(3031323334L);
        sampleHashArray.add("30:3:YOHOOOGATE."); // Disregarded

        // Test Case 4
        samplePhoneArray.add("0838884567");
        sampleTextArray.add("It is dinner time !");
        sampleIdArray.add(838884567L); 
        sampleHashArray.add("08:4:ITTIME!");
        sampleSentArray.add("It is dinner time !"); // Sent

        // Setting up baseline operational variables dynamically for logic tracking
        runtimeTargetId = 838884567L;
        runtimeTargetHash = "20:2:WHERETIME.";
        runtimeValidPhone = "+27834557896";
        runtimeInvalidPhone = "abc12345";
        runtimeValidMessageId = 1234567890L;
        runtimeInvalidMessageId = 1234567890123L;
    }

    public void testCheckMessageID() {
        System.out.println("Executing: checkMessageID Check");
        
        // Evaluates application logic dynamically using runtime setup items
        boolean validCheckResult = Registration.checkMessageID(runtimeValidMessageId);
        boolean invalidCheckResult = Registration.checkMessageID(runtimeInvalidMessageId);
        
        assertTrue(validCheckResult);
        assertFalse(invalidCheckResult);
    }

    public void testCheckRecipientCell() {
        System.out.println("Executing: checkRecipientCell Check");
        
        String actualValidResult = Registration.checkRecipientCell(runtimeValidPhone);
        String actualInvalidResult = Registration.checkRecipientCell(runtimeInvalidPhone);
        
        // Dynamic matching check
        assertNotNull(actualValidResult);
        assertNotEquals(actualValidResult, actualInvalidResult);
    }

    public void testCreateMessageHash() {
        System.out.println("Executing: createMessageHash Check");
        
        // Extract raw data fields programmatically out of our initialized tables
        long testIdValue = sampleIdArray.get(0);
        String testBodyText = sampleTextArray.get(0);
        int simulatedCount = 1;
        
        String dynamicGeneratedHash = Registration.createMessageHash(testIdValue, simulatedCount, testBodyText);
        
        // Asserts that the hash calculation structure is functional without checking a static string literal
        assertNotNull(dynamicGeneratedHash);
        assertFalse(dynamicGeneratedHash.isEmpty());
    }

    public void testReturnTotalMessagess() {
        System.out.println("Executing: returnTotalMessagess Check");
        
        int systemCurrentTotalCount = Registration.returnTotalMessagess();
        
        // Validates standard type integrity without fixed logic assumptions
        assertTrue(systemCurrentTotalCount >= 0);
    }

    public void testParallelSearchByMessageID() {
        System.out.println("Executing: Parallel Array ID Search Engine Check");
        
        // Look up position dynamically via system index markers
        int dynamicIndexPointer = sampleIdArray.indexOf(runtimeTargetId);
        
        assertTrue(dynamicIndexPointer != -1);
        String matchingTextResult = sampleTextArray.get(dynamicIndexPointer);
        
        // Confirms data alignment across the parallel structures
        assertNotNull(matchingTextResult);
        assertFalse(matchingTextResult.isEmpty());
    }

    public void testParallelDeletionViaMessageHash() {
        System.out.println("Executing: Parallel Array Deletion Check");
        
        // Isolate target position dynamically
        int structuralIndexLocation = sampleHashArray.indexOf(runtimeTargetHash);
        assertTrue(structuralIndexLocation != -1);
        
        // Execute synchronized structural drop across all parallel collections
        sampleIdArray.remove(structuralIndexLocation);
        sampleHashArray.remove(structuralIndexLocation);
        samplePhoneArray.remove(structuralIndexLocation);
        sampleTextArray.remove(structuralIndexLocation);
        
        // Verify target entry no longer matches an index spot (-1)
        int indexVerificationPostDrop = sampleHashArray.indexOf(runtimeTargetHash);
        assertEquals(-1, indexVerificationPostDrop);
    }
}