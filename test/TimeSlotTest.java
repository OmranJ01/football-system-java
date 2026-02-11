import Time.TimeSlot;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class TimeSlotTest {

    @Test
    public void testValidTimeSlotCreation() {
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(12, 0);
        TimeSlot slot = new TimeSlot(start, end);
        
        assertEquals(start, slot.getStart());
        assertEquals(end, slot.getEnd());
    }

    @Test
    public void testNullStartThrowsException() {
        LocalTime end = LocalTime.of(12, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(null, end);
        });
    }

    @Test
    public void testNullEndThrowsException() {
        LocalTime start = LocalTime.of(10, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(start, null);
        });
    }

    @Test
    public void testBothNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(null, null);
        });
    }

    @Test
    public void testStartAfterEndThrowsException() {
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(12, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(start, end);
        });
    }

    @Test
    public void testStartEqualsEndThrowsException() {
        LocalTime time = LocalTime.of(12, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot(time, time);
        });
    }

    @Test
    public void testOverlapsWithFullOverlap() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertTrue(slot1.overlaps(slot2));
        assertTrue(slot2.overlaps(slot1));
    }

    @Test
    public void testOverlapsWithPartialOverlap() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 30));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertTrue(slot1.overlaps(slot2));
        assertTrue(slot2.overlaps(slot1));
    }

    @Test
    public void testNoOverlapWithAdjacentSlots() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(12, 0), LocalTime.of(14, 0));
        
        assertFalse(slot1.overlaps(slot2));
        assertFalse(slot2.overlaps(slot1));
    }

    @Test
    public void testNoOverlapWithSeparateSlots() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 0));
        
        assertFalse(slot1.overlaps(slot2));
        assertFalse(slot2.overlaps(slot1));
    }

    @Test
    public void testContainsWhenFullyContained() {
        TimeSlot outer = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot inner = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertTrue(outer.contains(inner));
        assertFalse(inner.contains(outer));
    }

    @Test
    public void testContainsWhenExactMatch() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertTrue(slot1.contains(slot2));
        assertTrue(slot2.contains(slot1));
    }

    @Test
    public void testNotContainedWhenPartialOverlap() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertFalse(slot1.contains(slot2));
        assertFalse(slot2.contains(slot1));
    }

    @Test
    public void testNotContainedWhenNoOverlap() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 0));
        
        assertFalse(slot1.contains(slot2));
        assertFalse(slot2.contains(slot1));
    }

    @Test
    public void testEqualsWithSameSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertEquals(slot, slot);
    }

    @Test
    public void testEqualsWithIdenticalSlots() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertEquals(slot1, slot2);
        assertEquals(slot2, slot1);
    }

    @Test
    public void testNotEqualsWithDifferentSlots() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(13, 0));
        
        assertNotEquals(slot1, slot2);
    }

    @Test
    public void testNotEqualsWithNull() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertNotEquals(null, slot);
    }

    @Test
    public void testNotEqualsWithDifferentType() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertNotEquals(slot, "Not a TimeSlot");
    }

    @Test
    public void testHashCodeConsistency() {
        TimeSlot slot1 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeSlot slot2 = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertEquals(slot1.hashCode(), slot2.hashCode());
    }

    @Test
    public void testToString() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        String result = slot.toString();
        
        assertTrue(result.contains("10:00"));
        assertTrue(result.contains("12:00"));
    }

    @Test
    public void testMidnightTimeSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.MIDNIGHT, LocalTime.of(1, 0));
        
        assertEquals(LocalTime.MIDNIGHT, slot.getStart());
        assertEquals(LocalTime.of(1, 0), slot.getEnd());
    }

    @Test
    public void testLateNightTimeSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.of(23, 0), LocalTime.of(23, 59));
        
        assertEquals(LocalTime.of(23, 0), slot.getStart());
        assertEquals(LocalTime.of(23, 59), slot.getEnd());
    }

    @Test
    public void testVeryShortTimeSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(10, 1));
        
        assertEquals(LocalTime.of(10, 0), slot.getStart());
        assertEquals(LocalTime.of(10, 1), slot.getEnd());
    }

    @Test
    public void testFullDayTimeSlot() {
        TimeSlot slot = new TimeSlot(LocalTime.MIDNIGHT, LocalTime.of(23, 59, 59));
        
        assertEquals(LocalTime.MIDNIGHT, slot.getStart());
        assertEquals(LocalTime.of(23, 59, 59), slot.getEnd());
    }
}
