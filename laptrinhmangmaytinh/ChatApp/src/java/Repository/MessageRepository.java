package Repository;

import java.util.List;
import model.Message;

public interface MessageRepository {
    void save(Message message); // Lưu tin nhắn
    List<Message> getMessages(int senderId, int receiverId); // Lấy tin nhắn giữa hai người dùng
    List<Message> findAll(); // Lấy tất cả tin nhắn
}
