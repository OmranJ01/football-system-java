package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import stadium.Stadium;

public class StadiumRepository {

    private final Map<Integer,Stadium> map;

    public StadiumRepository()
    {
        map = new HashMap<>();
    }

    public void addStadium(Stadium input)
    {
        if(map.containsKey(input.getId()))
        {
            throw new IllegalArgumentException("Stadium already exist");
        }

        map.put(input.getId(),input);
    }

    public Stadium getStadium(int id)
    {
         if(!map.containsKey(id))
         {
             throw new IllegalArgumentException("Stadium doesn't exist !");
         }
         return map.get(id);
    }

    public List<Stadium> getAll() {

        return new ArrayList<>(map.values());
    }

    public boolean contains(int id) {
        return map.containsKey(id);
    }
}
