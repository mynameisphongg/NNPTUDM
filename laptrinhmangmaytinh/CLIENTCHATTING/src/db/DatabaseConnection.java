package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class DatabaseConnection {
    private Connection connection;
    
    private static DatabaseConnection databaseConnection;
    
    private DatabaseConnection() throws ClassNotFoundException, SQLException {
        // Nạp MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Kết nối đến cơ sở dữ liệu với tài khoản root và mật khẩu admin123
        connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/db_chatapp?useTimezone=true&useSSL=false&useTimezone=true&serverTimezone=UTC",
                               "root", "admin123");
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static DatabaseConnection getDatabaseConnection() throws ClassNotFoundException, SQLException {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }
}
