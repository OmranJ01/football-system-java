package repository;

import booking.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRepository {

    private final Map<Integer, Booking> map;

    public BookingRepository() {
        map = new HashMap<>();
    }

    public void addBooking(Booking b) {
        if (b == null) {
            throw new IllegalArgumentException("Booking cannot be null !");
        }

        int id = b.getBookingId();
        if (map.containsKey(id)) {
            throw new IllegalArgumentException("Booking already exists !");
        }

        map.put(id, b);
    }

    public Booking getBooking(int id) {
        if (!map.containsKey(id)) {
            throw new IllegalArgumentException("Booking doesn't exist !");
        }
        return map.get(id);
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }

    public List<Booking> getAll() {
        return new ArrayList<>(map.values());
    }

    public void removeBooking(int id) {
        if (!map.containsKey(id)) {
            throw new IllegalArgumentException("Booking doesn't exist !");
        }
        map.remove(id);
    }
}
