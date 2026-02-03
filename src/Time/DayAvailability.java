package Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DayAvailability {

    private final List<TimeSlot> slots;

    public DayAvailability() {
        this.slots = new ArrayList<>();
    }

    // Add slot only if it doesn't overlap existing ones, keep sorted
    public boolean addSlot(TimeSlot newSlot) {

        for (TimeSlot s : slots) {
            if (s.overlaps(newSlot)) {
                return false; // overlap -> reject
            }
        }

        slots.add(newSlot);
        Collections.sort(slots, (a, b) -> a.getStart().compareTo(b.getStart()));
        return true;
    }

    // True if any existing slot fully contains the requested slot
    public boolean isAvailable(TimeSlot requested) {
        for (TimeSlot s : slots) {
            if (s.contains(requested)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeSlot(TimeSlot toRemove)
    {
        for(TimeSlot it : slots)
        {
            if(it == toRemove)
            {
                slots.remove(toRemove);
                return true;
            }
        }
        return false;
    }

    public List<TimeSlot> getSlots() {
        return slots;
    }
}