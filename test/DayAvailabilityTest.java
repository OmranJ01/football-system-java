import Time.DayAvailability;
import Time.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class DayAvailabilityTest {

    private DayAvailability dayAvailability;

    @BeforeEach
    public void setUp() {
        dayAvailability = new DayAvailability();
    }

    @Test
    public void testAddSlotSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertTrue(dayAvailability.addSlot(slot));
        assertEquals(1, dayAvailability.getSlots().size());
    }

    @Test
    public void testAddMultipleNonOverlappingSlots() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0));
        
        assertTrue(dayAvailability.addSlot(slot1));
        assertTrue(dayAvailability.addSlot(slot2));
        assertEquals(2, dayAvailability.getSlots().size());
    }

    @Test
    public void testAddOverlappingSlotFails() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertTrue(dayAvailability.addSlot(slot1));
        assertFalse(dayAvailability.addSlot(slot2));
        assertEquals(1, dayAvailability.getSlots().size());
    }

    @Test
    public void testAddAdjacentSlots() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0));
        
        assertTrue(dayAvailability.addSlot(slot1));
        assertTrue(dayAvailability.addSlot(slot2));
        assertEquals(2, dayAvailability.getSlots().size());
    }

    @Test
    public void testSlotsSortedByStartTime() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot3 = new TimeSlot(LocalTime.of(18, 0), LocalTime.of(20, 0));
        
        dayAvailability.addSlot(slot1);
        dayAvailability.addSlot(slot2);
        dayAvailability.addSlot(slot3);
        
        assertEquals(slot2, dayAvailability.getSlots().get(0));
        assertEquals(slot1, dayAvailability.getSlots().get(1));
        assertEquals(slot3, dayAvailability.getSlots().get(2));
    }

    @Test
    public void testRemoveSlotSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        dayAvailability.addSlot(slot);
        assertTrue(dayAvailability.removeSlot(slot));
        assertEquals(0, dayAvailability.getSlots().size());
    }

    @Test
    public void testRemoveNonExistentSlotReturnsFalse() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0));
        
        dayAvailability.addSlot(slot1);
        assertFalse(dayAvailability.removeSlot(slot2));
        assertEquals(1, dayAvailability.getSlots().size());
    }

    @Test
    public void testRemoveNullSlotThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            dayAvailability.removeSlot(null);
        });
    }

    @Test
    public void testRemoveSlotWithBookingThrowsException() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking);
        
        assertThrows(RuntimeException.class, () -> {
            dayAvailability.removeSlot(availability);
        });
    }

    @Test
    public void testIsAvailableWhenSlotIsAvailable() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        dayAvailability.addSlot(availability);
        
        assertTrue(dayAvailability.isAvailable(requested));
    }

    @Test
    public void testIsNotAvailableWhenNoAvailabilityWindow() {
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        assertFalse(dayAvailability.isAvailable(requested));
    }

    @Test
    public void testIsNotAvailableWhenOutsideAvailabilityWindow() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot requested = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 0));
        
        dayAvailability.addSlot(availability);
        
        assertFalse(dayAvailability.isAvailable(requested));
    }

    @Test
    public void testIsNotAvailableWhenPartiallyOutsideWindow() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        dayAvailability.addSlot(availability);
        
        assertFalse(dayAvailability.isAvailable(requested));
    }

    @Test
    public void testIsNotAvailableWhenSlotIsBooked() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booked = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 30), LocalTime.of(13, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booked);
        
        assertFalse(dayAvailability.isAvailable(requested));
    }

    @Test
    public void testBookSlotSuccessfully() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking);
        
        assertEquals(1, dayAvailability.getBookedSlots().size());
        assertFalse(dayAvailability.isAvailable(booking));
    }

    @Test
    public void testBookMultipleNonOverlappingSlots() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(16, 0));
        TimeSlot booking1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0));
        TimeSlot booking2 = new TimeSlot(LocalTime.of(12, 0), LocalTime.of(13, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking1);
        dayAvailability.bookSlot(booking2);
        
        assertEquals(2, dayAvailability.getBookedSlots().size());
    }

    @Test
    public void testBookSlotWhenNotAvailableThrowsException() {
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        assertThrows(RuntimeException.class, () -> {
            dayAvailability.bookSlot(booking);
        });
    }

    @Test
    public void testBookOverlappingSlotThrowsException() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking1 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        TimeSlot booking2 = new TimeSlot(LocalTime.of(11, 30), LocalTime.of(13, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking1);
        
        assertThrows(RuntimeException.class, () -> {
            dayAvailability.bookSlot(booking2);
        });
    }

    @Test
    public void testBookedSlotsSortedByStartTime() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(18, 0));
        TimeSlot booking1 = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(15, 0));
        TimeSlot booking2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        TimeSlot booking3 = new TimeSlot(LocalTime.of(16, 0), LocalTime.of(17, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking1);
        dayAvailability.bookSlot(booking2);
        dayAvailability.bookSlot(booking3);
        
        assertEquals(booking2, dayAvailability.getBookedSlots().get(0));
        assertEquals(booking1, dayAvailability.getBookedSlots().get(1));
        assertEquals(booking3, dayAvailability.getBookedSlots().get(2));
    }

    @Test
    public void testGetSlotsReturnsUnmodifiableList() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        dayAvailability.addSlot(slot);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            dayAvailability.getSlots().add(slot);
        });
    }

    @Test
    public void testGetBookedSlotsReturnsUnmodifiableList() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            dayAvailability.getBookedSlots().add(booking);
        });
    }

    @Test
    public void testComplexAvailabilityScenario() {
        // Add multiple availability windows
        TimeSlot availability1 = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(12, 0));
        TimeSlot availability2 = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(18, 0));
        
        dayAvailability.addSlot(availability1);
        dayAvailability.addSlot(availability2);
        
        // Book some slots
        TimeSlot booking1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot booking2 = new TimeSlot(LocalTime.of(15, 0), LocalTime.of(16, 0));
        
        dayAvailability.bookSlot(booking1);
        dayAvailability.bookSlot(booking2);
        
        // Test various queries
        assertTrue(dayAvailability.isAvailable(new TimeSlot(LocalTime.of(10, 30), LocalTime.of(11, 30))));
        assertTrue(dayAvailability.isAvailable(new TimeSlot(LocalTime.of(16, 30), LocalTime.of(17, 30))));
        assertFalse(dayAvailability.isAvailable(new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 30))));
        assertFalse(dayAvailability.isAvailable(new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0))));
    }

    @Test
    public void testEdgeCaseAdjacentBookings() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(16, 0));
        TimeSlot booking1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot booking2 = new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking1);
        dayAvailability.bookSlot(booking2);
        
        assertTrue(dayAvailability.isAvailable(new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0))));
    }

    @Test
    public void testRemoveSlotWithOverlappingBookingThrowsException() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        dayAvailability.addSlot(availability);
        dayAvailability.bookSlot(booking);
        
        TimeSlot partialAvailability = new TimeSlot(LocalTime.of(11, 30), LocalTime.of(13, 0));
        dayAvailability.addSlot(partialAvailability);
        
        assertThrows(RuntimeException.class, () -> {
            dayAvailability.removeSlot(partialAvailability);
        });
    }
}
