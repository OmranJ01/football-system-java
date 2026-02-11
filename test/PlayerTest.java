import USER.Player;
import Time.TimeSlot;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerCreationWithValidData() {
        Player player = new Player(1, "John Doe", "New York");
        
        assertEquals(1, player.getId());
        assertEquals("John Doe", player.getName());
        assertEquals("New York", player.getTown());
        assertNotNull(player.getSchedule());
        assertNotNull(player.getFriends());
        assertNotNull(player.getOutgoingRequests());
        assertNotNull(player.getPendingRequests());
    }

    @Test
    public void testPlayerWithInvalidIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(0, "John Doe", "New York");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(-1, "John Doe", "New York");
        });
    }

    @Test
    public void testPlayerWithNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, null, "New York");
        });
    }

    @Test
    public void testPlayerWithBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, "", "New York");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, "   ", "New York");
        });
    }

    @Test
    public void testPlayerWithNullTownThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, "John Doe", null);
        });
    }

    @Test
    public void testPlayerWithBlankTownThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, "John Doe", "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, "John Doe", "   ");
        });
    }

    @Test
    public void testUpdateTown() {
        Player player = new Player(1, "John Doe", "New York");
        player.updateTown("Los Angeles");
        
        assertEquals("Los Angeles", player.getTown());
    }

    @Test
    public void testFriendsSetInitiallyEmpty() {
        Player player = new Player(1, "John Doe", "New York");
        
        assertTrue(player.getFriends().isEmpty());
    }

    @Test
    public void testOutgoingRequestsSetInitiallyEmpty() {
        Player player = new Player(1, "John Doe", "New York");
        
        assertTrue(player.getOutgoingRequests().isEmpty());
    }

    @Test
    public void testPendingRequestsSetInitiallyEmpty() {
        Player player = new Player(1, "John Doe", "New York");
        
        assertTrue(player.getPendingRequests().isEmpty());
    }

    @Test
    public void testAddFriend() {
        Player player = new Player(1, "John Doe", "New York");
        player.getFriends().add(2);
        
        assertTrue(player.getFriends().contains(2));
        assertEquals(1, player.getFriends().size());
    }

    @Test
    public void testAddMultipleFriends() {
        Player player = new Player(1, "John Doe", "New York");
        player.getFriends().add(2);
        player.getFriends().add(3);
        player.getFriends().add(4);
        
        assertEquals(3, player.getFriends().size());
        assertTrue(player.getFriends().contains(2));
        assertTrue(player.getFriends().contains(3));
        assertTrue(player.getFriends().contains(4));
    }

    @Test
    public void testRemoveFriend() {
        Player player = new Player(1, "John Doe", "New York");
        player.getFriends().add(2);
        player.getFriends().remove(2);
        
        assertFalse(player.getFriends().contains(2));
        assertTrue(player.getFriends().isEmpty());
    }

    @Test
    public void testAddOutgoingRequest() {
        Player player = new Player(1, "John Doe", "New York");
        player.getOutgoingRequests().add(2);
        
        assertTrue(player.getOutgoingRequests().contains(2));
        assertEquals(1, player.getOutgoingRequests().size());
    }

    @Test
    public void testAddPendingRequest() {
        Player player = new Player(1, "John Doe", "New York");
        player.getPendingRequests().add(2);
        
        assertTrue(player.getPendingRequests().contains(2));
        assertEquals(1, player.getPendingRequests().size());
    }

    @Test
    public void testPlayerScheduleCanAddSlots() {
        Player player = new Player(1, "John Doe", "New York");
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertTrue(player.getSchedule().addSlot(DayOfWeek.MONDAY, slot));
        assertTrue(player.getSchedule().isAvailable(DayOfWeek.MONDAY, slot));
    }

    @Test
    public void testPlayerScheduleCanBookSlots() {
        Player player = new Player(1, "John Doe", "New York");
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        player.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        player.getSchedule().bookSlot(DayOfWeek.MONDAY, booking);
        
        assertFalse(player.getSchedule().isAvailable(DayOfWeek.MONDAY, booking));
    }

    @Test
    public void testFriendsDontContainDuplicates() {
        Player player = new Player(1, "John Doe", "New York");
        player.getFriends().add(2);
        player.getFriends().add(2);
        
        assertEquals(1, player.getFriends().size());
    }

    @Test
    public void testOutgoingRequestsDontContainDuplicates() {
        Player player = new Player(1, "John Doe", "New York");
        player.getOutgoingRequests().add(2);
        player.getOutgoingRequests().add(2);
        
        assertEquals(1, player.getOutgoingRequests().size());
    }

    @Test
    public void testPendingRequestsDontContainDuplicates() {
        Player player = new Player(1, "John Doe", "New York");
        player.getPendingRequests().add(2);
        player.getPendingRequests().add(2);
        
        assertEquals(1, player.getPendingRequests().size());
    }

    @Test
    public void testPlayerWithSpecialCharactersInName() {
        Player player = new Player(1, "José O'Brien-Smith", "São Paulo");
        
        assertEquals("José O'Brien-Smith", player.getName());
        assertEquals("São Paulo", player.getTown());
    }

    @Test
    public void testPlayerWithLongName() {
        String longName = "Alexander Christopher Benjamin Montgomery III";
        Player player = new Player(1, longName, "New York");
        
        assertEquals(longName, player.getName());
    }

    @Test
    public void testPlayerWithVeryLargeId() {
        Player player = new Player(Integer.MAX_VALUE, "John Doe", "New York");
        
        assertEquals(Integer.MAX_VALUE, player.getId());
    }

    @Test
    public void testComplexFriendshipScenario() {
        Player player = new Player(1, "John Doe", "New York");
        
        // Add multiple friends
        for (int i = 2; i <= 10; i++) {
            player.getFriends().add(i);
        }
        
        // Add multiple pending requests
        for (int i = 11; i <= 15; i++) {
            player.getPendingRequests().add(i);
        }
        
        // Add multiple outgoing requests
        for (int i = 16; i <= 20; i++) {
            player.getOutgoingRequests().add(i);
        }
        
        assertEquals(9, player.getFriends().size());
        assertEquals(5, player.getPendingRequests().size());
        assertEquals(5, player.getOutgoingRequests().size());
    }

    @Test
    public void testPlayerStateTransitions() {
        Player player = new Player(1, "John Doe", "New York");
        
        // Simulate sending a friend request
        player.getOutgoingRequests().add(2);
        assertTrue(player.getOutgoingRequests().contains(2));
        
        // Simulate request being accepted
        player.getOutgoingRequests().remove(2);
        player.getFriends().add(2);
        
        assertFalse(player.getOutgoingRequests().contains(2));
        assertTrue(player.getFriends().contains(2));
    }

    @Test
    public void testUpdateTownMultipleTimes() {
        Player player = new Player(1, "John Doe", "New York");
        
        player.updateTown("Los Angeles");
        assertEquals("Los Angeles", player.getTown());
        
        player.updateTown("Chicago");
        assertEquals("Chicago", player.getTown());
        
        player.updateTown("Miami");
        assertEquals("Miami", player.getTown());
    }
}
