package booking;

import Time.TimeSlot;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Booking {

    private final int bookingId;
    private final int stadiumId;
    private final int organizerId;

    private final DayOfWeek day;
    private final TimeSlot slot;

    private final int minPlayers;

    private final Set<Integer> invited;
    private final Set<Integer> accepted;
    private final Set<Integer> declined;

    private BookingStatus status;

    public Booking(int bookingId, int stadiumId, int organizerId,
                   DayOfWeek day, TimeSlot slot, int minPlayers) {

        if (bookingId <= 0) throw new IllegalArgumentException("bookingId must be positive !");
        if (stadiumId <= 0) throw new IllegalArgumentException("stadiumId must be positive !");
        if (organizerId <= 0) throw new IllegalArgumentException("organizerId must be positive !");
        if (day == null) throw new IllegalArgumentException("day cannot be null !");
        if (slot == null) throw new IllegalArgumentException("slot cannot be null !");
        if (minPlayers <= 0) throw new IllegalArgumentException("minPlayers must be positive !");

        this.bookingId = bookingId;
        this.stadiumId = stadiumId;
        this.organizerId = organizerId;
        this.day = day;
        this.slot = slot;
        this.minPlayers = minPlayers;

        this.invited = new HashSet<>();
        this.accepted = new HashSet<>();
        this.declined = new HashSet<>();

        this.status = BookingStatus.PENDING;
    }

    public int getBookingId() { return bookingId; }

    public int getStadiumId() { return stadiumId; }

    public int getOrganizerId() { return organizerId; }

    public DayOfWeek getDay() { return day; }

    public TimeSlot getSlot() { return slot; }

    public int getMinPlayers() { return minPlayers; }

    public BookingStatus getStatus() { return status; }

    public Set<Integer> getInvited() { return Collections.unmodifiableSet(invited); }

    public Set<Integer> getAccepted() { return Collections.unmodifiableSet(accepted); }

    public Set<Integer> getDeclined() { return Collections.unmodifiableSet(declined); }

    public boolean isPending() { return status == BookingStatus.PENDING; }

    public boolean isConfirmed() { return status == BookingStatus.CONFIRMED; }

    public boolean isCancelled() { return status == BookingStatus.CANCELLED; }

    public int acceptedCount() { return accepted.size(); }

    public void invitePlayer(int playerId) {

        if (status != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending !");
        }

        if (playerId <= 0) {
            throw new IllegalArgumentException("Invalid player id !");
        }

        if (playerId == organizerId) {
            throw new RuntimeException("Organizer cannot be invited !");
        }

        if (invited.contains(playerId) || accepted.contains(playerId) || declined.contains(playerId)) {
            throw new RuntimeException("Player already has a state in this booking !");
        }

        invited.add(playerId);
    }

    public void acceptInvite(int playerId) {

        if (status != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending !");
        }

        if (!invited.contains(playerId)) {
            throw new RuntimeException("Player was not invited !");
        }

        invited.remove(playerId);
        declined.remove(playerId);
        accepted.add(playerId);
    }

    public void declineInvite(int playerId) {

        if (status != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending !");
        }

        if (!invited.contains(playerId)) {
            throw new RuntimeException("Player was not invited !");
        }

        invited.remove(playerId);
        accepted.remove(playerId);
        declined.add(playerId);
    }

    public void confirm() {

        if (status != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending !");
        }

        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {

        if (status == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled !");
        }

        this.status = BookingStatus.CANCELLED;
    }
}
