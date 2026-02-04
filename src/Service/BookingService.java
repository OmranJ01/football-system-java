package Service;

import Time.TimeSlot;
import booking.Booking;
import booking.BookingStatus;
import repository.BookingRepository;
import repository.PlayerRepository;
import repository.StadiumRepository;
import stadium.Stadium;

import java.time.DayOfWeek;

public class BookingService {

    private final PlayerRepository playerRepository;
    private final StadiumRepository stadiumRepository;
    private final BookingRepository bookingRepository;

    private int nextBookingId;

    public BookingService(PlayerRepository playerRepository,
                          StadiumRepository stadiumRepository,
                          BookingRepository bookingRepository)
    {
        if (playerRepository == null || stadiumRepository == null || bookingRepository == null) {
            throw new IllegalArgumentException("Repositories cannot be null !");
        }

        this.playerRepository = playerRepository;
        this.stadiumRepository = stadiumRepository;
        this.bookingRepository = bookingRepository;
        this.nextBookingId = 1;
    }

    public Booking createBooking(int organizerId, int stadiumId, DayOfWeek day, TimeSlot slot, int minPlayers)
    {
        if (organizerId <= 0 || stadiumId <= 0) {
            throw new IllegalArgumentException("Invalid ids !");
        }

        if (day == null || slot == null) {
            throw new IllegalArgumentException("day/slot cannot be null !");
        }

        if (minPlayers <= 0) {
            throw new IllegalArgumentException("minPlayers must be positive !");
        }

        if (!playerRepository.contains(organizerId)) {
            throw new RuntimeException("Organizer doesn't exist !");
        }

        if (!stadiumRepository.contains(stadiumId)) {
            throw new RuntimeException("Stadium doesn't exist !");
        }

        Stadium stadium = stadiumRepository.getStadium(stadiumId);

        if (minPlayers > stadium.getCapacity()) {
            throw new RuntimeException("minPlayers exceeds stadium capacity !");
        }

        if (!stadium.getSchedule().isAvailable(day, slot)) {
            throw new RuntimeException("Stadium is not available in this slot !");
        }

        int id = nextBookingId++;
        Booking booking = new Booking(id, stadiumId, organizerId, day, slot, minPlayers);

        bookingRepository.addBooking(booking);
        return booking;
    }

    public void invitePlayer(int bookingId, int organizerId, int playerId)
    {
        Booking booking = bookingRepository.getBooking(bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending !");
        }

        if (booking.getOrganizerId() != organizerId) {
            throw new RuntimeException("Only organizer can invite !");
        }

        if (!playerRepository.contains(playerId)) {
            throw new RuntimeException("Player doesn't exist !");
        }

        booking.invitePlayer(playerId);
    }

    public void respondToInvite(int bookingId, int playerId, boolean accept)
    {
        Booking booking = bookingRepository.getBooking(bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending !");
        }

        if (accept) {
            booking.acceptInvite(playerId);

            if (booking.acceptedCount() >= booking.getMinPlayers()) {

                Stadium stadium = stadiumRepository.getStadium(booking.getStadiumId());

                // block the stadium slot now
                stadium.getSchedule().bookSlot(booking.getDay(), booking.getSlot());

                booking.confirm();
            }

        } else {
            booking.declineInvite(playerId);
        }
    }

    public void cancelBooking(int bookingId, int organizerId)
    {
        Booking booking = bookingRepository.getBooking(bookingId);

        if (booking.getOrganizerId() != organizerId) {
            throw new RuntimeException("Only organizer can cancel !");
        }

        booking.cancel();
    }
}
