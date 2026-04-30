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

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

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
            fileName = "new_transaction.csv";
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
                double amount = Double.parseDouble(parts[4]);
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

        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        String amountStr = scanner.nextLine().trim();

        if (amountStr.contains("-")) {
            System.out.println("Can't have a negative deposit");
            return;
        }

        // Validate amount is actually a number
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered");
            return;
        }

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
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
        String dateTime = scanner.nextLine().trim();

        String[] dateTimeParts = dateTime.split(" ");
        if (dateTimeParts.length != 2) {
            System.out.println("Invalid date+time format");
            return;
        }
        String date = dateTimeParts[0];
        String time = dateTimeParts[1];

        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        String amountStr = scanner.nextLine().trim();

        if (amountStr.contains("-")) {
            System.out.println("Please enter a positive amount, it will be stored as negative");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered");
            return;
        }

        String result = date + "|" + time + "|" + description + "|" + vendor + "|" + (-amount);

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
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction t : transactions) {
            System.out.println(t);
        }



    }

    private static void displayDeposits() {
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction t : transactions) {
            if( t.getAmount() > 0){
                System.out.println(t);
            }

        }
    }

    private static void displayPayments() { System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
            "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction t : transactions) {
            if( t.getAmount() < 0){
                System.out.println(t);
            }

        } }

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
                case "1" -> {filterTransactionsByDate(LocalDate.now().withDayOfMonth(1), LocalDate.now());
                }
                case "2" -> {filterTransactionsByDate( LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalDate.now().minusMonths(1).withDayOfMonth(    // last day of last month
                        LocalDate.now().minusMonths(1).lengthOfMonth())); }
                case "3" -> {filterTransactionsByDate( LocalDate.now().withDayOfYear(1), LocalDate.now()); }
                case "4" -> {
                    filterTransactionsByDate(
                        LocalDate.now().minusYears(1).withDayOfYear(1),                          // first day of last year
                        LocalDate.now().minusYears(1).withDayOfYear(                             // last day of last year
                                LocalDate.now().minusYears(1).lengthOfYear())
                );
                }
                case "5" -> {  System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor); }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(start) && !t.getDate().isAfter(end)) {
                System.out.println(t);
            }
        }

    }

    private static void filterTransactionsByVendor(String vendor) {
        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction t : transactions) {
            if (vendor.equalsIgnoreCase(t.getVendor())) {
                System.out.println(t);
            }
        }

    }

    private static void customSearch(Scanner scanner) {
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

        System.out.printf("%-12s | %-10s | %-35s | %-25s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(100));

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
    private static LocalDate parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(s.trim(), DATE_FMT);
        } catch (Exception e) {
            return null;
        }
    }

    private static Double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return null;
        }
    }
}
