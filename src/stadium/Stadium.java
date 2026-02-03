
package stadium;
import Time.AvailabilitySchedule;

public class Stadium {

    private final int id;
    private final String name;
    private final String town;

    private  int ownerId;          // owner user id
    private  int pricePerHour;
    private  int capacity;

    private final AvailabilitySchedule schedule;

    public Stadium(int id, String name, String town, int ownerId, int pricePerHour, int capacity) {
        this.id = id;
        this.name = name;
        this.town = town;
        this.ownerId = ownerId;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.schedule = new AvailabilitySchedule(); // just like you did in User
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getTown() { return town; }

    public int getOwnerId() { return ownerId; }

    public int getPricePerHour() { return pricePerHour; }

    public int getCapacity() { return capacity; }

    public AvailabilitySchedule getSchedule() { return schedule; }

}
