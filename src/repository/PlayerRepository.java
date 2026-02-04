package repository;
import USER.Player;

import java.util.*;

public class PlayerRepository {

    private final Map<Integer,Player> map;

    PlayerRepository()
    {
        map = new HashMap<>();
    }

    public void addPlayer(Player p)
    {
        if(map.containsKey(p.getId()))
        {
            throw new RuntimeException("Player already exists !");
        }
        map.put(p.getId(), p);
    }

    public void removePlayer(int id)
    {
        if(!map.containsKey(id))
        {
            throw new RuntimeException("The player doesn't exist !");
        }
        map.remove(id);
    }

    public Player getPlayer(int id)
    {
        if(!map.containsKey(id))
        {
            throw new RuntimeException("The player doesn't exist !");
        }
        return map.get(id);
    }

    public List<Player> getAll()
    {
        List<Player> res = new ArrayList<>();

        for(Player it : map.values())
        {
            res.add(it);
        }
        return res;
    }

    public boolean contains(int id)
    {
        return map.containsKey(id);
    }

}
