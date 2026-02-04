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

        if (id <= 0) throw new IllegalArgumentException("id must be positive");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
        if (town == null || town.isBlank()) throw new IllegalArgumentException("town cannot be null or blank");

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

    public String getTown() {

        return this.town;
    }
}