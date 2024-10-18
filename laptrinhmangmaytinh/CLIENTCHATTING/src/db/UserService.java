package db;


import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {

    public void registerUser(String username, String password) {
        Connection conn = null;

        try {
            // Lấy kết nối từ lớp DatabaseConnection
            conn = DatabaseConnection.getDatabaseConnection().getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Chèn vào bảng user
            String insertUserSQL = "INSERT INTO user (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }

            // Chèn vào bảng recover_user
            String insertRecoverSQL = "INSERT INTO recover_user (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertRecoverSQL)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }

            conn.commit(); // Cam kết giao dịch

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
