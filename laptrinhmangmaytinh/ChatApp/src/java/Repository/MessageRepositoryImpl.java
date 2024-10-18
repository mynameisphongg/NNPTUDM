package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Message;

public class MessageRepositoryImpl implements MessageRepository {
    private Connection connection; // Thêm thuộc tính Connection

    // Constructor nhận đối tượng Connection
    public MessageRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, message.getSenderId());
            statement.setInt(2, message.getReceiverId());
            statement.setString(3, message.getContent());
            statement.setTimestamp(4, message.getTimestamp());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu tin nhắn: " + e.getMessage());
        }
    }

    @Override
    public List<Message> getMessages(int senderId, int receiverId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            statement.setInt(3, receiverId);
            statement.setInt(4, senderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = new Message();
                    message.setId(resultSet.getInt("id"));
                    message.setSenderId(resultSet.getInt("sender_id"));
                    message.setReceiverId(resultSet.getInt("receiver_id"));
                    message.setContent(resultSet.getString("content"));
                    message.setTimestamp(resultSet.getTimestamp("timestamp"));
                    messages.add(message);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tin nhắn: " + e.getMessage());
        }
        return messages;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages ORDER BY timestamp";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getInt("id"));
                message.setSenderId(resultSet.getInt("sender_id"));
                message.setReceiverId(resultSet.getInt("receiver_id"));
                message.setContent(resultSet.getString("content"));
                message.setTimestamp(resultSet.getTimestamp("timestamp"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả tin nhắn: " + e.getMessage());
        }
        return messages;
    }
}
