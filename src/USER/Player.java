package USER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player extends User {

    private final Set<Integer> friends;
    private final Set<Integer> friendRequests;
    private final Set<Integer> pendingRequests;

    Player(int id,String name,String town)
    {
        super(id,name,town);
        friends = new HashSet<>();
        friendRequests = new HashSet<>();
        pendingRequests = new HashSet<>();
    }

    public Set<Integer> getFriends()
    {
        return this.friends;
    }

    public Set<Integer> getFriendRequests()
    {
        return this.friendRequests;
    }

    public Set<Integer> getPendingRequests()
    {
        return this.pendingRequests;
    }
}
