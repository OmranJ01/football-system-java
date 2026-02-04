package Chat;

import java.util.Objects;

public class ConversationKey {

    private final int user1;
    private final int user2;

    public ConversationKey(int id1, int id2) {

        if (id1 == id2) {
            throw new IllegalArgumentException("Conversation requires two different users !");
        }

        if (id1 <= 0 || id2 <= 0) {
            throw new IllegalArgumentException("Invalid user id !");
        }

        // ensure consistent order (smallest first)
        if (id1 < id2) {
            this.user1 = id1;
            this.user2 = id2;
        } else {
            this.user1 = id2;
            this.user2 = id1;
        }
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationKey)) return false;
        ConversationKey that = (ConversationKey) o;
        return user1 == that.user1 && user2 == that.user2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
