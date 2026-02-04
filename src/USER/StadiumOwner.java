package USER;
import stadium.Stadium;

import java.util.HashSet;
import java.util.Set;

public class StadiumOwner extends User{

    private final Set<Stadium> stadiums;

   public StadiumOwner(Integer id,String name,String town)
    {
        super(id,name,town);
        stadiums = new HashSet<>();
    }

    public  Set<Stadium> getStadiums()
    {
        return this.stadiums;
    }

}
