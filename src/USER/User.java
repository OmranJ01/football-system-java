package USER;
import Time.*;



public abstract class User
{

    private AvailabilitySchedule schedule;
    private int id;
    private String name;
    private String town;

    User(int id, String name,String town)
    {
        this.id = id;
        this.name = name;
        this.town = town;
        this.schedule = new AvailabilitySchedule();
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }


    public AvailabilitySchedule getSchedule()
    {
        return this.schedule;
    }

    public void updateTown(String newTown)
    {
        this.town = newTown;
    }
}