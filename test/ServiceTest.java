import USER.Player;
import USER.StadiumOwner;
import stadium.Stadium;
import booking.Booking;
import booking.BookingStatus;
import Chat.Conversation;
import Time.TimeSlot;
import repository.*;
import Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    // FriendshipService Tests
    private PlayerRepository playerRepo;
    private FriendshipService friendshipService;

    @BeforeEach
    public void setupFriendshipTests() {
        playerRepo = new PlayerRepository();
        friendshipService = new FriendshipService(playerRepo);
    }

    @Test
    public void testSendFriendRequest() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        friendshipService.sendRequest(1, 2);
        
        assertTrue(player1.getOutgoingRequests().contains(2));
        assertTrue(player2.getPendingRequests().contains(1));
    }

    @Test
    public void testSendRequestToSelfThrowsException() {
        Player player = new Player(1, "John", "New York");
        playerRepo.addPlayer(player);
        
        assertThrows(RuntimeException.class, () -> {
            friendshipService.sendRequest(1, 1);
        });
    }

    @Test
    public void testSendRequestToNonExistentPlayerThrowsException() {
        Player player = new Player(1, "John", "New York");
        playerRepo.addPlayer(player);
        
        assertThrows(RuntimeException.class, () -> {
            friendshipService.sendRequest(1, 2);
        });
    }

    @Test
    public void testSendRequestWhenAlreadyFriendsThrowsException() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        player1.getFriends().add(2);
        player2.getFriends().add(1);
        
        assertThrows(RuntimeException.class, () -> {
            friendshipService.sendRequest(1, 2);
        });
    }

    @Test
    public void testSendDuplicateRequestThrowsException() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        friendshipService.sendRequest(1, 2);
        
        assertThrows(RuntimeException.class, () -> {
            friendshipService.sendRequest(1, 2);
        });
    }

    @Test
    public void testAcceptFriendRequest() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        friendshipService.sendRequest(1, 2);
        friendshipService.acceptRequest(1, 2);
        
        assertTrue(player1.getFriends().contains(2));
        assertTrue(player2.getFriends().contains(1));
        assertFalse(player1.getOutgoingRequests().contains(2));
        assertFalse(player2.getPendingRequests().contains(1));
    }

    @Test
    public void testAcceptNonExistentRequestThrowsException() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        assertThrows(RuntimeException.class, () -> {
            friendshipService.acceptRequest(1, 2);
        });
    }

    @Test
    public void testRejectFriendRequest() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        friendshipService.sendRequest(1, 2);
        friendshipService.rejectRequest(1, 2);
        
        assertFalse(player1.getOutgoingRequests().contains(2));
        assertFalse(player2.getPendingRequests().contains(1));
        assertFalse(player1.getFriends().contains(2));
        assertFalse(player2.getFriends().contains(1));
    }

    @Test
    public void testRemoveFriend() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        friendshipService.sendRequest(1, 2);
        friendshipService.acceptRequest(1, 2);
        friendshipService.removeFriend(1, 2);
        
        assertFalse(player1.getFriends().contains(2));
        assertFalse(player2.getFriends().contains(1));
    }

    @Test
    public void testRemoveNonFriendThrowsException() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        assertThrows(RuntimeException.class, () -> {
            friendshipService.removeFriend(1, 2);
        });
    }

    @Test
    public void testCancelFriendRequest() {
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        
        friendshipService.sendRequest(1, 2);
        friendshipService.cancelRequest(1, 2);
        
        assertFalse(player1.getOutgoingRequests().contains(2));
        assertFalse(player2.getPendingRequests().contains(1));
    }

    // ChatService Tests
    @Test
    public void testChatServiceCreation() {
        PlayerRepository repo = new PlayerRepository();
        ChatService chatService = new ChatService(repo);
        assertNotNull(chatService);
    }

    @Test
    public void testChatServiceWithNullRepoThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ChatService(null);
        });
    }

    @Test
    public void testSendMessage() {
        PlayerRepository repo = new PlayerRepository();
        ChatService chatService = new ChatService(repo);
        
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        repo.addPlayer(player1);
        repo.addPlayer(player2);
        
        player1.getFriends().add(2);
        player2.getFriends().add(1);
        
        chatService.sendMessage(1, 2, "Hello");
        
        Conversation conv = chatService.getConversation(1, 2);
        assertEquals(1, conv.getMessages().size());
        assertEquals("Hello", conv.getMessages().get(0).getContent());
    }

    @Test
    public void testSendMessageToNonFriendThrowsException() {
        PlayerRepository repo = new PlayerRepository();
        ChatService chatService = new ChatService(repo);
        
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        repo.addPlayer(player1);
        repo.addPlayer(player2);
        
        assertThrows(RuntimeException.class, () -> {
            chatService.sendMessage(1, 2, "Hello");
        });
    }

    @Test
    public void testSendMessageToSelfThrowsException() {
        PlayerRepository repo = new PlayerRepository();
        ChatService chatService = new ChatService(repo);
        
        Player player = new Player(1, "John", "New York");
        repo.addPlayer(player);
        
        assertThrows(RuntimeException.class, () -> {
            chatService.sendMessage(1, 1, "Hello");
        });
    }

    @Test
    public void testGetConversation() {
        PlayerRepository repo = new PlayerRepository();
        ChatService chatService = new ChatService(repo);
        
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        repo.addPlayer(player1);
        repo.addPlayer(player2);
        
        Conversation conv = chatService.getConversation(1, 2);
        assertNotNull(conv);
        assertTrue(conv.getMessages().isEmpty());
    }

    @Test
    public void testHasConversation() {
        PlayerRepository repo = new PlayerRepository();
        ChatService chatService = new ChatService(repo);
        
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "New York");
        repo.addPlayer(player1);
        repo.addPlayer(player2);
        
        player1.getFriends().add(2);
        player2.getFriends().add(1);
        
        assertFalse(chatService.hasConversation(1, 2));
        
        chatService.sendMessage(1, 2, "Hello");
        
        assertTrue(chatService.hasConversation(1, 2));
    }

    // SearchService Tests
    @Test
    public void testSearchServiceCreation() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        SearchService searchService = new SearchService(playerRepo, stadiumRepo);
        assertNotNull(searchService);
    }

    @Test
    public void testSearchServiceWithNullReposThrowsException() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        
        assertThrows(IllegalArgumentException.class, () -> {
            new SearchService(null, stadiumRepo);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new SearchService(playerRepo, null);
        });
    }

    @Test
    public void testSearchPlayers() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        SearchService searchService = new SearchService(playerRepo, stadiumRepo);
        
        Player player1 = new Player(1, "John", "New York");
        Player player2 = new Player(2, "Jane", "Boston");
        Player player3 = new Player(3, "Bob", "New York");
        
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        player1.getSchedule().addSlot(DayOfWeek.MONDAY, slot);
        player2.getSchedule().addSlot(DayOfWeek.MONDAY, slot);
        player3.getSchedule().addSlot(DayOfWeek.MONDAY, slot);
        
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);
        playerRepo.addPlayer(player3);
        
        Set<String> towns = new HashSet<>();
        towns.add("New York");
        
        TimeSlot searchSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        List<Player> results = searchService.searchPlayers(towns, DayOfWeek.MONDAY, searchSlot);
        
        assertEquals(2, results.size());
    }

    @Test
    public void testSearchStadiums() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        SearchService searchService = new SearchService(playerRepo, stadiumRepo);
        
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        Stadium stadium1 = new Stadium(1, "Stadium1", "New York", owner, 100, 50);
        Stadium stadium2 = new Stadium(2, "Stadium2", "Boston", owner, 120, 60);
        Stadium stadium3 = new Stadium(3, "Stadium3", "New York", owner, 90, 40);
        
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        stadium1.getSchedule().addSlot(DayOfWeek.MONDAY, slot);
        stadium2.getSchedule().addSlot(DayOfWeek.MONDAY, slot);
        stadium3.getSchedule().addSlot(DayOfWeek.MONDAY, slot);
        
        stadiumRepo.addStadium(stadium1);
        stadiumRepo.addStadium(stadium2);
        stadiumRepo.addStadium(stadium3);
        
        Set<String> towns = new HashSet<>();
        towns.add("New York");
        
        TimeSlot searchSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        List<Stadium> results = searchService.searchStadiums(towns, DayOfWeek.MONDAY, searchSlot);
        
        assertEquals(2, results.size());
    }

    @Test
    public void testSearchWithNullParametersThrowsException() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        SearchService searchService = new SearchService(playerRepo, stadiumRepo);
        
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Set<String> towns = new HashSet<>();
        towns.add("New York");
        
        assertThrows(IllegalArgumentException.class, () -> {
            searchService.searchPlayers(null, DayOfWeek.MONDAY, slot);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            searchService.searchPlayers(towns, null, slot);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            searchService.searchPlayers(towns, DayOfWeek.MONDAY, null);
        });
    }

    // BookingService Tests
    @Test
    public void testBookingServiceCreation() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        assertNotNull(bookingService);
    }

    @Test
    public void testCreateBooking() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        Player organizer = new Player(1, "John", "New York");
        playerRepo.addPlayer(organizer);
        
        StadiumOwner owner = new StadiumOwner(2, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        Booking booking = bookingService.createBooking(1, 1, DayOfWeek.MONDAY, bookingSlot, 10);
        
        assertNotNull(booking);
        assertEquals(1, booking.getOrganizerId());
        assertEquals(1, booking.getStadiumId());
        assertTrue(bookingRepo.contains(booking.getBookingId()));
    }

    @Test
    public void testCreateBookingWithUnavailableStadiumThrowsException() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        Player organizer = new Player(1, "John", "New York");
        playerRepo.addPlayer(organizer);
        
        StadiumOwner owner = new StadiumOwner(2, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(1, 1, DayOfWeek.MONDAY, bookingSlot, 10);
        });
    }

    @Test
    public void testInvitePlayer() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        Player organizer = new Player(1, "John", "New York");
        Player invited = new Player(2, "Jane", "New York");
        playerRepo.addPlayer(organizer);
        playerRepo.addPlayer(invited);
        
        StadiumOwner owner = new StadiumOwner(3, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        Booking booking = bookingService.createBooking(1, 1, DayOfWeek.MONDAY, bookingSlot, 10);
        
        bookingService.invitePlayer(booking.getBookingId(), 1, 2);
        
        assertTrue(booking.getInvited().contains(2));
    }

    @Test
    public void testRespondToInviteAccept() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        Player organizer = new Player(1, "John", "New York");
        playerRepo.addPlayer(organizer);
        
        for (int i = 2; i <= 11; i++) {
            playerRepo.addPlayer(new Player(i, "Player" + i, "New York"));
        }
        
        StadiumOwner owner = new StadiumOwner(12, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        Booking booking = bookingService.createBooking(1, 1, DayOfWeek.MONDAY, bookingSlot, 10);
        
        for (int i = 2; i <= 11; i++) {
            bookingService.invitePlayer(booking.getBookingId(), 1, i);
        }
        
        for (int i = 2; i <= 11; i++) {
            bookingService.respondToInvite(booking.getBookingId(), i, true);
        }
        
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertFalse(stadium.getSchedule().isAvailable(DayOfWeek.MONDAY, bookingSlot));
    }

    @Test
    public void testCancelBooking() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        Player organizer = new Player(1, "John", "New York");
        playerRepo.addPlayer(organizer);
        
        StadiumOwner owner = new StadiumOwner(2, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        Booking booking = bookingService.createBooking(1, 1, DayOfWeek.MONDAY, bookingSlot, 10);
        
        bookingService.cancelBooking(booking.getBookingId(), 1);
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    public void testCancelByNonOrganizerThrowsException() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        BookingService bookingService = new BookingService(playerRepo, stadiumRepo, bookingRepo);
        
        Player organizer = new Player(1, "John", "New York");
        playerRepo.addPlayer(organizer);
        
        StadiumOwner owner = new StadiumOwner(2, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot bookingSlot = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        Booking booking = bookingService.createBooking(1, 1, DayOfWeek.MONDAY, bookingSlot, 10);
        
        assertThrows(RuntimeException.class, () -> {
            bookingService.cancelBooking(booking.getBookingId(), 99);
        });
    }
}
