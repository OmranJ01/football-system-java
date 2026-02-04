package Service;

import Chat.Conversation;
import Chat.ConversationKey;
import Chat.Message;
import USER.Player;
import repository.PlayerRepository;

import java.util.HashMap;
import java.util.Map;

public class ChatService {

    private final PlayerRepository playerRepository;
    private final Map<ConversationKey, Conversation> conversations;

    public ChatService(PlayerRepository playerRepository) {
        if (playerRepository == null) {
            throw new IllegalArgumentException("PlayerRepository cannot be null !");
        }
        this.playerRepository = playerRepository;
        this.conversations = new HashMap<>();
    }

    public void sendMessage(int senderId, int receiverId, String text) {

        if (senderId == receiverId) {
            throw new RuntimeException("Same player !");
        }

        if (!playerRepository.contains(senderId) || !playerRepository.contains(receiverId)) {
            throw new RuntimeException("Player doesn't exist !");
        }

        Player sender = playerRepository.getPlayer(senderId);
        Player receiver = playerRepository.getPlayer(receiverId);

        if (!sender.getFriends().contains(receiverId) || !receiver.getFriends().contains(senderId)) {
            throw new RuntimeException("Users are not friends !");
        }

        ConversationKey key = new ConversationKey(senderId, receiverId);

        Conversation conv = conversations.get(key);
        if (conv == null) {
            conv = new Conversation(senderId, receiverId);
            conversations.put(key, conv);
        }

        Message m = new Message(senderId, text);
        conv.addMessage(m);
    }

    public Conversation getConversation(int id1, int id2) {

        if (id1 == id2) {
            throw new RuntimeException("Same player !");
        }

        if (!playerRepository.contains(id1) || !playerRepository.contains(id2)) {
            throw new RuntimeException("Player doesn't exist !");
        }

        ConversationKey key = new ConversationKey(id1, id2);
        Conversation conv = conversations.get(key);

        if (conv == null) {
            return new Conversation(id1, id2);
        }

        return conv;
    }

    public boolean hasConversation(int id1, int id2) {
        ConversationKey key = new ConversationKey(id1, id2);
        return conversations.containsKey(key);
    }
}
