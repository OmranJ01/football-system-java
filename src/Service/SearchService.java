package Service;

import Time.TimeSlot;
import USER.Player;
import repository.PlayerRepository;
import repository.StadiumRepository;
import stadium.Stadium;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class SearchService {

    private final PlayerRepository playerRepository;
    private final StadiumRepository stadiumRepository;

    public SearchService(PlayerRepository playerRepository, StadiumRepository stadiumRepository) {

        if (playerRepository == null || stadiumRepository == null) {
            throw new IllegalArgumentException("Repositories cannot be null !");
        }

        this.playerRepository = playerRepository;
        this.stadiumRepository = stadiumRepository;
    }

    public List<Player> searchPlayers(Set<String> towns, DayOfWeek day, TimeSlot slot) {

        if (towns == null || towns.isEmpty()) {
            throw new IllegalArgumentException("towns cannot be null or empty !");
        }

        if (day == null || slot == null) {
            throw new IllegalArgumentException("day/slot cannot be null !");
        }

        List<Player> res = new ArrayList<>();

        for (Player p : playerRepository.getAll()) {


            if (!towns.contains(p.getTown())) {
                continue;
            }

            if (!p.getSchedule().isAvailable(day, slot)) {
                continue;
            }

            res.add(p);
        }

        return res;
    }

    public List<Stadium> searchStadiums(Set<String> towns, DayOfWeek day, TimeSlot slot) {

        if (towns == null || towns.isEmpty()) {
            throw new IllegalArgumentException("towns cannot be null or empty !");
        }

        if (day == null || slot == null) {
            throw new IllegalArgumentException("day/slot cannot be null !");
        }

        List<Stadium> res = new ArrayList<>();

        for (Stadium s : stadiumRepository.getAll()) {

            if (!towns.contains(s.getTown())) {
                continue;
            }

            if (!s.getSchedule().isAvailable(day, slot)) {
                continue;
            }

            res.add(s);
        }

        return res;
    }
}
