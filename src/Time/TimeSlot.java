package Time;
import java.time.LocalTime;
import java.util.Objects;


public class TimeSlot {

    private final LocalTime start;
    private final LocalTime end;

    public TimeSlot(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start must be before end");
        }

        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    // Check if two slots overlap
    public boolean overlaps(TimeSlot other) {
        return this.start.isBefore(other.end) &&
                this.end.isAfter(other.start);
    }

    // Check if this slot fully contains another
    public boolean contains(TimeSlot other) {
        return !this.start.isAfter(other.start) &&
                !this.end.isBefore(other.end);
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot that = (TimeSlot) o;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
