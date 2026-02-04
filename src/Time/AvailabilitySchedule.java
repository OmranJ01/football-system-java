package Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;


public class AvailabilitySchedule {

    private  Map<DayOfWeek, DayAvailability> days;

    public AvailabilitySchedule()
    {
        this.days = new HashMap<>();

        for(DayOfWeek d : DayOfWeek.values())
        {
            days.put(d,new DayAvailability());
        }
    }

    public boolean addSlot(DayOfWeek day,TimeSlot inp)
    {
        return days.get(day).addSlot(inp);
    }
    public void bookSlot(DayOfWeek day, TimeSlot slot) {
        days.get(day).bookSlot(slot);
    }

    public boolean removeSlot(DayOfWeek day,TimeSlot inp)
    {
        return days.get(day).removeSlot(inp);
    }

    public boolean isAvailable(DayOfWeek day,TimeSlot inp)
    {
        return days.get(day).isAvailable(inp);
    }

    public DayAvailability detDayAvailability(DayOfWeek day)
    {
        return days.get(day);
    }
}
