import USER.Player;
import USER.StadiumOwner;
import stadium.Stadium;
import booking.Booking;
import Time.TimeSlot;
import repository.PlayerRepository;
import repository.StadiumRepository;
import repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    // PlayerRepository Tests
    @Test
    public void testPlayerRepositoryCreation() {
        PlayerRepository repo = new PlayerRepository();
        assertTrue(repo.getAll().isEmpty());
    }

    @Test
    public void testAddPlayer() {
        PlayerRepository repo = new PlayerRepository();
        Player player = new Player(1, "John", "New York");
        
        repo.addPlayer(player);
        
        assertTrue(repo.contains(1));
        assertEquals(player, repo.getPlayer(1));
    }

    @Test
    public void testAddMultiplePlayers() {
        PlayerRepository repo = new PlayerRepository();
        
        for (int i = 1; i <= 5; i++) {
            repo.addPlayer(new Player(i, "Player" + i, "Town" + i));
        }
        
        assertEquals(5, repo.getAll().size());
    }

    @Test
    public void testAddDuplicatePlayerThrowsException() {
        PlayerRepository repo = new PlayerRepository();
        Player player = new Player(1, "John", "New York");
        
        repo.addPlayer(player);
        
        assertThrows(RuntimeException.class, () -> {
            repo.addPlayer(new Player(1, "Jane", "Boston"));
        });
    }

    @Test
    public void testRemovePlayer() {
        PlayerRepository repo = new PlayerRepository();
        Player player = new Player(1, "John", "New York");
        
        repo.addPlayer(player);
        repo.removePlayer(1);
        
        assertFalse(repo.contains(1));
    }

    @Test
    public void testRemoveNonExistentPlayerThrowsException() {
        PlayerRepository repo = new PlayerRepository();
        
        assertThrows(RuntimeException.class, () -> {
            repo.removePlayer(1);
        });
    }

    @Test
    public void testGetNonExistentPlayerThrowsException() {
        PlayerRepository repo = new PlayerRepository();
        
        assertThrows(RuntimeException.class, () -> {
            repo.getPlayer(1);
        });
    }

    @Test
    public void testPlayerContains() {
        PlayerRepository repo = new PlayerRepository();
        Player player = new Player(1, "John", "New York");
        
        assertFalse(repo.contains(1));
        repo.addPlayer(player);
        assertTrue(repo.contains(1));
    }

    @Test
    public void testGetAllPlayers() {
        PlayerRepository repo = new PlayerRepository();
        
        repo.addPlayer(new Player(1, "John", "New York"));
        repo.addPlayer(new Player(2, "Jane", "Boston"));
        repo.addPlayer(new Player(3, "Bob", "Chicago"));
        
        assertEquals(3, repo.getAll().size());
    }

    // StadiumRepository Tests
    @Test
    public void testStadiumRepositoryCreation() {
        StadiumRepository repo = new StadiumRepository();
        assertTrue(repo.getAll().isEmpty());
    }

    @Test
    public void testAddStadium() {
        StadiumRepository repo = new StadiumRepository();
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        
        repo.addStadium(stadium);
        
        assertTrue(repo.contains(1));
        assertEquals(stadium, repo.getStadium(1));
    }

    @Test
    public void testAddMultipleStadiums() {
        StadiumRepository repo = new StadiumRepository();
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        
        for (int i = 1; i <= 5; i++) {
            repo.addStadium(new Stadium(i, "Stadium" + i, "Town" + i, owner, 100, 50));
        }
        
        assertEquals(5, repo.getAll().size());
    }

    @Test
    public void testAddDuplicateStadiumThrowsException() {
        StadiumRepository repo = new StadiumRepository();
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        
        repo.addStadium(stadium);
        
        assertThrows(IllegalArgumentException.class, () -> {
            repo.addStadium(new Stadium(1, "Another", "Boston", owner, 120, 60));
        });
    }

    @Test
    public void testGetNonExistentStadiumThrowsException() {
        StadiumRepository repo = new StadiumRepository();
        
        assertThrows(IllegalArgumentException.class, () -> {
            repo.getStadium(1);
        });
    }

    @Test
    public void testStadiumContains() {
        StadiumRepository repo = new StadiumRepository();
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        
        assertFalse(repo.contains(1));
        repo.addStadium(stadium);
        assertTrue(repo.contains(1));
    }

    @Test
    public void testGetAllStadiums() {
        StadiumRepository repo = new StadiumRepository();
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        
        repo.addStadium(new Stadium(1, "Stadium1", "New York", owner, 100, 50));
        repo.addStadium(new Stadium(2, "Stadium2", "Boston", owner, 120, 60));
        repo.addStadium(new Stadium(3, "Stadium3", "Chicago", owner, 90, 40));
        
        assertEquals(3, repo.getAll().size());
    }

    // BookingRepository Tests
    @Test
    public void testBookingRepositoryCreation() {
        BookingRepository repo = new BookingRepository();
        assertTrue(repo.getAll().isEmpty());
    }

    @Test
    public void testAddBooking() {
        BookingRepository repo = new BookingRepository();
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        repo.addBooking(booking);
        
        assertTrue(repo.contains(1));
        assertEquals(booking, repo.getBooking(1));
    }

    @Test
    public void testAddNullBookingThrowsException() {
        BookingRepository repo = new BookingRepository();
        
        assertThrows(IllegalArgumentException.class, () -> {
            repo.addBooking(null);
        });
    }

    @Test
    public void testAddDuplicateBookingThrowsException() {
        BookingRepository repo = new BookingRepository();
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        repo.addBooking(booking);
        
        assertThrows(IllegalArgumentException.class, () -> {
            repo.addBooking(new Booking(1, 200, 6, DayOfWeek.TUESDAY, slot, 12));
        });
    }

    @Test
    public void testRemoveBooking() {
        BookingRepository repo = new BookingRepository();
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        repo.addBooking(booking);
        repo.removeBooking(1);
        
        assertFalse(repo.contains(1));
    }

    @Test
    public void testRemoveNonExistentBookingThrowsException() {
        BookingRepository repo = new BookingRepository();
        
        assertThrows(IllegalArgumentException.class, () -> {
            repo.removeBooking(1);
        });
    }

    @Test
    public void testGetNonExistentBookingThrowsException() {
        BookingRepository repo = new BookingRepository();
        
        assertThrows(IllegalArgumentException.class, () -> {
            repo.getBooking(1);
        });
    }

    @Test
    public void testBookingContains() {
        BookingRepository repo = new BookingRepository();
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        assertFalse(repo.contains(1));
        repo.addBooking(booking);
        assertTrue(repo.contains(1));
    }

    @Test
    public void testGetAllBookings() {
        BookingRepository repo = new BookingRepository();
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        repo.addBooking(new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10));
        repo.addBooking(new Booking(2, 101, 6, DayOfWeek.TUESDAY, slot, 12));
        repo.addBooking(new Booking(3, 102, 7, DayOfWeek.WEDNESDAY, slot, 15));
        
        assertEquals(3, repo.getAll().size());
    }

    @Test
    public void testAddMultipleBookings() {
        BookingRepository repo = new BookingRepository();
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        for (int i = 1; i <= 10; i++) {
            repo.addBooking(new Booking(i, 100, i, DayOfWeek.MONDAY, slot, 10));
        }
        
        assertEquals(10, repo.getAll().size());
    }

    // Integration Tests
    @Test
    public void testMultipleRepositoriesIndependence() {
        PlayerRepository playerRepo = new PlayerRepository();
        StadiumRepository stadiumRepo = new StadiumRepository();
        BookingRepository bookingRepo = new BookingRepository();
        
        Player player = new Player(1, "John", "New York");
        playerRepo.addPlayer(player);
        
        StadiumOwner owner = new StadiumOwner(1, "Owner", "New York");
        Stadium stadium = new Stadium(1, "Stadium", "New York", owner, 100, 50);
        stadiumRepo.addStadium(stadium);
        
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 1, 1, DayOfWeek.MONDAY, slot, 10);
        bookingRepo.addBooking(booking);
        
        assertEquals(1, playerRepo.getAll().size());
        assertEquals(1, stadiumRepo.getAll().size());
        assertEquals(1, bookingRepo.getAll().size());
    }
}
