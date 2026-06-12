/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poe2;

/**
 *
 * @author mangwane
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Registration {

    // Global lists to track current session active messaging logs
    private static final ArrayList<String> recipients = new ArrayList<>();
    private static final ArrayList<String> messages = new ArrayList<>();
    private static final ArrayList<String> hashes = new ArrayList<>();
    private static int sentMessages = 0;

    // --- TASK arrays (Dynamically filled, NO values are hard-coded) ---
    private static final ArrayList<String> userSentList = new ArrayList<>();
    private static final ArrayList<String> userSkippedList = new ArrayList<>();
    
    // Parallel arrays for tracking the JSON text file data
    private static final ArrayList<Long> savedIds = new ArrayList<>();
    private static final ArrayList<String> savedHashes = new ArrayList<>();
    private static final ArrayList<String> savedPhones = new ArrayList<>();
    private static final ArrayList<String> savedTexts = new ArrayList<>();

    // Physical text document name on the drive
    private static final String FILE_NAME = "stored_messages.json";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login auth = new Login();

        getJsonFileData();

        System.out.println("<<<<Registration>>>>>");

        System.out.print("First Name: ");
        String fn = input.nextLine();

        System.out.print("Last Name: ");
        String ln = input.nextLine();

        // Username loop
        String username;
        while (true) {
            System.out.print("Username: ");
            username = input.nextLine();
            if (auth.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted. "
                        + "It must contain an underscore and be no more than 5 characters long.");
            }
        }

        // Password loop
        String password;
        while (true) {
            System.out.print("Password: ");
            password = input.nextLine();
            if (auth.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted. "
                        + "It must be at least 8 characters long and include a capital letter, "
                        + "a number, and a special character.");
            }
        }

        // Phone loop
        String phone;
        while (true) {
            System.out.print("Phone (+27): ");
            phone = input.nextLine();
            if (auth.checkCellphoneNumber(phone)) {
                System.out.println("Cell phone number successfully captured.");
                break;
            } else {
                System.out.println("Cell phone number incorrectly formatted. "
                        + "It must start with +27 followed by 9 digits (e.g., +27831234567).");
            }
        }

        System.out.println(auth.registerUser(username, password, phone, fn, ln));

        // Login Check
        System.out.println("\n--- Login ---");
        boolean success = false;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (!success && attempts < MAX_ATTEMPTS) {
            System.out.print("Username: ");
            String lUser = input.nextLine();

            System.out.print("Password: ");
            String lPass = input.nextLine();

            success = auth.loginUser(lUser, lPass);
            System.out.println(auth.returnLoginStatus(success));
            attempts++;

            if (!success && attempts < MAX_ATTEMPTS) {
                System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
            }
        }

        // Main App Logic Loop
        if (success) {
            Random random = new Random();
            int option;

            do {
                System.out.println("\n===== QUICKCHAT MENU =====");
                System.out.println("1. Send Messages");
                System.out.println("2. Show Recently Sent Messages");
                System.out.println("3. Stored Messages Toolset");
                System.out.println("4. Quit");

                System.out.print("Choose an option: ");
                option = input.nextInt();
                input.nextLine(); 

                switch (option) {
                    case 1 -> {
                        System.out.print("\nHow many messages would you like to send? ");
                        int totalMessages = input.nextInt();
                        input.nextLine();

                        if (sentMessages >= totalMessages) {
                            System.out.println("Message limit reached.");
                            break;
                        }

                        while (sentMessages < totalMessages) {
                            System.out.print("\nEnter recipient number (+27xxxxxxxxx): ");
                            String recipient = input.nextLine();

                            String validationResult = checkRecipientCell(recipient);
                            if (!validationResult.equals("Valid")) {
                                System.out.println(validationResult);
                                continue;
                            }

                            System.out.print("Enter your message: ");
                            String message = input.nextLine();

                            if (message.length() > 250) {
                                System.out.println("Please enter a message of less than 250 characters.");
                                continue;
                            } else {
                                System.out.println("Message captured.");
                            }

                            // Math logic to generate random unique message ID dynamically
                            long messageID;
                            do {
                                messageID = 1000000000L + (long) (random.nextDouble() * 9000000000L);
                            } while (!checkMessageID(messageID));

                            int messageNumber = returnTotalMessagess() + 1;
                            String hash = createMessageHash(messageID, messageNumber, message);

                            System.out.println("\n===== MESSAGE DETAILS =====");
                            System.out.println("Message ID: " + messageID);
                            System.out.println("Message Hash: " + hash);
                            System.out.println("Recipient: " + recipient);
                            System.out.println("Message: " + message);

                            String actionResult = SentMessage();

                            switch (actionResult) {
                                case "Send" -> {
                                    recipients.add(recipient);
                                    messages.add(message);
                                    hashes.add(hash);
                                    sentMessages++;
                                    userSentList.add(message);
                                }
                                case "Store" -> {
                                    recipients.add(recipient);
                                    messages.add(message);
                                    hashes.add(hash);
                                    sentMessages++;
                                    
                                    addMessageToDisk(messageID, hash, recipient, message);
                                    
                                    // Refresh memory arrays from the file
                                    getJsonFileData();
                                }
                                case "Disregard" -> {
                                    userSkippedList.add(message); // Dynamic population
                                }
                            }

                            if (sentMessages >= totalMessages) {
                                System.out.println("\nYou have processed your total set target allocation.");
                                break;
                            }

                            System.out.print("\nDo you want to process another message sequence slot? (yes/no): ");
                            String answer = input.nextLine();
                            if (!answer.equalsIgnoreCase("yes")) {
                                break;
                            }
                        }
                    }

                    case 2 -> System.out.print(printMessages());
                    case 3 -> handleSubMenuOptions(input);
                    case 4 -> {
                        System.out.println("Thank you for using QuickChat.");
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } while (option != 4);
        } else {
            System.out.println("Too many failed login attempts. Access denied.");
        }
        input.close();
    }
    
    private static void getJsonFileData() {
        savedIds.clear();
        savedHashes.clear();
        savedPhones.clear();
        savedTexts.clear();

        if (!Files.exists(Paths.get(FILE_NAME))) {
            return; 
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String row;
            long currentId = 0;
            String currentHash = "";
            String currentPhone = "";
            String currentText = "";

            while ((row = br.readLine()) != null) {
                row = row.trim();

                if (row.contains("\"messageID\"")) {
                    currentId = Long.parseLong(cleanJsonRow(row));
                } else if (row.contains("\"messageHash\"")) {
                    currentHash = cleanJsonRow(row);
                } else if (row.contains("\"recipient\"")) {
                    currentPhone = cleanJsonRow(row);
                } else if (row.contains("\"messageText\"")) {
                    currentText = cleanJsonRow(row);
                    
                    savedIds.add(currentId);
                    savedHashes.add(currentHash);
                    savedPhones.add(currentPhone);
                    savedTexts.add(currentText);
                }
            }
        } catch (IOException | NumberFormatException err) {
            System.out.println("Error reading storage file data: " + err.getMessage());
        }
    }

    private static String cleanJsonRow(String rowData) {
        int splitPos = rowData.indexOf(":");
        if (splitPos == -1) return "";
        
        String cleanValue = rowData.substring(splitPos + 1).replace(",", "").trim();
        if (cleanValue.startsWith("\"") && cleanValue.endsWith("\"")) {
            cleanValue = cleanValue.substring(1, cleanValue.length() - 1);
        }
        return cleanValue;
    }

    private static void addMessageToDisk(long id, String hash, String phone, String txt) {
        try {
            boolean fileExists = Files.exists(Paths.get(FILE_NAME));
            String rawJsonBlock = "{\n" +
                    "  \"messageID\": " + id + ",\n" +
                    "  \"messageHash\": \"" + hash + "\",\n" +
                    "  \"recipient\": \"" + phone + "\",\n" +
                    "  \"messageText\": \"" + txt + "\"\n" +
                    "}\n";

            Files.write(Paths.get(FILE_NAME), rawJsonBlock.getBytes(), 
                fileExists ? java.nio.file.StandardOpenOption.APPEND : java.nio.file.StandardOpenOption.CREATE);
            System.out.println("Record appended securely to disk database file.");
        } catch (IOException err) {
            System.out.println("Error saving message item down to drive file.");
        }
    }

    private static void updateDiskDatabase() {
        try {
            Files.deleteIfExists(Paths.get(FILE_NAME));
            for (int i = 0; i < savedTexts.size(); i++) {
                addMessageToDisk(savedIds.get(i), savedHashes.get(i), savedPhones.get(i), savedTexts.get(i));
            }
        } catch (IOException err) {
            System.out.println("Error rewriting disk files to keep arrays uniform: " + err.getMessage());
        }
    }

    private static void handleSubMenuOptions(Scanner input) {
        System.out.println("\n--- STORED MESSAGES SUB-MANAGEMENT ---");
        System.out.println("a. Display sender and recipient of all stored messages");
        System.out.println("b. Display the longest stored message");
        System.out.println("c. Search for a message ID and display tracking info");
        System.out.println("d. Search for all messages stored for a particular recipient");
        System.out.println("e. Delete a message using its tracking string hash signature");
        System.out.println("f. Display full unified detailed report of all stored entries");
        System.out.print("Select feature option letter (a-f): ");
        String choice = input.nextLine().trim().toLowerCase();

        switch (choice) {
            case "a" -> {
                System.out.println("\n--- Sender & Recipient Pairs ---");
                if (savedTexts.isEmpty()) {
                    System.out.println("No stored database records found.");
                    break;
                }
                for (int i = 0; i < savedTexts.size(); i++) {
                    System.out.println("Index [" + i + "] Sender: System User -> Destination: " + savedPhones.get(i));
                }
            }
            case "b" -> {
                System.out.println("\n--- Longest Stored Message Check ---");
                if (savedTexts.isEmpty()) {
                    System.out.println("No elements found in tracking memory.");
                    break;
                }
                String maxStr = savedTexts.get(0);
                for (String textItem : savedTexts) {
                    if (textItem.length() > maxStr.length()) {
                        maxStr = textItem;
                    }
                }
                System.out.println("Longest Text Payload: \"" + maxStr + "\" (" + maxStr.length() + " characters)");
            }
            case "c" -> {
                System.out.print("\nEnter Message ID number: ");
                try {
                    long targetId = Long.parseLong(input.nextLine().trim());
                    int indexLocation = savedIds.indexOf(targetId);
                    if (indexLocation != -1) {
                        System.out.println(" Match Found at Index Location " + indexLocation);
                        System.out.println("Recipient Phone: " + savedPhones.get(indexLocation));
                        System.out.println("Message Text: " + savedTexts.get(indexLocation));
                    } else {
                        System.out.println("No match located for that numerical ID token.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Numeric typing conversion format error.");
                }
            }
            case "d" -> {
                System.out.print("\nEnter destination phone string: ");
                String matchPhone = input.nextLine().trim();
                boolean matchesExist = false;
                System.out.println("\nMatching Data Results:");
                for (int i = 0; i < savedPhones.size(); i++) {
                    if (savedPhones.get(i).equalsIgnoreCase(matchPhone)) {
                        System.out.println("-> Hash [" + savedHashes.get(i) + "]: " + savedTexts.get(i));
                        matchesExist = true;
                    }
                }
                if (!matchesExist) {
                    System.out.println("No database items match that requested telephone filter.");
                }
            }
            case "e" -> {
                System.out.print("\nEnter tracking signature hash token to drop: ");
                String hashFilter = input.nextLine().trim().toUpperCase();
                int indexPosition = savedHashes.indexOf(hashFilter);
                if (indexPosition != -1) {
                    // Synchronously drop items across all 4 parallel arrays to maintain order
                    savedIds.remove(indexPosition);
                    savedHashes.remove(indexPosition);
                    savedPhones.remove(indexPosition);
                    savedTexts.remove(indexPosition);
                    
                    // Force text database file rewrite to preserve array structural changes
                    updateDiskDatabase();
                    System.out.println("Record dropped cleanly from parallel tracking systems and disk.");
                } else {
                    System.out.println("The provided message verification hash token was not located.");
                }
            }
            case "f" -> {
                System.out.println("\n================= FULL SYSTEM STORAGE DATA REPORT =================");
                if (savedTexts.isEmpty()) {
                    System.out.println("No records found.");
                    break;
                }
                for (int i = 0; i < savedTexts.size(); i++) {
                    System.out.println("Record Frame (" + (i + 1) + ")");
                    System.out.println("  ID  : " + savedIds.get(i));
                    System.out.println("  Hash: " + savedHashes.get(i));
                    System.out.println("  Cell: " + savedPhones.get(i));
                    System.out.println("  Body: " + savedTexts.get(i));
                    System.out.println("-------------------------------------------------------------------");
                }
            }
            default -> System.out.println("Incorrect character selection entered.");
        }
    }

    public static boolean checkMessageID(long messageID) {
        return String.valueOf(messageID).length() <= 10;
    }

    public static String checkRecipientCell(String cellNumber) {
        if (!cellNumber.matches("^\\+27\\d{9}$") && !cellNumber.matches("^\\d{10}$")) {
            return "Cell phone number is incorrectly formatted.";
        }
        return "Valid";
    }

    public static String createMessageHash(long messageID, int messageNumber, String message) {
        String idPrefix = String.valueOf(messageID).substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public static String SentMessage() {
        Scanner menuScanner = new Scanner(System.in);
        System.out.println("\nChoose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Your choice: ");
        int choice = menuScanner.nextInt();

        switch (choice) {
            case 1 -> {
                System.out.println("Message successfully sent");
                return "Send";
            }
            case 2 -> {
                System.out.println("Press 0 to delete the message");
                return "Disregard";
            }
            case 3 -> {
                System.out.println("Message successfully stored");
                return "Store";
            }
            default -> {
                return "Disregard";
            }
        }
    }

    public static String printMessages() {
        if (messages.isEmpty()) {
            return "\nNo messages processed yet.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== RECENTLY LOGGED MESSAGES =====");
        for (int i = 0; i < messages.size(); i++) {
            sb.append("\nMessage ").append(i + 1);
            sb.append("\nRecipient: ").append(recipients.get(i));
            sb.append("\nMessage: ").append(messages.get(i));
            sb.append("\nHash: ").append(hashes.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() {
        return sentMessages;
    }
}