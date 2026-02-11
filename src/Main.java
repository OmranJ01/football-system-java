import USER.Player;
import USER.StadiumOwner;
import stadium.Stadium;
import booking.Booking;
import booking.BookingStatus;
import Time.TimeSlot;
import Time.AvailabilitySchedule;
import repository.*;
import Service.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Football Stadium Booking System ===\n");
        
        // Initialize repositories
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        
        // Initialize services
        FriendshipService friendshipService = new FriendshipService(playerRepo);
        ChatService chatService = new ChatService(playerRepo);
        SearchService searchService = new SearchService(playerRepo, stadiumRepo);
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        // Create players
        System.out.println("Creating players...");
        Player player1 = new Player(1, "John Smith", "New York");
        Player player2 = new Player(2, "Jane Doe", "New York");
        Player player3 = new Player(3, "Bob Johnson", "Boston");
        Player player4 = new Player(4, "Alice Brown", "New York");
        Player player5 = new Player(5, "Charlie Wilson", "New York");
        
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        playerRepo.addPlayer(player3);
        playerRepo.addPlayer(player4);
        playerRepo.addPlayer(player5);
        System.out.println("Created " + playerRepo.getAll().size() + " players\n");
        
        // Set player availability
        System.out.println("Setting player availability...");
        TimeSlot mondayMorning = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot mondayAfternoon = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(18, 0));
        
        player1.getSchedule().addSlot(DayOfWeek.MONDAY, mondayMorning);
        player1.getSchedule().addSlot(DayOfWeek.MONDAY, mondayAfternoon);
        player2.getSchedule().addSlot(DayOfWeek.MONDAY, mondayAfternoon);
        player4.getSchedule().addSlot(DayOfWeek.MONDAY, mondayAfternoon);
        player5.getSchedule().addSlot(DayOfWeek.MONDAY, mondayAfternoon);
        System.out.println("Player availability set\n");
        
        // Create stadium owner and stadiums
        System.out.println("Creating stadiums...");
        StadiumOwner owner = new StadiumOwner(100, "Stadium Corp", "New York");
        Stadium stadium1 = new Stadium(1, "Central Stadium", "New York", owner, 150, 22);
        Stadium stadium2 = new Stadium(2, "North Arena", "New York", owner, 200, 30);
        Stadium stadium3 = new Stadium(3, "Boston Sports Complex", "Boston", owner, 180, 25);
        
        owner.getStadiums().add(stadium1);
        owner.getStadiums().add(stadium2);
        owner.getStadiums().add(stadium3);
        
        // Set stadium availability
        stadium1.getSchedule().addSlot(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(8, 0), LocalTime.of(20, 0)));
        stadium2.getSchedule().addSlot(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(22, 0)));
        stadium3.getSchedule().addSlot(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(9, 0), LocalTime.of(21, 0)));
        
        stadiumRepo.addStadium(stadium1);
        stadiumRepo.addStadium(stadium2);
        stadiumRepo.addStadium(stadium3);
        System.out.println("Created " + stadiumRepo.getAll().size() + " stadiums\n");
        
        // Demonstrate friendship functionality
        System.out.println("=== Testing Friendship System ===");
        System.out.println("Sending friend requests...");
        friendshipService.sendRequest(1, 2);
        friendshipService.sendRequest(1, 4);
        friendshipService.sendRequest(1, 5);
        System.out.println("Player 1 sent requests to players 2, 4, and 5");
        
        System.out.println("Accepting friend requests...");
        friendshipService.acceptRequest(1, 2);
        friendshipService.acceptRequest(1, 4);
        friendshipService.acceptRequest(1, 5);
        System.out.println("Players 2, 4, and 5 accepted");
        System.out.println("Player 1 now has " + player1.getFriends().size() + " friends\n");
        
        // Demonstrate chat functionality
        System.out.println("=== Testing Chat System ===");
        chatService.sendMessage(1, 2, "Hey Jane, want to play football on Monday?");
        chatService.sendMessage(2, 1, "Sure! What time?");
        chatService.sendMessage(1, 2, "How about 3 PM?");
        chatService.sendMessage(2, 1, "Perfect! See you there.");
        
        var conversation = chatService.getConversation(1, 2);
        System.out.println("Conversation between Player 1 and Player 2:");
        conversation.getMessages().forEach(msg -> 
            System.out.println("  Player " + msg.getSenderId() + ": " + msg.getContent())
        );
        System.out.println();
        
        // Demonstrate search functionality
        System.out.println("=== Testing Search System ===");
        Set<String> searchTowns = new HashSet<>();
        searchTowns.add("New York");
        
        TimeSlot searchSlot = new TimeSlot(LocalTime.of(15, 0), LocalTime.of(17, 0));
        List<Player> availablePlayers = searchService.searchPlayers(
            searchTowns, DayOfWeek.MONDAY, searchSlot
        );
        System.out.println("Available players in New York on Monday 3-5 PM: " + availablePlayers.size());
        availablePlayers.forEach(p -> System.out.println("  - " + p.getName()));
        
        List<Stadium> availableStadiums = searchService.searchStadiums(
            searchTowns, DayOfWeek.MONDAY, searchSlot
        );
        System.out.println("\nAvailable stadiums in New York on Monday 3-5 PM: " + availableStadiums.size());
        availableStadiums.forEach(s -> 
            System.out.println("  - " + s.getName() + " (Capacity: " + s.getCapacity() + 
                             ", Price: $" + s.getPricePerHour() + "/hr)")
        );
        System.out.println();
        
        // Demonstrate booking functionality
        System.out.println("=== Testing Booking System ===");
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(15, 0), LocalTime.of(17, 0));
        System.out.println("Creating a booking for Monday 3-5 PM...");
        Booking booking = bookingService.createBooking(
            1, // organizer (player1)
            1, // stadium1
            DayOfWeek.MONDAY,
            bookingSlot,
            5  // minimum 5 players
        );
        System.out.println("Booking created with ID: " + booking.getBookingId());
        
        System.out.println("\nInviting players to the booking...");
        bookingService.invitePlayer(booking.getBookingId(), 1, 2);
        bookingService.invitePlayer(booking.getBookingId(), 1, 4);
        bookingService.invitePlayer(booking.getBookingId(), 1, 5);
        System.out.println("Invited players: 2, 4, 5");
        
        System.out.println("\nPlayers responding to invites...");
        bookingService.respondToInvite(booking.getBookingId(), 2, true);
        System.out.println("Player 2 accepted");
        bookingService.respondToInvite(booking.getBookingId(), 4, true);
        System.out.println("Player 4 accepted");
        bookingService.respondToInvite(booking.getBookingId(), 5, true);
        System.out.println("Player 5 accepted");
        
        System.out.println("\nBooking status: " + booking.getStatus());
        System.out.println("Accepted players: " + booking.acceptedCount() + "/" + booking.getMinPlayers());
        
        if (booking.isConfirmed()) {
            System.out.println("✓ Booking is CONFIRMED!");
            System.out.println("Stadium slot is now booked.");
            
            // Verify stadium is no longer available
            boolean stillAvailable = stadium1.getSchedule().isAvailable(DayOfWeek.MONDAY, bookingSlot);
            System.out.println("Stadium still available at this time? " + stillAvailable);
        }
        
        System.out.println("\n=== System Summary ===");
        System.out.println("Total Players: " + playerRepo.getAll().size());
        System.out.println("Total Stadiums: " + stadiumRepo.getAll().size());
        System.out.println("Total Bookings: " + bookingRepo.getAll().size());
        System.out.println("Active Friendships: " + player1.getFriends().size());
        
        System.out.println("\n=== Football Stadium Booking System - Demo Complete ===");
    }
}

