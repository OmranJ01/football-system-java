
package stadium;
import Time.AvailabilitySchedule;
import USER.StadiumOwner;

import java.util.Set;

public class Stadium {

    private final int id;
    private final String name;
    private final String town;

    private  StadiumOwner owner;          // owner user id
    private  int pricePerHour;
    private  int capacity;

    private final AvailabilitySchedule schedule;

    public Stadium(int id, String name, String town, StadiumOwner owner, int pricePerHour, int capacity) {
        this.id = id;
        this.name = name;
        this.town = town;
        this.owner = owner;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.schedule = new AvailabilitySchedule(); // just like you did in User
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getTown() { return town; }

    public StadiumOwner getOwner() { return this.owner; }

    public int getPricePerHour() { return pricePerHour; }

    public int getCapacity() { return capacity; }

    public AvailabilitySchedule getSchedule() { return schedule; }

    public void updateCapacity(int newCapacity)
    {

        if(newCapacity <= 0)
        {
            throw new IllegalArgumentException();
        }
        this.capacity = newCapacity;
    }

    public void updatePrice(int newPrice)
    {

        if(newPrice <= 0)
        {
            throw new IllegalArgumentException();
        }
        this.pricePerHour = newPrice;
    }

    public void updateOwmer(StadiumOwner newOwner)
    {
        if(owner == newOwner || newOwner == null)
        {
            throw new IllegalArgumentException();
        }

        Set<Stadium> stadiumSet = this.owner.getStadiums();
        stadiumSet.remove(this);
        newOwner.getStadiums().add(this);
        this.owner = newOwner;
    }


}
