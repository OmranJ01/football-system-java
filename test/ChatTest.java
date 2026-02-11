import Chat.Message;
import Chat.ConversationKey;
import Chat.Conversation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {

    // Message Tests
    @Test
    public void testMessageCreationWithValidData() {
        Message message = new Message(1, "Hello World");
        
        assertEquals(1, message.getSenderId());
        assertEquals("Hello World", message.getContent());
        assertNotNull(message.getSentAt());
    }

    @Test
    public void testMessageWithInvalidSenderIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(0, "Hello");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(-1, "Hello");
        });
    }

    @Test
    public void testMessageWithNullContentThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(1, null);
        });
    }

    @Test
    public void testMessageWithBlankContentThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(1, "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(1, "   ");
        });
    }

    @Test
    public void testMessageTrimsContent() {
        Message message = new Message(1, "  Hello World  ");
        
        assertEquals("Hello World", message.getContent());
    }

    @Test
    public void testMessageWithLongContent() {
        String longContent = "a".repeat(1000);
        Message message = new Message(1, longContent);
        
        assertEquals(longContent, message.getContent());
    }

    // ConversationKey Tests
    @Test
    public void testConversationKeyCreation() {
        ConversationKey key = new ConversationKey(1, 2);
        
        assertEquals(1, key.getUser1());
        assertEquals(2, key.getUser2());
    }

    @Test
    public void testConversationKeyOrdersUserIds() {
        ConversationKey key1 = new ConversationKey(1, 2);
        ConversationKey key2 = new ConversationKey(2, 1);
        
        assertEquals(key1.getUser1(), key2.getUser1());
        assertEquals(key1.getUser2(), key2.getUser2());
    }

    @Test
    public void testConversationKeyWithSameUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConversationKey(1, 1);
        });
    }

    @Test
    public void testConversationKeyWithInvalidIdsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConversationKey(0, 1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ConversationKey(1, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ConversationKey(-1, 1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ConversationKey(1, -1);
        });
    }

    @Test
    public void testConversationKeyEquals() {
        ConversationKey key1 = new ConversationKey(1, 2);
        ConversationKey key2 = new ConversationKey(1, 2);
        ConversationKey key3 = new ConversationKey(2, 1);
        
        assertEquals(key1, key2);
        assertEquals(key1, key3);
        assertEquals(key2, key3);
    }

    @Test
    public void testConversationKeyNotEquals() {
        ConversationKey key1 = new ConversationKey(1, 2);
        ConversationKey key2 = new ConversationKey(1, 3);
        
        assertNotEquals(key1, key2);
    }

    @Test
    public void testConversationKeyHashCode() {
        ConversationKey key1 = new ConversationKey(1, 2);
        ConversationKey key2 = new ConversationKey(2, 1);
        
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    // Conversation Tests
    @Test
    public void testConversationCreation() {
        Conversation conv = new Conversation(1, 2);
        
        assertNotNull(conv.getKey());
        assertEquals(1, conv.getKey().getUser1());
        assertEquals(2, conv.getKey().getUser2());
        assertTrue(conv.getMessages().isEmpty());
    }

    @Test
    public void testConversationAddMessage() {
        Conversation conv = new Conversation(1, 2);
        Message msg = new Message(1, "Hello");
        
        conv.addMessage(msg);
        
        assertEquals(1, conv.getMessages().size());
        assertEquals(msg, conv.getMessages().get(0));
    }

    @Test
    public void testConversationAddMultipleMessages() {
        Conversation conv = new Conversation(1, 2);
        Message msg1 = new Message(1, "Hello");
        Message msg2 = new Message(2, "Hi there");
        Message msg3 = new Message(1, "How are you?");
        
        conv.addMessage(msg1);
        conv.addMessage(msg2);
        conv.addMessage(msg3);
        
        assertEquals(3, conv.getMessages().size());
        assertEquals(msg1, conv.getMessages().get(0));
        assertEquals(msg2, conv.getMessages().get(1));
        assertEquals(msg3, conv.getMessages().get(2));
    }

    @Test
    public void testConversationAddNullMessageThrowsException() {
        Conversation conv = new Conversation(1, 2);
        
        assertThrows(IllegalArgumentException.class, () -> {
            conv.addMessage(null);
        });
    }

    @Test
    public void testConversationGetMessagesReturnsUnmodifiableList() {
        Conversation conv = new Conversation(1, 2);
        Message msg = new Message(1, "Hello");
        
        conv.addMessage(msg);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            conv.getMessages().add(msg);
        });
    }

    @Test
    public void testConversationKeySymmetry() {
        Conversation conv1 = new Conversation(1, 2);
        Conversation conv2 = new Conversation(2, 1);
        
        assertEquals(conv1.getKey(), conv2.getKey());
    }

    @Test
    public void testCompleteConversation() {
        Conversation conv = new Conversation(5, 10);
        
        // Simulate a conversation
        conv.addMessage(new Message(5, "Hey, want to play football?"));
        conv.addMessage(new Message(10, "Sure! When?"));
        conv.addMessage(new Message(5, "How about tomorrow at 3pm?"));
        conv.addMessage(new Message(10, "Perfect, I'll be there!"));
        
        assertEquals(4, conv.getMessages().size());
        assertEquals(5, conv.getMessages().get(0).getSenderId());
        assertEquals(10, conv.getMessages().get(1).getSenderId());
        assertEquals("Hey, want to play football?", conv.getMessages().get(0).getContent());
    }

    @Test
    public void testConversationWithManyMessages() {
        Conversation conv = new Conversation(1, 2);
        
        for (int i = 0; i < 100; i++) {
            conv.addMessage(new Message(i % 2 == 0 ? 1 : 2, "Message " + i));
        }
        
        assertEquals(100, conv.getMessages().size());
    }

    @Test
    public void testConversationKeyWithLargeIds() {
        ConversationKey key = new ConversationKey(Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        
        assertEquals(Integer.MAX_VALUE - 1, key.getUser1());
        assertEquals(Integer.MAX_VALUE, key.getUser2());
    }

    @Test
    public void testMessageTimestampOrder() throws InterruptedException {
        Message msg1 = new Message(1, "First");
        Thread.sleep(10);
        Message msg2 = new Message(1, "Second");
        
        assertTrue(msg1.getSentAt().isBefore(msg2.getSentAt()) || 
                   msg1.getSentAt().equals(msg2.getSentAt()));
    }
}
