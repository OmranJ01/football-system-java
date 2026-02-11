import USER.StadiumOwner;
import stadium.Stadium;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StadiumOwnerTest {

    @Test
    public void testStadiumOwnerCreationWithValidData() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        
        assertEquals(1, owner.getId());
        assertEquals("John Stadium", owner.getName());
        assertEquals("New York", owner.getTown());
        assertNotNull(owner.getSchedule());
        assertNotNull(owner.getStadiums());
    }

    @Test
    public void testStadiumOwnerWithInvalidIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(0, "John Stadium", "New York");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(-1, "John Stadium", "New York");
        });
    }

    @Test
    public void testStadiumOwnerWithNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(1, null, "New York");
        });
    }

    @Test
    public void testStadiumOwnerWithBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(1, "", "New York");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(1, "   ", "New York");
        });
    }

    @Test
    public void testStadiumOwnerWithNullTownThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(1, "John Stadium", null);
        });
    }

    @Test
    public void testStadiumOwnerWithBlankTownThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(1, "John Stadium", "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new StadiumOwner(1, "John Stadium", "   ");
        });
    }

    @Test
    public void testStadiumsSetInitiallyEmpty() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        
        assertTrue(owner.getStadiums().isEmpty());
    }

    @Test
    public void testAddStadiumToOwner() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        owner.getStadiums().add(stadium);
        
        assertEquals(1, owner.getStadiums().size());
        assertTrue(owner.getStadiums().contains(stadium));
    }

    @Test
    public void testAddMultipleStadiums() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        Stadium stadium1 = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        Stadium stadium2 = new Stadium(2, "North Stadium", "New York", owner, 120, 60);
        Stadium stadium3 = new Stadium(3, "South Stadium", "New York", owner, 90, 40);
        
        owner.getStadiums().add(stadium1);
        owner.getStadiums().add(stadium2);
        owner.getStadiums().add(stadium3);
        
        assertEquals(3, owner.getStadiums().size());
        assertTrue(owner.getStadiums().contains(stadium1));
        assertTrue(owner.getStadiums().contains(stadium2));
        assertTrue(owner.getStadiums().contains(stadium3));
    }

    @Test
    public void testRemoveStadiumFromOwner() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        owner.getStadiums().add(stadium);
        owner.getStadiums().remove(stadium);
        
        assertTrue(owner.getStadiums().isEmpty());
        assertFalse(owner.getStadiums().contains(stadium));
    }

    @Test
    public void testStadiumsDontContainDuplicates() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner, 100, 50);
        
        owner.getStadiums().add(stadium);
        owner.getStadiums().add(stadium);
        
        assertEquals(1, owner.getStadiums().size());
    }

    @Test
    public void testUpdateTown() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        owner.updateTown("Los Angeles");
        
        assertEquals("Los Angeles", owner.getTown());
    }

    @Test
    public void testStadiumOwnerWithSpecialCharactersInName() {
        StadiumOwner owner = new StadiumOwner(1, "José O'Brien", "São Paulo");
        
        assertEquals("José O'Brien", owner.getName());
        assertEquals("São Paulo", owner.getTown());
    }

    @Test
    public void testStadiumOwnerWithVeryLargeId() {
        StadiumOwner owner = new StadiumOwner(Integer.MAX_VALUE, "John Stadium", "New York");
        
        assertEquals(Integer.MAX_VALUE, owner.getId());
    }

    @Test
    public void testMultipleStadiumsInDifferentTowns() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        Stadium stadium1 = new Stadium(1, "NY Stadium", "New York", owner, 100, 50);
        Stadium stadium2 = new Stadium(2, "LA Stadium", "Los Angeles", owner, 120, 60);
        Stadium stadium3 = new Stadium(3, "Chicago Stadium", "Chicago", owner, 90, 40);
        
        owner.getStadiums().add(stadium1);
        owner.getStadiums().add(stadium2);
        owner.getStadiums().add(stadium3);
        
        assertEquals(3, owner.getStadiums().size());
        
        // Check that stadiums are in different towns
        assertEquals("New York", stadium1.getTown());
        assertEquals("Los Angeles", stadium2.getTown());
        assertEquals("Chicago", stadium3.getTown());
    }

    @Test
    public void testOwnerCanOwnManyStadiums() {
        StadiumOwner owner = new StadiumOwner(1, "Big Owner", "New York");
        
        for (int i = 1; i <= 20; i++) {
            Stadium stadium = new Stadium(i, "Stadium " + i, "New York", owner, 100, 50);
            owner.getStadiums().add(stadium);
        }
        
        assertEquals(20, owner.getStadiums().size());
    }

    @Test
    public void testUpdateTownMultipleTimes() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        
        owner.updateTown("Los Angeles");
        assertEquals("Los Angeles", owner.getTown());
        
        owner.updateTown("Chicago");
        assertEquals("Chicago", owner.getTown());
        
        owner.updateTown("Miami");
        assertEquals("Miami", owner.getTown());
    }

    @Test
    public void testStadiumOwnerSchedule() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        
        // Owner should have a schedule (inherited from User)
        assertNotNull(owner.getSchedule());
    }

    @Test
    public void testStadiumOwnerInheritedMethods() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        
        // Test inherited methods
        assertEquals(1, owner.getId());
        assertEquals("John Stadium", owner.getName());
        assertEquals("New York", owner.getTown());
        
        owner.updateTown("Boston");
        assertEquals("Boston", owner.getTown());
    }

    @Test
    public void testClearAllStadiums() {
        StadiumOwner owner = new StadiumOwner(1, "John Stadium", "New York");
        
        for (int i = 1; i <= 5; i++) {
            Stadium stadium = new Stadium(i, "Stadium " + i, "New York", owner, 100, 50);
            owner.getStadiums().add(stadium);
        }
        
        assertEquals(5, owner.getStadiums().size());
        
        owner.getStadiums().clear();
        
        assertTrue(owner.getStadiums().isEmpty());
    }

    @Test
    public void testStadiumOwnerWithLongCompanyName() {
        String longName = "International Football Stadium Management Corporation LLC";
        StadiumOwner owner = new StadiumOwner(1, longName, "New York");
        
        assertEquals(longName, owner.getName());
    }

    @Test
    public void testTransferStadiumBetweenOwners() {
        StadiumOwner owner1 = new StadiumOwner(1, "Owner 1", "New York");
        StadiumOwner owner2 = new StadiumOwner(2, "Owner 2", "New York");
        
        Stadium stadium = new Stadium(1, "Central Stadium", "New York", owner1, 100, 50);
        owner1.getStadiums().add(stadium);
        
        assertEquals(1, owner1.getStadiums().size());
        assertEquals(0, owner2.getStadiums().size());
        
        // Transfer stadium using the updateOwner method
        stadium.updateOwner(owner2);
        
        assertEquals(0, owner1.getStadiums().size());
        assertEquals(1, owner2.getStadiums().size());
        assertTrue(owner2.getStadiums().contains(stadium));
    }
}
