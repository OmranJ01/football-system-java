package Chat;

import java.time.LocalDateTime;

public class Message {

    private final int senderId;
    private final String content;
    private final LocalDateTime sentAt;

    public Message(int senderId, String content) {

        if (senderId <= 0) {
            throw new IllegalArgumentException("Invalid sender id !");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty !");
        }

        this.senderId = senderId;
        this.content = content.trim();
        this.sentAt = LocalDateTime.now();
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
