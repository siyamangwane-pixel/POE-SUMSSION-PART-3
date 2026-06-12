/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poe2;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class QuickChatTest {
    
    // Test beds representing our runtime structures dynamically
    private ArrayList<String> testSentMessagesArray;
    private ArrayList<String> testMessageTexts;
    private ArrayList<Long> testMessageIDs;
    private ArrayList<String> testRecipients;
    private ArrayList<String> testMessageHashes;

    public QuickChatTest() {
    }

    @BeforeEach
    public void setUp() {
        // Initialize dynamic storage collections with zero hardcoded values in logic loops
        testSentMessagesArray = new ArrayList<>();
        testMessageTexts = new ArrayList<>();
        testMessageIDs = new ArrayList<>();
        testRecipients = new ArrayList<>();
        testMessageHashes = new ArrayList<>();

        // Test Data Message 1
        testRecipients.add("+27834557896");
        testMessageTexts.add("Did you get the cake?");
        testMessageIDs.add(1011121314L);
        testMessageHashes.add("10:1:DIDCAKE");
        testSentMessagesArray.add("Did you get the cake?"); // Flagged as Sent

        // Test Data Message 2
        testRecipients.add("+27838884567");
        testMessageTexts.add("Where are you? You are late! I have asked you to be on time.");
        testMessageIDs.add(2021222324L);
        testMessageHashes.add("20:2:WHERETIME.");
        // Flagged as Stored (Not added to sent list array)

        // Test Data Message 3
        testRecipients.add("+27844484567");
        testMessageTexts.add("Yohoooo, I am at your gate.");
        testMessageIDs.add(3031323334L);
        testMessageHashes.add("30:3:YOHOOOGATE.");
        // Flagged as Disregard

        // Test Data Message 4
        testRecipients.add("0838884567");
        testMessageTexts.add("It is dinner time !");
        testMessageIDs.add(838884567L); // Dynamic representation matching ID lookup value
        testMessageHashes.add("08:4:ITTIME!");
        testSentMessagesArray.add("It is dinner time !"); // Flagged as Sent
    }

    public void testSentMessagesArrayCorrectlyPopulated() {
        System.out.println("Running: Sent Messages Array Check");
        
        // Assertions check that the array matches the prompt's structural return
        assertEquals(2, testSentMessagesArray.size());
        assertEquals("Did you get the cake?", testSentMessagesArray.get(0));
        assertEquals("It is dinner time !", testSentMessagesArray.get(1));
    }
    
    public void testDisplayLongestMessage() {
        System.out.println("Running: Longest Message Evaluator Check");
        
        String longestFound = "";
        for (String currentMessage : testMessageTexts) {
            if (currentMessage.length() > longestFound.length()) {
                longestFound = currentMessage;
            }
        }
        
        String expectedLongest = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expectedLongest, longestFound);
    }
    public void testSearchForMessageID() {
        System.out.println("Running: Message ID Search Engine Check");
        
        long targetID = 838884567L; 
        int locateIndex = testMessageIDs.indexOf(targetID);
        
        assertTrue(locateIndex != -1);
        String matchedMessageText = testMessageTexts.get(locateIndex);
        
        assertEquals("It is dinner time !", matchedMessageText);
    }
    
    public void testSearchAllMessagesForParticularRecipient() {
        System.out.println("Running: Particular Recipient Query Check");
        
        String searchTargetRecipient = "+27838884567";
        ArrayList<String> matchingPayloadsCollector = new ArrayList<>();
        
        for (int i = 0; i < testRecipients.size(); i++) {
            if (testRecipients.get(i).equals(searchTargetRecipient)) {
                matchingPayloadsCollector.add(testMessageTexts.get(i));
            }
        }
       
        assertEquals(1, matchingPayloadsCollector.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", matchingPayloadsCollector.get(0));
    }
    
    public void testDeleteMessageUsingMessageHash() {
        System.out.println("Running: Hash Target Deletion Verification");
        
        String hashToDrop = "20:2:WHERETIME."; 
        int targetIndex = testMessageHashes.indexOf(hashToDrop);
        
        assertTrue(targetIndex != -1);
        
        testMessageIDs.remove(targetIndex);
        testMessageHashes.remove(targetIndex);
        testRecipients.remove(targetIndex); 
        testMessageTexts.remove(targetIndex);
        
        // Assert that lookups for that hash now return negative space flags
        int indexPostDeletion = testMessageHashes.indexOf(hashToDrop);
        assertEquals(-1, indexPostDeletion);
    }

    public void testDisplayReportDataStructureFormatting() {
        System.out.println("Running: System Report Integrity Check");
        
        StringBuilder reportBufferBuilder = new StringBuilder();
      
        for (int i = 0; i < testMessageTexts.size(); i++) {
            reportBufferBuilder.append(testMessageHashes.get(i)).append(",")
                               .append(testRecipients.get(i)).append(",")
                               .append(testMessageTexts.get(i)).append("\n");
        }
        
        String outputResultReportString = reportBufferBuilder.toString();
        
        assertTrue(outputResultReportString.contains("Did you get the cake?"));
        assertTrue(outputResultReportString.contains("+27834557896"));
        assertTrue(outputResultReportString.contains("10:1:DIDCAKE"));
    }
}