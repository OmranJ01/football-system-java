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

    public List<Integer> getAll()
    {
        List<Integer> res = new ArrayList<>();

        for(Integer it : map.keySet())
        {
            res.add(it);
        }
        return res;
    }

}
