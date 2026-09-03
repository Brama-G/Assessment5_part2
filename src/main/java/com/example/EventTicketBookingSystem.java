package com.example;
import java.util.*;

/**
 * Event Ticket Booking System
 * Calculates total cost based on ticket category and quantity
 * Applies discounts for bulk purchases
 */
class TicketCategory {
    private String categoryName;
    private double pricePerTicket;
    private String description;
    
    public TicketCategory(String categoryName, double pricePerTicket, String description) {
        this.categoryName = categoryName;
        this.pricePerTicket = pricePerTicket;
        this.description = description;
    }
    
    public String getCategoryName() { return categoryName; }
    public double getPricePerTicket() { return pricePerTicket; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        return String.format("%-10s | ₹%-8.2f | %s", 
                            categoryName, pricePerTicket, description);
    }
}

class Booking {
    private String bookingId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private TicketCategory category;
    private int numberOfTickets;
    private double subtotal;
    private double discount;
    private double totalAmount;
    private Date bookingDate;
    
    public Booking(String bookingId, String customerName, String customerEmail, 
                   String customerPhone, TicketCategory category, int numberOfTickets) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.category = category;
        this.numberOfTickets = numberOfTickets;
        this.bookingDate = new Date();
        calculateCost();
    }
    
    private void calculateCost() {
        // Calculate subtotal
        this.subtotal = numberOfTickets * category.getPricePerTicket();
        
        // Apply discount logic
        this.discount = calculateDiscount();
        
        // Calculate total after discount
        this.totalAmount = subtotal - discount;
    }
    
    private double calculateDiscount() {
        double discountAmount = 0.0;
        
        // Discount tiers based on number of tickets
        if (numberOfTickets >= 50) {
            // 20% discount for 50+ tickets
            discountAmount = subtotal * 0.20;
        } else if (numberOfTickets >= 25) {
            // 15% discount for 25-49 tickets
            discountAmount = subtotal * 0.15;
        } else if (numberOfTickets >= 10) {
            // 10% discount for 10-24 tickets
            discountAmount = subtotal * 0.10;
        } else if (numberOfTickets >= 5) {
            // 5% discount for 5-9 tickets
            discountAmount = subtotal * 0.05;
        }
        
        // Additional VIP category discount for bulk bookings
        if (category.getCategoryName().equalsIgnoreCase("VIP") && numberOfTickets >= 20) {
            // Extra 5% for VIP bulk bookings
            discountAmount += subtotal * 0.05;
        }
        
        return discountAmount;
    }
    
    // Getters
    public String getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public TicketCategory getCategory() { return category; }
    public int getNumberOfTickets() { return numberOfTickets; }
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getTotalAmount() { return totalAmount; }
    public Date getBookingDate() { return bookingDate; }
    
    public String getBookingDetails() {
        StringBuilder details = new StringBuilder();
        details.append("\n========== BOOKING DETAILS ==========\n");
        details.append("Booking ID: ").append(bookingId).append("\n");
        details.append("Booking Date: ").append(bookingDate).append("\n");
        details.append("-----------------------------------\n");
        details.append("Customer Name: ").append(customerName).append("\n");
        details.append("Customer Email: ").append(customerEmail).append("\n");
        details.append("Customer Phone: ").append(customerPhone).append("\n");
        details.append("-----------------------------------\n");
        details.append("Ticket Category: ").append(category.getCategoryName()).append("\n");
        details.append("Price Per Ticket: ₹").append(String.format("%.2f", category.getPricePerTicket())).append("\n");
        details.append("Number of Tickets: ").append(numberOfTickets).append("\n");
        details.append("-----------------------------------\n");
        details.append("Subtotal: ₹").append(String.format("%.2f", subtotal)).append("\n");
        
        if (discount > 0) {
            details.append("Discount Applied: -₹").append(String.format("%.2f", discount)).append("\n");
            double discountPercentage = (discount / subtotal) * 100;
            details.append("Discount Percentage: ").append(String.format("%.1f", discountPercentage)).append("%\n");
        } else {
            details.append("Discount Applied: ₹0.00 (No discount)\n");
        }
        
        details.append("-----------------------------------\n");
        details.append("TOTAL AMOUNT: ₹").append(String.format("%.2f", totalAmount)).append("\n");
        details.append("===================================\n");
        
        return details.toString();
    }
}

