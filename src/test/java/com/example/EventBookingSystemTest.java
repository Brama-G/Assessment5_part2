package com.example;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Map;

public class EventTicketBookingSystemTest {
    
    private EventTicketBookingSystem system;
    private Map<String, TicketCategory> categories;
    
    @Before
    public void setUp() {
        system = new EventTicketBookingSystem();
        try {
            Field field = EventTicketBookingSystem.class.getDeclaredField("categories");
            field.setAccessible(true);
            categories = (Map<String, TicketCategory>) field.get(system);
        } catch (Exception e) {
            fail("Could not access categories: " + e.getMessage());
        }
    }
    
    @Test
    public void testCategoryInitialization() {
        assertNotNull("Regular category should exist", categories.get("regular"));
        assertNotNull("Premium category should exist", categories.get("premium"));
        assertNotNull("VIP category should exist", categories.get("vip"));
        assertNotNull("Student category should exist", categories.get("student"));
        assertNotNull("Group category should exist", categories.get("group"));
        
        TicketCategory regular = categories.get("regular");
        assertEquals("Regular category name", "Regular", regular.getCategoryName());
        assertEquals("Regular price", 500.00, regular.getPricePerTicket(), 0.01);
        
        TicketCategory vip = categories.get("vip");
        assertEquals("VIP category name", "VIP", vip.getCategoryName());
        assertEquals("VIP price", 1500.00, vip.getPricePerTicket(), 0.01);
    }
    
    @Test
    public void testBookingCreation() {
        TicketCategory regular = categories.get("regular");
        Booking booking = new Booking("BK1001", "John Doe", "john@email.com", 
                                     "123-456-7890", regular, 5);
        
        assertEquals("Booking ID should match", "BK1001", booking.getBookingId());
        assertEquals("Customer name should match", "John Doe", booking.getCustomerName());
        assertEquals("Number of tickets should match", 5, booking.getNumberOfTickets());
        assertEquals("Category should match", regular, booking.getCategory());
    }
    
    @Test
    public void testDiscountCalculation() {
        TicketCategory regular = categories.get("regular");
        
        // Test 5 tickets (5% discount)
        Booking booking1 = new Booking("BK1002", "Alice", "alice@email.com", 
                                      "111-222-3333", regular, 5);
        assertEquals("Subtotal for 5 tickets", 2500.00, booking1.getSubtotal(), 0.01);
        assertEquals("Discount for 5 tickets (5%)", 125.00, booking1.getDiscount(), 0.01);
        assertEquals("Total for 5 tickets", 2375.00, booking1.getTotalAmount(), 0.01);
        
        // Test 15 tickets (10% discount)
        Booking booking2 = new Booking("BK1003", "Bob", "bob@email.com", 
                                      "444-555-6666", regular, 15);
        assertEquals("Subtotal for 15 tickets", 7500.00, booking2.getSubtotal(), 0.01);
        assertEquals("Discount for 15 tickets (10%)", 750.00, booking2.getDiscount(), 0.01);
        assertEquals("Total for 15 tickets", 6750.00, booking2.getTotalAmount(), 0.01);
        
        // Test 30 tickets (15% discount)
        Booking booking3 = new Booking("BK1004", "Charlie", "charlie@email.com", 
                                      "777-888-9999", regular, 30);
        assertEquals("Subtotal for 30 tickets", 15000.00, booking3.getSubtotal(), 0.01);
        assertEquals("Discount for 30 tickets (15%)", 2250.00, booking3.getDiscount(), 0.01);
        assertEquals("Total for 30 tickets", 12750.00, booking3.getTotalAmount(), 0.01);
    }
    
    @Test
    public void testVIPDiscount() {
        TicketCategory vip = categories.get("vip");
        
        // VIP + 25 tickets (15% + 5% extra = 20%)
        Booking booking = new Booking("BK1005", "Diana", "diana@email.com", 
                                     "111-111-1111", vip, 25);
        double subtotal = 25 * 1500.00; // 37500
        double expectedDiscount = subtotal * 0.20; // 20% total discount
        assertEquals("VIP 25 tickets discount should be 20%", 
                    expectedDiscount, booking.getDiscount(), 0.01);
        assertEquals("Total should reflect 20% discount", 
                    subtotal - expectedDiscount, booking.getTotalAmount(), 0.01);
        
        // VIP + 15 tickets (10% discount, no extra VIP discount)
        Booking booking2 = new Booking("BK1006", "Eve", "eve@email.com", 
                                      "222-222-2222", vip, 15);
        double subtotal2 = 15 * 1500.00; // 22500
        double expectedDiscount2 = subtotal2 * 0.10; // 10% only
        assertEquals("VIP 15 tickets discount should be 10%", 
                    expectedDiscount2, booking2.getDiscount(), 0.01);
    }
    
    @Test
    public void testDifferentCategories() {
        TicketCategory premium = categories.get("premium");
        TicketCategory student = categories.get("student");
        TicketCategory group = categories.get("group");
        
        // Premium: 10 tickets
        Booking b1 = new Booking("BK1007", "Frank", "frank@email.com", 
                                "333-333-3333", premium, 10);
        assertEquals("Premium 10 tickets subtotal", 8000.00, b1.getSubtotal(), 0.01);
        assertEquals("Premium 10 tickets discount", 800.00, b1.getDiscount(), 0.01);
        
        // Student: 5 tickets (5% discount)
        Booking b2 = new Booking("BK1008", "Grace", "grace@email.com", 
                                "444-444-4444", student, 5);
        assertEquals("Student 5 tickets subtotal", 1500.00, b2.getSubtotal(), 0.01);
        assertEquals("Student 5 tickets discount", 75.00, b2.getDiscount(), 0.01);
        
        // Group: 10 tickets (10% discount)
        Booking b3 = new Booking("BK1009", "Henry", "henry@email.com", 
                                "555-555-5555", group, 10);
        assertEquals("Group 10 tickets subtotal", 4500.00, b3.getSubtotal(), 0.01);
        assertEquals("Group 10 tickets discount", 450.00, b3.getDiscount(), 0.01);
    }
    
    @Test
    public void testBookingDetails() {
        TicketCategory premium = categories.get("premium");
        Booking booking = new Booking("BK1010", "Test User", "test@email.com", 
                                     "999-999-9999", premium, 15);
        
        String details = booking.getBookingDetails();
        
        assertTrue("Should contain booking ID", details.contains("Booking ID: BK1010"));
        assertTrue("Should contain customer name", details.contains("Customer Name: Test User"));
        assertTrue("Should contain category", details.contains("Ticket Category: Premium"));
        assertTrue("Should contain number of tickets", details.contains("Number of Tickets: 15"));
        assertTrue("Should contain subtotal", details.contains("Subtotal: ₹12000.00"));
        assertTrue("Should contain discount", details.contains("Discount Applied: -₹1200.00"));
        assertTrue("Should contain total amount", details.contains("TOTAL AMOUNT: ₹10800.00"));
    }
    
    @Test
    public void testNoDiscount() {
        TicketCategory regular = categories.get("regular");
        Booking booking = new Booking("BK1011", "NoDiscount", "no@email.com", 
                                     "000-000-0000", regular, 3);
        
        assertEquals("No discount for 3 tickets", 0.0, booking.getDiscount(), 0.01);
        assertEquals("Total should equal subtotal", booking.getSubtotal(), booking.getTotalAmount(), 0.01);
    }
}
