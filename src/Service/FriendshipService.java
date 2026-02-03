package Service;
import USER.Player;
import repository.PlayerRepository;

import java.util.Set;

public class FriendshipService {

    private final PlayerRepository playerRepository;

    public FriendshipService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void sendRequest(int senderId,int receiverId)
    {

        if (senderId == receiverId)
        {
            throw new RuntimeException("Same player !");
        }

        if(!playerRepository.contains(senderId) || !playerRepository.contains(receiverId))
        {
            throw new RuntimeException("Player doesn't exist !");
        }

        Player sender = playerRepository.getPlayer(senderId);
        Player receiver = playerRepository.getPlayer(receiverId);

        Set<Integer> friends = sender.getFriends();

        if(friends.contains(receiverId))
        {
            throw new RuntimeException("They are already friends !");
        }

        Set<Integer> pending = receiver.getPendingRequests();

        if(pending.contains(senderId))
        {
            throw new RuntimeException("You have already sent a request " + "to " + receiverId );
        }

        sender.getOutgoingRequests().add(receiverId);
        receiver.getPendingRequests().add(senderId);
    }


    public void acceptRequest(int senderId,int acceptorId)
    {
        if (senderId == acceptorId)
        {
            throw new RuntimeException("Same player !");
        }


        if(!playerRepository.contains(senderId) || !playerRepository.contains(acceptorId))
        {
            throw new RuntimeException("Player doesn't exist !");
        }

        Player sender = playerRepository.getPlayer(senderId);
        Player acceptor = playerRepository.getPlayer(acceptorId);

        if(sender.getFriends().contains(acceptorId))
        {
            throw new RuntimeException("They are already friends !");
        }


        if(!acceptor.getPendingRequests().contains(senderId)
        || !sender.getOutgoingRequests().contains(acceptorId))
        {
              throw new RuntimeException("Invalid arguments !");
        }


        sender.getOutgoingRequests().remove(acceptorId);
        acceptor.getPendingRequests().remove(senderId);
        sender.getFriends().add(acceptorId);
        acceptor.getFriends().add(senderId);
    }

    public void rejectRequest(int senderId, int rejectorId)
    {
        if (senderId == rejectorId)
        {
            throw new RuntimeException("Same player !");
        }

        if(!playerRepository.contains(senderId) || !playerRepository.contains(rejectorId))
        {
            throw new RuntimeException("Player doesn't exist !");
        }

        Player sender = playerRepository.getPlayer(senderId);
        Player rejector = playerRepository.getPlayer(rejectorId);

        if(sender.getFriends().contains(rejectorId))
        {
            throw new RuntimeException("They are already friends !");
        }

        if(!rejector.getPendingRequests().contains(senderId)
                || !sender.getOutgoingRequests().contains(rejectorId))
        {
            throw new RuntimeException("Invalid arguments !");
        }

        sender.getOutgoingRequests().remove(rejectorId);
        rejector.getPendingRequests().remove(senderId);
    }

    public void removeFriend(int id1, int id2)
    {
        if (id1 == id2)
        {
            throw new RuntimeException("Same player !");
        }

        if(!playerRepository.contains(id1) || !playerRepository.contains(id2))
        {
            throw new RuntimeException("Player doesn't exist !");
        }

        Player p1 = playerRepository.getPlayer(id1);
        Player p2 = playerRepository.getPlayer(id2);

        if(!p1.getFriends().contains(id2) || !p2.getFriends().contains(id1))
        {
            throw new RuntimeException("They are not friends !");
        }

        p1.getFriends().remove(id2);
        p2.getFriends().remove(id1);

        p1.getOutgoingRequests().remove(id2);
        p2.getPendingRequests().remove(id1);

        p2.getOutgoingRequests().remove(id1);
        p1.getPendingRequests().remove(id2);
    }

    public void cancelRequest(int senderId, int receiverId)
    {
        if (senderId == receiverId)
        {
            throw new RuntimeException("Same player !");
        }

        if(!playerRepository.contains(senderId) || !playerRepository.contains(receiverId))
        {
            throw new RuntimeException("Player doesn't exist !");
        }

        Player sender = playerRepository.getPlayer(senderId);
        Player receiver = playerRepository.getPlayer(receiverId);

        if(!sender.getOutgoingRequests().contains(receiverId)
                || !receiver.getPendingRequests().contains(senderId))
        {
            throw new RuntimeException("Invalid arguments !");
        }

        sender.getOutgoingRequests().remove(receiverId);
        receiver.getPendingRequests().remove(senderId);
    }

}