public class EventTicketBookingSystem {
    private List<Booking> bookings;
    private Map<String, TicketCategory> categories;
    private Scanner scanner;
    private int bookingCounter;
    
    public EventTicketBookingSystem() {
        bookings = new ArrayList<>();
        categories = new HashMap<>();
        scanner = new Scanner(System.in);
        bookingCounter = 1000;
        initializeCategories();
    }
    
    private void initializeCategories() {
        // Regular: ₹500 per ticket
        TicketCategory regular = new TicketCategory("Regular", 500.00, "Standard seating");
        // Premium: ₹800 per ticket
        TicketCategory premium = new TicketCategory("Premium", 800.00, "Better view, comfortable seating");
        // VIP: ₹1500 per ticket
        TicketCategory vip = new TicketCategory("VIP", 1500.00, "Best view, VIP lounge access, complimentary drinks");
        // Student: ₹300 per ticket (special category)
        TicketCategory student = new TicketCategory("Student", 300.00, "Student discount with valid ID");
        // Group: ₹450 per ticket (for groups of 10+)
        TicketCategory group = new TicketCategory("Group", 450.00, "Group booking rate (min 10 tickets)");
        
        categories.put("regular", regular);
        categories.put("premium", premium);
        categories.put("vip", vip);
        categories.put("student", student);
        categories.put("group", group);
    }
    
    public void displayCategories() {
        System.out.println("\n===== AVAILABLE TICKET CATEGORIES =====");
        System.out.printf("%-10s | %-10s | %-30s\n", "Category", "Price", "Description");
        System.out.println("------------------------------------------------------------");
        for (TicketCategory category : categories.values()) {
            System.out.printf("%-10s | ₹%-9.2f | %-30s\n", 
                            category.getCategoryName(), 
                            category.getPricePerTicket(),
                            category.getDescription());
        }
        System.out.println("============================================================");
        System.out.println("\nDiscount Tiers:");
        System.out.println("  • 5-9 tickets: 5% discount");
        System.out.println("  • 10-24 tickets: 10% discount");
        System.out.println("  • 25-49 tickets: 15% discount");
        System.out.println("  • 50+ tickets: 20% discount");
        System.out.println("  • VIP category + 20+ tickets: Additional 5% discount");
        System.out.println("============================================================");
    }
    
    public void displayDiscountTiers() {
        System.out.println("\n===== DISCOUNT INFORMATION =====");
        System.out.println("Number of Tickets | Discount Applied");
        System.out.println("------------------|-----------------");
        System.out.println("1-4               | 0%");
        System.out.println("5-9               | 5%");
        System.out.println("10-24             | 10%");
        System.out.println("25-49             | 15%");
        System.out.println("50+               | 20%");
        System.out.println("VIP + 20+ tickets | Additional 5%");
        System.out.println("=================================");
    }
    
    private TicketCategory selectCategory() {
        System.out.print("\nEnter ticket category (Regular/Premium/VIP/Student/Group): ");
        String categoryName = scanner.nextLine().trim().toLowerCase();
        TicketCategory category = categories.get(categoryName);
        
        if (category == null) {
            System.out.println("Invalid category. Using Regular as default.");
            category = categories.get("regular");
        }
        return category;
    }
    
