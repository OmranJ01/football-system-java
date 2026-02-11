import booking.Booking;
import booking.BookingStatus;
import Time.TimeSlot;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    public void testBookingCreationWithValidData() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        assertEquals(1, booking.getBookingId());
        assertEquals(100, booking.getStadiumId());
        assertEquals(5, booking.getOrganizerId());
        assertEquals(DayOfWeek.MONDAY, booking.getDay());
        assertEquals(slot, booking.getSlot());
        assertEquals(10, booking.getMinPlayers());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertTrue(booking.isPending());
        assertFalse(booking.isConfirmed());
        assertFalse(booking.isCancelled());
    }

    @Test
    public void testBookingWithInvalidBookingIdThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(0, 100, 5, DayOfWeek.MONDAY, slot, 10);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(-1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        });
    }

    @Test
    public void testBookingWithInvalidStadiumIdThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 0, 5, DayOfWeek.MONDAY, slot, 10);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, -1, 5, DayOfWeek.MONDAY, slot, 10);
        });
    }

    @Test
    public void testBookingWithInvalidOrganizerIdThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 100, 0, DayOfWeek.MONDAY, slot, 10);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 100, -1, DayOfWeek.MONDAY, slot, 10);
        });
    }

    @Test
    public void testBookingWithNullDayThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 100, 5, null, slot, 10);
        });
    }

    @Test
    public void testBookingWithNullSlotThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 100, 5, DayOfWeek.MONDAY, null, 10);
        });
    }

    @Test
    public void testBookingWithInvalidMinPlayersThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, -1);
        });
    }

    @Test
    public void testInvitePlayerSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        
        assertTrue(booking.getInvited().contains(10));
        assertEquals(1, booking.getInvited().size());
    }

    @Test
    public void testInviteMultiplePlayers() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.invitePlayer(11);
        booking.invitePlayer(12);
        
        assertEquals(3, booking.getInvited().size());
        assertTrue(booking.getInvited().contains(10));
        assertTrue(booking.getInvited().contains(11));
        assertTrue(booking.getInvited().contains(12));
    }

    @Test
    public void testInvitePlayerWithInvalidIdThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        assertThrows(IllegalArgumentException.class, () -> {
            booking.invitePlayer(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            booking.invitePlayer(-1);
        });
    }

    @Test
    public void testInviteOrganizerThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        assertThrows(RuntimeException.class, () -> {
            booking.invitePlayer(5);
        });
    }

    @Test
    public void testInviteAlreadyInvitedPlayerThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        
        assertThrows(RuntimeException.class, () -> {
            booking.invitePlayer(10);
        });
    }

    @Test
    public void testInviteWhenNotPendingThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.cancel();
        
        assertThrows(RuntimeException.class, () -> {
            booking.invitePlayer(10);
        });
    }

    @Test
    public void testAcceptInviteSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.acceptInvite(10);
        
        assertFalse(booking.getInvited().contains(10));
        assertTrue(booking.getAccepted().contains(10));
        assertEquals(1, booking.acceptedCount());
    }

    @Test
    public void testAcceptMultipleInvites() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.invitePlayer(11);
        booking.invitePlayer(12);
        
        booking.acceptInvite(10);
        booking.acceptInvite(11);
        
        assertEquals(2, booking.acceptedCount());
        assertTrue(booking.getAccepted().contains(10));
        assertTrue(booking.getAccepted().contains(11));
        assertFalse(booking.getAccepted().contains(12));
    }

    @Test
    public void testAcceptInviteWhenNotInvitedThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        assertThrows(RuntimeException.class, () -> {
            booking.acceptInvite(10);
        });
    }

    @Test
    public void testAcceptInviteWhenNotPendingThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.cancel();
        
        assertThrows(RuntimeException.class, () -> {
            booking.acceptInvite(10);
        });
    }

    @Test
    public void testDeclineInviteSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.declineInvite(10);
        
        assertFalse(booking.getInvited().contains(10));
        assertTrue(booking.getDeclined().contains(10));
        assertEquals(0, booking.acceptedCount());
    }

    @Test
    public void testDeclineMultipleInvites() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.invitePlayer(11);
        
        booking.declineInvite(10);
        booking.declineInvite(11);
        
        assertEquals(2, booking.getDeclined().size());
        assertTrue(booking.getDeclined().contains(10));
        assertTrue(booking.getDeclined().contains(11));
    }

    @Test
    public void testDeclineInviteWhenNotInvitedThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        assertThrows(RuntimeException.class, () -> {
            booking.declineInvite(10);
        });
    }

    @Test
    public void testDeclineInviteWhenNotPendingThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.cancel();
        
        assertThrows(RuntimeException.class, () -> {
            booking.declineInvite(10);
        });
    }

    @Test
    public void testConfirmBookingSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.confirm();
        
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertTrue(booking.isConfirmed());
        assertFalse(booking.isPending());
    }

    @Test
    public void testConfirmWhenNotPendingThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.confirm();
        
        assertThrows(RuntimeException.class, () -> {
            booking.confirm();
        });
    }

    @Test
    public void testCancelPendingBookingSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.cancel();
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(booking.isCancelled());
        assertFalse(booking.isPending());
    }

    @Test
    public void testCancelConfirmedBookingSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.confirm();
        booking.cancel();
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(booking.isCancelled());
    }

    @Test
    public void testCancelAlreadyCancelledBookingThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.cancel();
        
        assertThrows(RuntimeException.class, () -> {
            booking.cancel();
        });
    }

    @Test
    public void testGetSetsAreUnmodifiable() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            booking.getInvited().add(11);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            booking.getAccepted().add(11);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            booking.getDeclined().add(11);
        });
    }

    @Test
    public void testComplexBookingScenario() {
        TimeSlot slot = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.SATURDAY, slot, 5);
        
        // Invite 10 players
        for (int i = 10; i < 20; i++) {
            booking.invitePlayer(i);
        }
        
        assertEquals(10, booking.getInvited().size());
        
        // 6 accept
        for (int i = 10; i < 16; i++) {
            booking.acceptInvite(i);
        }
        
        // 2 decline
        booking.declineInvite(16);
        booking.declineInvite(17);
        
        // 2 still pending
        assertEquals(2, booking.getInvited().size());
        assertEquals(6, booking.getAccepted().size());
        assertEquals(2, booking.getDeclined().size());
        
        assertTrue(booking.getInvited().contains(18));
        assertTrue(booking.getInvited().contains(19));
    }

    @Test
    public void testInviteAfterAcceptThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.acceptInvite(10);
        
        assertThrows(RuntimeException.class, () -> {
            booking.invitePlayer(10);
        });
    }

    @Test
    public void testInviteAfterDeclineThrowsException() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 10);
        
        booking.invitePlayer(10);
        booking.declineInvite(10);
        
        assertThrows(RuntimeException.class, () -> {
            booking.invitePlayer(10);
        });
    }

    @Test
    public void testBookingForAllDaysOfWeek() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        for (DayOfWeek day : DayOfWeek.values()) {
            Booking booking = new Booking(1, 100, 5, day, slot, 10);
            assertEquals(day, booking.getDay());
        }
    }

    @Test
    public void testBookingWithDifferentTimeSlots() {
        Booking morning = new Booking(1, 100, 5, DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(8, 0), LocalTime.of(10, 0)), 10);
        Booking afternoon = new Booking(2, 100, 5, DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0)), 10);
        Booking evening = new Booking(3, 100, 5, DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(18, 0), LocalTime.of(20, 0)), 10);
        
        assertNotEquals(morning.getSlot(), afternoon.getSlot());
        assertNotEquals(afternoon.getSlot(), evening.getSlot());
    }

    @Test
    public void testBookingWithMinimumMinPlayers() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 1);
        
        assertEquals(1, booking.getMinPlayers());
    }

    @Test
    public void testBookingWithLargeMinPlayers() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking booking = new Booking(1, 100, 5, DayOfWeek.MONDAY, slot, 100);
        
        assertEquals(100, booking.getMinPlayers());
    }
}
