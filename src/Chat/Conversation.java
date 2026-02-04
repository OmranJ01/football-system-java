package Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conversation {

    private final ConversationKey key;
    private final List<Message> messages;

    public Conversation(int id1, int id2) {
        this.key = new ConversationKey(id1, id2);
        this.messages = new ArrayList<>();
    }

    public ConversationKey getKey() {
        return key;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void addMessage(Message m) {
        if (m == null) {
            throw new IllegalArgumentException("Message cannot be null !");
        }
        messages.add(m);
    }
}