    private int getNumberOfTickets() {
        while (true) {
            System.out.print("Enter number of tickets: ");
            try {
                int tickets = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (tickets > 0) {
                    return tickets;
                } else {
                    System.out.println("Number of tickets must be greater than 0. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }
    
    public void createBooking() {
        System.out.println("\n========== NEW BOOKING ==========");
        
        // Generate booking ID
        String bookingId = "BK" + (++bookingCounter);
        
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter Customer Phone: ");
        String phone = scanner.nextLine().trim();
        
        // Display categories
        displayCategories();
        
        // Select category
        TicketCategory category = selectCategory();
        
        // Get number of tickets
        int numberOfTickets = getNumberOfTickets();
        
        // Create booking
        Booking booking = new Booking(bookingId, name, email, phone, category, numberOfTickets);
        bookings.add(booking);
        
        // Display booking details
        System.out.println("\n✓ Booking confirmed!");
        System.out.println(booking.getBookingDetails());
    }
    
    public void createMultipleBookings() {
        System.out.print("\nHow many bookings do you want to create? ");
        try {
            int count = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            for (int i = 0; i < count; i++) {
                System.out.println("\n--- Booking " + (i + 1) + " of " + count + " ---");
                createBooking();
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Returning to main menu.");
            scanner.nextLine();
        }
    }
    
    public void viewBooking() {
        System.out.print("\nEnter Booking ID (e.g., BK1001): ");
        String bookingId = scanner.nextLine().trim();
        
        Booking found = null;
        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                found = booking;
                break;
            }
        }
        
        if (found != null) {
            System.out.println(found.getBookingDetails());
        } else {
            System.out.println("Booking not found!");
        }
    }
    
    public void viewAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
            return;
        }
        
        System.out.println("\n========== ALL BOOKINGS ==========");
        System.out.printf("%-10s | %-20s | %-12s | %-10s | %-12s\n", 
                         "Booking ID", "Customer", "Category", "Tickets", "Total");
        System.out.println("------------------------------------------------------------");
        
        double totalRevenue = 0;
        for (Booking booking : bookings) {
            totalRevenue += booking.getTotalAmount();
            System.out.printf("%-10s | %-20s | %-12s | %-10d | ₹%-11.2f\n",
                             booking.getBookingId(),
                             booking.getCustomerName().length() > 20 ? 
                             booking.getCustomerName().substring(0, 17) + "..." : booking.getCustomerName(),
                             booking.getCategory().getCategoryName(),
                             booking.getNumberOfTickets(),
                             booking.getTotalAmount());
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("Total Revenue: ₹%.2f\n", totalRevenue);
        System.out.println("============================================================");
    }
    
    public void generateSummaryReport() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings to report.");
            return;
        }
        
        System.out.println("\n========== BOOKING SUMMARY REPORT ==========");
        System.out.println("Total Bookings: " + bookings.size());
        
        // Summary by category
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Double> categoryRevenue = new HashMap<>();
        
        for (Booking booking : bookings) {
            String category = booking.getCategory().getCategoryName();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            categoryRevenue.put(category, 
                categoryRevenue.getOrDefault(category, 0.0) + booking.getTotalAmount());
        }
        
        System.out.println("\nBookings by Category:");
        System.out.printf("%-12s | %-10s | %-15s\n", "Category", "Count", "Revenue");
        System.out.println("----------------------------------------");
        for (String category : categoryCount.keySet()) {
            System.out.printf("%-12s | %-10d | ₹%-14.2f\n", 
                             category, 
                             categoryCount.get(category),
                             categoryRevenue.get(category));
        }
        
        // Total revenue
        double totalRevenue = 0;
        int totalTickets = 0;
        for (Booking booking : bookings) {
            totalRevenue += booking.getTotalAmount();
            totalTickets += booking.getNumberOfTickets();
        }
        
        System.out.println("----------------------------------------");
        System.out.printf("Total Tickets Sold: %d\n", totalTickets);
        System.out.printf("Total Revenue: ₹%.2f\n", totalRevenue);
        System.out.printf("Average Ticket Price: ₹%.2f\n", 
                         totalTickets > 0 ? totalRevenue / totalTickets : 0);
        System.out.println("============================================");
    }
    
    public void displayMenu() {
        System.out.println("\n===== EVENT TICKET BOOKING SYSTEM =====");
        System.out.println("1. Create New Booking");
        System.out.println("2. Create Multiple Bookings");
        System.out.println("3. View Booking by ID");
        System.out.println("4. View All Bookings");
        System.out.println("5. View Available Categories");
        System.out.println("6. View Discount Information");
        System.out.println("7. Generate Summary Report");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }
    
    public void run() {
        System.out.println("================================================");
        System.out.println("  WELCOME TO EVENT TICKET BOOKING SYSTEM");
        System.out.println("================================================");
        
        while (true) {
            displayMenu();
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }
            
            switch (choice) {
                case 1:
                    createBooking();
                    break;
                    
                case 2:
                    createMultipleBookings();
                    break;
                    
                case 3:
                    viewBooking();
                    break;
                    
                case 4:
                    viewAllBookings();
                    break;
                    
                case 5:
                    displayCategories();
                    break;
                    
                case 6:
                    displayDiscountTiers();
                    break;
                    
                case 7:
                    generateSummaryReport();
                    break;
                    
                case 8:
                    System.out.println("\nThank you for using the Event Ticket Booking System!");
                    System.out.println("Have a great day!");
                    return;
                    
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    public static void main(String[] args) {
        EventTicketBookingSystem system = new EventTicketBookingSystem();
        system.run();
    }
}
