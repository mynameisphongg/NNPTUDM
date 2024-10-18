package Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ChatApp"; // Địa chỉ cơ sở dữ liệu
    private static final String USERNAME = "root"; // Tên đăng nhập
    private static final String PASSWORD = "admin123"; // Mật khẩu

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Nạp driver JDBC cho MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Tạo kết nối với cơ sở dữ liệu
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Kết nối thành công!");

        } catch (ClassNotFoundException e) {
            // Xử lý lỗi không tìm thấy driver
            System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());

        } catch (SQLException e) {
            // Xử lý lỗi kết nối cơ sở dữ liệu
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
        return connection;
    }
}
