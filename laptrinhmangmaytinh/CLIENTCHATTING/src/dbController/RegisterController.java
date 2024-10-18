package dbController;

import db.DatabaseConnection;
import db.MD5;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class RegisterController {
    // String username, String password
   public static boolean Register(String username, String password) throws SQLException, ClassNotFoundException {
    if (username != null && password != null) {
        if (username.length() > 0 && password.length() > 0) {
            // Kiểm tra xem tên người dùng đã tồn tại hay chưa
            String SQL = "SELECT * FROM user WHERE username=?";
            Connection connection = DatabaseConnection.getDatabaseConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username); // Sử dụng câu lệnh để tìm kiếm người dùng
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Nếu người dùng đã tồn tại
            if (resultSet.next()) {
                // Tên người dùng đã tồn tại
                System.out.println("Tên người dùng đã tồn tại.");
                return false; // Trả về false nếu tên người dùng đã tồn tại
            }

            // Nếu không có người dùng trùng tên, tiến hành thêm mới
            String insertToUser = "INSERT INTO user (username, password) VALUES (?, ?)";
            String insertToRecoverySQL = "INSERT INTO recovery_user (username, password) VALUES (?, ?)";
            
            try {
                // Thêm vào bảng user
                preparedStatement = connection.prepareStatement(insertToUser);
                String hashedUsername = new MD5().MD5Digest(username);
                String hashedPassword = new MD5().MD5Digest(password);
                preparedStatement.setString(1, hashedUsername);
                preparedStatement.setString(2, hashedPassword);
                int n = preparedStatement.executeUpdate();
                
                if (n != 1) {
                    System.out.println("[INSERT OBJECT] Fail insert to database.");
                    return false;
                }
                
                // Thêm vào bảng recovery_user
                preparedStatement = connection.prepareStatement(insertToRecoverySQL);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                n = preparedStatement.executeUpdate();
                
                if (n != 1) {
                    System.out.println("[INSERT OBJECT] Fail insert to database.");
                    return false;
                }      
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false; // Trả về false nếu có lỗi xảy ra
            }
        }
    }
    return true; // Trả về true nếu đăng ký thành công
}

}
