import USER.StadiumOwner;
import stadium.Stadium;
import Time.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class StadiumTest {

    private StadiumOwner owner;

    @BeforeEach
    public void setUp() {
        owner = new StadiumOwner(1, "John Owner", "New York");
    }

    @Test
    public void testStadiumCreationWithValidData() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertEquals(1, stadium.getId());
        assertEquals("Central Stadium", stadium.getName());
        assertEquals("New York", stadium.getTown());
        assertEquals(owner, stadium.getOwner());
        assertEquals(100, stadium.getPricePerHour());
        assertEquals(50, stadium.getCapacity());
        assertNotNull(stadium.getSchedule());
    }

    @Test
    public void testUpdateCapacityWithValidValue() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        stadium.updateCapacity(100);
        assertEquals(100, stadium.getCapacity());
    }

    @Test
    public void testUpdateCapacityWithZeroThrowsException() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stadium.updateCapacity(0);
        });
    }

    @Test
    public void testUpdateCapacityWithNegativeThrowsException() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stadium.updateCapacity(-10);
        });
    }

    @Test
    public void testUpdatePriceWithValidValue() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        stadium.updatePrice(150);
        assertEquals(150, stadium.getPricePerHour());
    }

    @Test
    public void testUpdatePriceWithZeroThrowsException() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stadium.updatePrice(0);
        });
    }

    @Test
    public void testUpdatePriceWithNegativeThrowsException() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stadium.updatePrice(-50);
        });
    }

    @Test
    public void testUpdateOwnerSuccessfully() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        owner.getStadiums().add(stadium);
        
        StadiumOwner newOwner = new StadiumOwner(2, "New Owner", "New York");
        
        stadium.updateOwner(newOwner);
        
        assertEquals(newOwner, stadium.getOwner());
        assertFalse(owner.getStadiums().contains(stadium));
        assertTrue(newOwner.getStadiums().contains(stadium));
    }

    @Test
    public void testUpdateOwnerWithSameOwnerThrowsException() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stadium.updateOwner(owner);
        });
    }

    @Test
    public void testUpdateOwnerWithNullThrowsException() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        assertThrows(IllegalArgumentException.class, () -> {
            stadium.updateOwner(null);
        });
    }

    @Test
    public void testStadiumScheduleCanAddSlots() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        TimeSlot slot = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(12, 0));
        
        assertTrue(stadium.getSchedule().addSlot(DayOfWeek.MONDAY, slot));
        assertTrue(stadium.getSchedule().isAvailable(DayOfWeek.MONDAY, slot));
    }

    @Test
    public void testStadiumScheduleCanBookSlots() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        TimeSlot availability = new TimeSlot(LocalTime.of(10, 0), LocalTime.of(14, 0));
        TimeSlot booking = new TimeSlot(LocalTime.of(11, 0), LocalTime.of(12, 0));
        
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, availability);
        stadium.getSchedule().bookSlot(DayOfWeek.MONDAY, booking);
        
        assertFalse(stadium.getSchedule().isAvailable(DayOfWeek.MONDAY, booking));
    }

    @Test
    public void testStadiumWithVeryHighPrice() {
        Stadium stadium = new Stadium(1, "Luxury Stadium", "New York", owner, 10000, 100);
        
        assertEquals(10000, stadium.getPricePerHour());
    }

    @Test
    public void testStadiumWithVeryLargeCapacity() {
        Stadium stadium = new Stadium(1, "Mega Stadium", "New York", owner, 100, 100000);
        
        assertEquals(100000, stadium.getCapacity());
    }

    @Test
    public void testStadiumWithMinimalCapacity() {
        Stadium stadium = new Stadium(1, "Mini Stadium", "New York", owner, 50, 1);
        
        assertEquals(1, stadium.getCapacity());
    }

    @Test
    public void testStadiumWithMinimalPrice() {
        Stadium stadium = new Stadium(1, "Budget Stadium", "New York", owner, 1, 50);
        
        assertEquals(1, stadium.getPricePerHour());
    }

    @Test
    public void testUpdateCapacityMultipleTimes() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        stadium.updateCapacity(60);
        assertEquals(60, stadium.getCapacity());
        
        stadium.updateCapacity(70);
        assertEquals(70, stadium.getCapacity());
        
        stadium.updateCapacity(40);
        assertEquals(40, stadium.getCapacity());
    }

    @Test
    public void testUpdatePriceMultipleTimes() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        stadium.updatePrice(120);
        assertEquals(120, stadium.getPricePerHour());
        
        stadium.updatePrice(90);
        assertEquals(90, stadium.getPricePerHour());
        
        stadium.updatePrice(150);
        assertEquals(150, stadium.getPricePerHour());
    }

    @Test
    public void testStadiumWithSpecialCharactersInName() {
        Stadium stadium = new Stadium(1, "São Paulo's #1 Stadium", "São Paulo", owner, 100, 50);
        
        assertEquals("São Paulo's #1 Stadium", stadium.getName());
        assertEquals("São Paulo", stadium.getTown());
    }

    @Test
    public void testStadiumFieldsAreImmutableWhereAppropriate() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        // These should not change
        assertEquals(1, stadium.getId());
        assertEquals("Central Stadium", stadium.getName());
        assertEquals("New York", stadium.getTown());
    }

    @Test
    public void testMultipleStadiumsWithSameOwner() {
        Stadium stadium1 = new Stadium(1, "Stadium 1", "New York", owner, 100, 50);
        Stadium stadium2 = new Stadium(2, "Stadium 2", "New York", owner, 120, 60);
        Stadium stadium3 = new Stadium(3, "Stadium 3", "New York", owner, 90, 40);
        
        owner.getStadiums().add(stadium1);
        owner.getStadiums().add(stadium2);
        owner.getStadiums().add(stadium3);
        
        assertEquals(owner, stadium1.getOwner());
        assertEquals(owner, stadium2.getOwner());
        assertEquals(owner, stadium3.getOwner());
        assertEquals(3, owner.getStadiums().size());
    }

    @Test
    public void testUpdateOwnerRemovesFromOldOwner() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        owner.getStadiums().add(stadium);
        
        StadiumOwner newOwner = new StadiumOwner(2, "New Owner", "Los Angeles");
        
        assertEquals(1, owner.getStadiums().size());
        assertEquals(0, newOwner.getStadiums().size());
        
        stadium.updateOwner(newOwner);
        
        assertEquals(0, owner.getStadiums().size());
        assertEquals(1, newOwner.getStadiums().size());
    }

    @Test
    public void testUpdateOwnerAddsToNewOwner() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        owner.getStadiums().add(stadium);
        
        StadiumOwner newOwner = new StadiumOwner(2, "New Owner", "Los Angeles");
        
        stadium.updateOwner(newOwner);
        
        assertTrue(newOwner.getStadiums().contains(stadium));
        assertEquals(stadium.getOwner(), newOwner);
    }

    @Test
    public void testComplexScheduleScenario() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        // Add availability for multiple days
        TimeSlot morningSlot = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(12, 0));
        TimeSlot afternoonSlot = new TimeSlot(LocalTime.of(14, 0), LocalTime.of(18, 0));
        
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, morningSlot);
        stadium.getSchedule().addSlot(DayOfWeek.MONDAY, afternoonSlot);
        stadium.getSchedule().addSlot(DayOfWeek.WEDNESDAY, morningSlot);
        stadium.getSchedule().addSlot(DayOfWeek.FRIDAY, afternoonSlot);
        
        // Book some slots
        TimeSlot mondayBooking = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0));
        stadium.getSchedule().bookSlot(DayOfWeek.MONDAY, mondayBooking);
        
        // Verify availability
        assertFalse(stadium.getSchedule().isAvailable(DayOfWeek.MONDAY, mondayBooking));
        assertTrue(stadium.getSchedule().isAvailable(DayOfWeek.MONDAY, 
            new TimeSlot(LocalTime.of(10, 30), LocalTime.of(11, 30))));
        assertTrue(stadium.getSchedule().isAvailable(DayOfWeek.WEDNESDAY, morningSlot));
    }

    @Test
    public void testOwnershipChain() {
        StadiumOwner owner1 = new StadiumOwner(1, "Owner 1", "New York");
        StadiumOwner owner2 = new StadiumOwner(2, "Owner 2", "Los Angeles");
        StadiumOwner owner3 = new StadiumOwner(3, "Owner 3", "Chicago");
        
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner1, 100, 50);
        owner1.getStadiums().add(stadium);
        
        // Transfer through multiple owners
        stadium.updateOwner(owner2);
        assertEquals(owner2, stadium.getOwner());
        
        stadium.updateOwner(owner3);
        assertEquals(owner3, stadium.getOwner());
        
        assertEquals(0, owner1.getStadiums().size());
        assertEquals(0, owner2.getStadiums().size());
        assertEquals(1, owner3.getStadiums().size());
    }

    @Test
    public void testIncreaseAndDecreaseCapacity() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        stadium.updateCapacity(100);
        assertEquals(100, stadium.getCapacity());
        
        stadium.updateCapacity(30);
        assertEquals(30, stadium.getCapacity());
        
        stadium.updateCapacity(1000);
        assertEquals(1000, stadium.getCapacity());
    }

    @Test
    public void testIncreaseAndDecreasePrice() {
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        stadium.updatePrice(200);
        assertEquals(200, stadium.getPricePerHour());
        
        stadium.updatePrice(50);
        assertEquals(50, stadium.getPricePerHour());
        
        stadium.updatePrice(500);
        assertEquals(500, stadium.getPricePerHour());
    }
}
