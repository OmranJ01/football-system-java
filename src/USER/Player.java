package USER;

import java.util.HashSet;

import java.util.Set;

public class Player extends User {

    private final Set<Integer> friends;
    private final Set<Integer> outgoingRequests;
    private final Set<Integer> pendingRequests;

    Player(int id,String name,String town)
    {
        super(id,name,town);
        friends = new HashSet<>();
        outgoingRequests = new HashSet<>();
        pendingRequests = new HashSet<>();
    }

    public Set<Integer> getFriends()
    {
        return this.friends;
    }

    public Set<Integer> getOutgoingRequests()
    {
        return this.outgoingRequests;
    }

    public Set<Integer> getPendingRequests()
    {
        return this.pendingRequests;
    }
}
