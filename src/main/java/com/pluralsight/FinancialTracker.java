package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Splash screen
        System.out.println("*".repeat(60));
        System.out.println("*" + " ".repeat(58) + "*");
        System.out.println("*" + " ".repeat(18) + "  AKIBA LEDGER  " + " ".repeat(24) + "*");
        System.out.println("*" + " ".repeat(10) + "Your Anime Merch Finance Tracker" + " ".repeat(16) + "*");
        System.out.println("*" + " ".repeat(58) + "*");
        System.out.println("*".repeat(60));
        System.out.println();
        System.out.println("  Tracking sales of: Figures | Manga | Apparel | Imports");
        System.out.println();

        while (running) {
            System.out.println("=".repeat(60));
            System.out.println("            AKIBA LEDGER — MAIN MENU");
            System.out.println("=".repeat(60));
            System.out.println("  D) Add Deposit   (Sale / Income)");
            System.out.println("  P) Make Payment  (Restock / Expense)");
            System.out.println("  L) Ledger");
            System.out.println("  X) Exit");
            System.out.println("=".repeat(60));
            System.out.print("  Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     */
    public static void loadTransactions(String fileName) {
        // create file if it doesnt exist
       try{
           File file = new File(fileName);
        if (!file.exists()){
            System.out.println("file doesnt exist.... will create a new one");
            BufferedWriter bf = new BufferedWriter( new FileWriter(fileName));




        }
        // read file once transactions have been made
           // add them to the arraylist transaction object

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) != null){
                if (line.trim().isEmpty()) continue;
                String [] parts = line.split("\\|");
                String d = parts[0];
                LocalDate date = LocalDate.parse(d, DATE_FMT);
                String t = parts[1];
                LocalTime time = LocalTime.parse(t,TIME_FMT);
                String description = parts[2];
                String vendor = parts[3];
                double amount = parseDouble(parts[4]);
                transactions.add(new Transaction(date,time,description,vendor,amount));


        }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.
    }

    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     * Prompt for ONE date+time string in the format
     * "yyyy-MM-dd HH:mm:ss", plus description, vendor, amount.
     * Validate that the amount entered is positive.
     * Store the amount as-is (positive) and append to the file.
     */
    private static void addDeposit(Scanner scanner) {

        // Prompt for date and time as a single input
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
        String dateTime = scanner.nextLine().trim();

        // Split the single datetime string into date and time
        String[] dateTimeParts = dateTime.split(" ");
        if (dateTimeParts.length != 2) {
            System.out.println("Invalid date+time format");
            return;
        }
        String date = dateTimeParts[0];
        String time = dateTimeParts[1];

        // Prompt for remaining transaction fields
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        String amountStr = scanner.nextLine().trim();

        // make sure there's no negative deposit
        if (amountStr.contains("-")) {
            System.out.println("Can't have a negative deposit");
            return;
        }

        // Validate amount is actually a number
        double amount;
        try {
            amount = parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered");
            return;
        }

        // Build the pipe-delimited record and append to file
        String result = date + "|" + time + "|" + description + "|" + vendor + "|" + (amount);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(result);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to the log file: " + e.getMessage());
        }
    }

    /**
     * Same prompts as addDeposit.
     * Amount must be entered as a positive number,
     * then converted to a negative amount before storing.
     */
    private static void addPayment(Scanner scanner) {

        // Prompt for date and time as a single input
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
        String dateTime = scanner.nextLine().trim();

        // Split the single datetime string into date and time
        String[] dateTimeParts = dateTime.split(" ");
        if (dateTimeParts.length != 2) {
            System.out.println("Invalid date+time format");
            return;
        }
        String date = dateTimeParts[0];
        String time = dateTimeParts[1];

        // Prompt for remaining transaction fields
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        String amountStr = scanner.nextLine().trim();

        // User must enter a positive number — the app applies the negative sign
        if (amountStr.contains("-")) {
            System.out.println("Please enter a positive amount, it will be stored as negative");
            return;
        }

        // Validate amount is actually a number
        double amount;
        try {
            amount = parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered");
            return;
        }

        // Build the pipe-delimited record, negating the amount to mark it as a payment
        String result = date + "|" + time + "|" + description + "|" + vendor + "|" + (-amount);

        // Append the payment record to the transactions file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(result);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to the log file: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() {
        System.out.println();
        System.out.println("=".repeat(100));
        System.out.println("                            AKIBA LEDGER — ALL TRANSACTIONS  ");
        System.out.println("=".repeat(100));
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Item / Description", "Vendor / Supplier", "Amount ($)");
        System.out.println("-".repeat(100));
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println("=".repeat(100));
        System.out.println();
    }

    private static void displayDeposits() {
        // Print header row and separator
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(100));

        // Only print transactions with a positive amount (deposits)
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                System.out.println(t);
            }
        }
    }

    private static void displayPayments() {
        // Print header row and separator
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(100));

        // Only print transactions with a negative amount (payments)
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                System.out.println(t);
            }
        }
    }

    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                // Month to date — from the first day of this month to today
                case "1" -> filterTransactionsByDate(
                        LocalDate.now().withDayOfMonth(1),
                        LocalDate.now()
                );

                // Previous month — from the first to the last day of last month
                case "2" -> filterTransactionsByDate(
                        LocalDate.now().minusMonths(1).withDayOfMonth(1),
                        LocalDate.now().minusMonths(1).withDayOfMonth(
                                LocalDate.now().minusMonths(1).lengthOfMonth())
                );

                // Year to date — from the first day of this year to today
                case "3" -> filterTransactionsByDate(
                        LocalDate.now().withDayOfYear(1),
                        LocalDate.now()
                );

                // Previous year — from the first to the last day of last year
                case "4" -> filterTransactionsByDate(
                        LocalDate.now().minusYears(1).withDayOfYear(1),
                        LocalDate.now().minusYears(1).withDayOfYear(
                                LocalDate.now().minusYears(1).lengthOfYear())
                );

                // Search by vendor — prompt for vendor name then filter
                case "5" -> {
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                }

                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default  -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // Print header row and separator
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(100));

        // Print transactions that fall within the given date range (inclusive)
        boolean found = false;
        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(start) && !t.getDate().isAfter(end)) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found in that date range");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // Print header row and separator
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(100));

        // Print transactions that match the given vendor name (case-insensitive)
        boolean found = false;
        for (Transaction t : transactions) {
            if (vendor.equalsIgnoreCase(t.getVendor())) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found for vendor: " + vendor);
        }
    }

    private static void customSearch(Scanner scanner) {
        // Prompt for each optional filter — blank input skips that filter
        System.out.print("Start date (yyyy-MM-dd) or blank to skip: ");
        LocalDate start = parseDate(scanner.nextLine());

        System.out.print("End date (yyyy-MM-dd) or blank to skip: ");
        LocalDate end = parseDate(scanner.nextLine());

        System.out.print("Description or blank to skip: ");
        String description = scanner.nextLine().trim();

        System.out.print("Vendor or blank to skip: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Amount or blank to skip: ");
        Double amount = parseDouble(scanner.nextLine());

        // Print header row and separator
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(100));

        // Apply each filter only if the user provided a value — skip null/blank fields
        boolean found = false;
        for (Transaction t : transactions) {
            if (start != null && t.getDate().isBefore(start)) continue;
            if (end != null && t.getDate().isAfter(end)) continue;
            if (!description.isEmpty() && !t.getDescription().toLowerCase().contains(description.toLowerCase())) continue;
            if (!vendor.isEmpty() && !t.getVendor().toLowerCase().contains(vendor.toLowerCase())) continue;
            if (amount != null && t.getAmount() != amount) continue;

            System.out.println(t);
            found = true;
        }

        if (!found) {
            System.out.println("No transactions found matching your search");
        }
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    // Parses a date string in yyyy-MM-dd format — returns null if blank or invalid
    private static LocalDate parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(s.trim(), DATE_FMT);
        } catch (Exception e) {
            return null;
        }
    }

    // Parses a double from a string — returns null if blank or invalid
    private static Double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return null;
        }
    }
}
