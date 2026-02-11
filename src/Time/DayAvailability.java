package Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayAvailability {

    private final List<TimeSlot> slots;        // availability windows (when stadium/user is free)
    private final List<TimeSlot> bookedSlots;  // bookings inside the availability windows

    public DayAvailability() {
        this.slots = new ArrayList<>();
        this.bookedSlots = new ArrayList<>();
    }

    public boolean addSlot(TimeSlot newSlot) {

        for (TimeSlot s : slots) {
            if (s.overlaps(newSlot)) {
                return false;
            }
        }

        slots.add(newSlot);
        Collections.sort(slots, (a, b) -> a.getStart().compareTo(b.getStart()));
        return true;
    }

    public boolean removeSlot(TimeSlot slot) {

        if (slot == null) {
            throw new IllegalArgumentException("slot cannot be null !");
        }

        // safety: don't allow removing availability if there is a booking inside it
        for (TimeSlot b : bookedSlots) {
            if (slot.contains(b) || b.overlaps(slot)) {
                throw new RuntimeException("Cannot remove availability that has bookings !");
            }
        }

        return slots.remove(slot); // removes only if TimeSlot equals() matches
    }


    public boolean isAvailable(TimeSlot requested) {

        boolean insideAvailability = false;
        for (TimeSlot s : slots) {
            if (s.contains(requested)) {   // you need TimeSlot.contains(...)
                insideAvailability = true;
                break;
            }
        }
        if (!insideAvailability) return false;

        for (TimeSlot b : bookedSlots) {
            if (b.overlaps(requested)) {
                return false;
            }
        }

        return true;
    }

    public void bookSlot(TimeSlot requested) {

        if (!isAvailable(requested)) {
            throw new RuntimeException("Slot is not available to book !");
        }

        bookedSlots.add(requested);
        Collections.sort(bookedSlots, (a, b) -> a.getStart().compareTo(b.getStart()));
    }

    public List<TimeSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public List<TimeSlot> getBookedSlots() {
        return Collections.unmodifiableList(bookedSlots);
    }
}
