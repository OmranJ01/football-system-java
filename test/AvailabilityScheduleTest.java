import Time.AvailabilitySchedule;
import Time.DayAvailability;
import Time.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class AvailabilityScheduleTest {

    private AvailabilitySchedule schedule;

    @BeforeEach
    public void setUp() {
        schedule = new AvailabilitySchedule();
    }

    @Test
    public void testScheduleInitializedWithAllDays() {
        for (DayOfWeek day : DayOfWeek.values()) {
            assertNotNull(schedule.getDayAvailability(day));
        }
    }

    @Test
    public void testAddSlotToMonday() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertTrue(schedule.addSlot(DayOfWeek.MONDAY, slot));
        assertEquals(1, schedule.getDayAvailability(DayOfWeek.MONDAY).getSlots().size());
    }

    @Test
    public void testAddSlotToAllDays() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        for (DayOfWeek day : DayOfWeek.values()) {
            assertTrue(schedule.addSlot(day, slot));
        }
        
        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(1, schedule.getDayAvailability(day).getSlots().size());
        }
    }

    @Test
    public void testAddOverlappingSlotsOnSameDay() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertTrue(schedule.addSlot(DayOfWeek.TUESDAY, slot1));
        assertFalse(schedule.addSlot(DayOfWeek.TUESDAY, slot2));
    }

    @Test
    public void testAddSameSlotsOnDifferentDays() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertTrue(schedule.addSlot(DayOfWeek.MONDAY, slot));
        assertTrue(schedule.addSlot(DayOfWeek.TUESDAY, slot));
        
        assertEquals(1, schedule.getDayAvailability(DayOfWeek.MONDAY).getSlots().size());
        assertEquals(1, schedule.getDayAvailability(DayOfWeek.TUESDAY).getSlots().size());
    }

    @Test
    public void testRemoveSlotSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.WEDNESDAY, slot);
        assertTrue(schedule.removeSlot(DayOfWeek.WEDNESDAY, slot));
        assertEquals(0, schedule.getDayAvailability(DayOfWeek.WEDNESDAY).getSlots().size());
    }

    @Test
    public void testRemoveNonExistentSlotReturnsFalse() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0));
        
        schedule.addSlot(DayOfWeek.THURSDAY, slot1);
        assertFalse(schedule.removeSlot(DayOfWeek.THURSDAY, slot2));
    }

    @Test
    public void testRemoveSlotFromWrongDay() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.FRIDAY, slot);
        assertFalse(schedule.removeSlot(DayOfWeek.SATURDAY, slot));
    }

    @Test
    public void testIsAvailableWhenSlotExists() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.SUNDAY, availability);
        
        assertTrue(schedule.isAvailable(DayOfWeek.SUNDAY, requested));
    }

    @Test
    public void testIsNotAvailableWhenNoSlot() {
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, requested));
    }

    @Test
    public void testIsNotAvailableOnWrongDay() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot requested = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.MONDAY, availability);
        
        assertFalse(schedule.isAvailable(DayOfWeek.TUESDAY, requested));
    }

    @Test
    public void testBookSlotSuccessfully() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.WEDNESDAY, availability);
        schedule.bookSlot(DayOfWeek.WEDNESDAY, booking);
        
        assertFalse(schedule.isAvailable(DayOfWeek.WEDNESDAY, booking));
        assertEquals(1, schedule.getDayAvailability(DayOfWeek.WEDNESDAY).getBookedSlots().size());
    }

    @Test
    public void testBookSlotWhenNotAvailableThrowsException() {
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        assertThrows(RuntimeException.class, () -> {
            schedule.bookSlot(DayOfWeek.THURSDAY, booking);
        });
    }

    @Test
    public void testBookSlotOnWrongDayThrowsException() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.FRIDAY, availability);
        
        assertThrows(RuntimeException.class, () -> {
            schedule.bookSlot(DayOfWeek.SATURDAY, booking);
        });
    }

    @Test
    public void testComplexWeekSchedule() {
        // Monday: 9-12, 14-17
        schedule.addSlot(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0)));
        schedule.addSlot(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(14, 0), LocalTime.of(17, 0)));
        
        // Wednesday: 10-18
        schedule.addSlot(DayOfWeek.WEDNESDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(18, 0)));
        
        // Friday: 8-20
        schedule.addSlot(DayOfWeek.FRIDAY, new TimeSlot(LocalTime.of(8, 0), LocalTime.of(20, 0)));
        
        // Test availability
        assertTrue(schedule.isAvailable(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0))));
        assertTrue(schedule.isAvailable(DayOfWeek.WEDNESDAY, new TimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0))));
        assertTrue(schedule.isAvailable(DayOfWeek.FRIDAY, new TimeSlot(LocalTime.of(19, 0), LocalTime.of(20, 0))));
        
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.TUESDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0))));
    }

    @Test
    public void testBookMultipleSlotsThroughoutWeek() {
        // Set up availability for the week
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.addSlot(day, new TimeSlot(LocalTime.of(9, 0), LocalTime.of(18, 0)));
        }
        
        // Book slots on different days
        schedule.bookSlot(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        schedule.bookSlot(DayOfWeek.WEDNESDAY, new TimeSlot(LocalTime.of(14, 0), LocalTime.of(15, 0)));
        schedule.bookSlot(DayOfWeek.FRIDAY, new TimeSlot(LocalTime.of(16, 0), LocalTime.of(17, 0)));
        
        // Verify bookings
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.WEDNESDAY, new TimeSlot(LocalTime.of(14, 0), LocalTime.of(15, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.FRIDAY, new TimeSlot(LocalTime.of(16, 0), LocalTime.of(17, 0))));
        
        // Verify other slots still available
        assertTrue(schedule.isAvailable(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(12, 0), LocalTime.of(13, 0))));
        assertTrue(schedule.isAvailable(DayOfWeek.TUESDAY, new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0))));
    }

    @Test
    public void testGetDayAvailabilityReturnsCorrectInstance() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.MONDAY, slot);
        
        DayAvailability mondayAvail = schedule.getDayAvailability(DayOfWeek.MONDAY);
        assertEquals(1, mondayAvail.getSlots().size());
        assertEquals(slot, mondayAvail.getSlots().get(0));
    }

    @Test
    public void testWeekendVsWeekdaySchedule() {
        TimeSlot weekdaySlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(17, 0));
        TimeSlot weekendSlot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(20, 0));
        
        // Add weekday schedule
        schedule.addSlot(DayOfWeek.MONDAY, weekdaySlot);
        schedule.addSlot(DayOfWeek.TUESDAY, weekdaySlot);
        schedule.addSlot(DayOfWeek.WEDNESDAY, weekdaySlot);
        schedule.addSlot(DayOfWeek.THURSDAY, weekdaySlot);
        schedule.addSlot(DayOfWeek.FRIDAY, weekdaySlot);
        
        // Add weekend schedule
        schedule.addSlot(DayOfWeek.SATURDAY, weekendSlot);
        schedule.addSlot(DayOfWeek.SUNDAY, weekendSlot);
        
        // Test availability
        assertTrue(schedule.isAvailable(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, new TimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 0))));
        
        assertTrue(schedule.isAvailable(DayOfWeek.SATURDAY, new TimeSlot(LocalTime.of(19, 0), LocalTime.of(20, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.SATURDAY, new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0))));
    }

    @Test
    public void testRemoveSlotWithBookingThrowsException() {
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.MONDAY, availability);
        schedule.bookSlot(DayOfWeek.MONDAY, booking);
        
        assertThrows(RuntimeException.class, () -> {
            schedule.removeSlot(DayOfWeek.MONDAY, availability);
        });
    }

    @Test
    public void testIndependenceOfDays() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        schedule.addSlot(DayOfWeek.MONDAY, slot);
        schedule.bookSlot(DayOfWeek.MONDAY, slot);
        
        // Tuesday should still be available with same time slot
        schedule.addSlot(DayOfWeek.TUESDAY, slot);
        assertTrue(schedule.isAvailable(DayOfWeek.TUESDAY, slot));
    }

    @Test
    public void testFullyBookedDay() {
        TimeSlot availability = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(17, 0));
        schedule.addSlot(DayOfWeek.MONDAY, availability);
        
        // Book the entire day
        schedule.bookSlot(DayOfWeek.MONDAY, availability);
        
        // Verify nothing is available
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(12, 0), LocalTime.of(13, 0))));
        assertFalse(schedule.isAvailable(DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(16, 0), LocalTime.of(17, 0))));
    }
}
