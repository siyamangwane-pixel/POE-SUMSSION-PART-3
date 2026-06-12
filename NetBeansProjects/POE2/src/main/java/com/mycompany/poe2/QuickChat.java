package com.mycompany.poe2;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**
 *
 * @author mangw
 */
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class QuickChat {

    static Scanner input = new Scanner(System.in);
    static int messagesSent = 0;

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       WELCOME TO QUICKCHAT");
        System.out.println("================================");

        int choice;

        do {

            System.out.println("\n1. Send Message");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");

            System.out.print("Choose an option: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1 -> sendMessage();

                case 2 -> System.out.println("Coming Soon.");

                case 3 -> System.out.println("Exiting QuickChat...");

                default -> System.out.println("Invalid option.");
            }

        } while (choice != 3);
    }

    // SEND MESSAGE METHOD
    public static void sendMessage() {

        // Generate random 10-digit Message ID
        Random random = new Random();

        long messageID = 1000000000L +
                (long) (random.nextDouble() * 9000000000L);

        // Recipient Number
        System.out.print("\nEnter recipient number (+27...): ");
        String recipient = input.nextLine();

        // Validate recipient
        if (!recipient.startsWith("+") || recipient.length() > 13) {
            System.out.println("Invalid recipient number.");
            return;
        }

        // Message
        System.out.print("Enter message: ");
        String message = input.nextLine();

        // Validate message length
        if (message.length() > 250) {

            System.out.println(
                    "Please enter a message of less than 250 characters."
            );

            return;

        } else {

            System.out.println("Message captured.");
        }

        // Count messages sent
        messagesSent++;

        // Create Message Hash
        String[] words = message.split(" ");

        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();

        String messageIDString = String.valueOf(messageID);

        String firstTwoDigits = messageIDString.substring(0, 2);

        String messageHash = firstTwoDigits + ":" +
                messagesSent + ":" +
                firstWord + lastWord;

        // Display Message Details
        System.out.println("\nMESSAGE DETAILS");
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);

        // Message Options
        System.out.println("\nChoose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to Send Later");

        System.out.print("Option: ");
        int option = input.nextInt();
        input.nextLine();

        switch (option) {

            case 1 -> System.out.println("Message successfully sent.");

            case 2 -> {

                System.out.println("Press 0 to delete the message.");
                int delete = input.nextInt();
                input.nextLine();

                if (delete == 0) {

                    System.out.println("Message deleted.");
                }
            }

            case 3 -> {

                storeMessage(messageID, messageHash, recipient, message);
                System.out.println("Message successfully stored.");
            }

            default -> System.out.println("Invalid option.");
        }
    }

    // STORE MESSAGE METHOD
    public static void storeMessage(long messageID,
                                    String messageHash,
                                    String recipient,
                                    String message) {

        try {

            FileWriter writer = new FileWriter("storedMessage.json", true);

            writer.write("{\n");

            writer.write("\"MessageID\":\"" + messageID + "\",\n");

            writer.write("\"MessageHash\":\"" + messageHash + "\",\n");

            writer.write("\"Recipient\":\"" + recipient + "\",\n");

            writer.write("\"Message\":\"" + message + "\"\n");

            writer.write("}\n");

            writer.close();

        } catch (IOException e) {

            System.out.println("Error storing message.");
        }
    }
}