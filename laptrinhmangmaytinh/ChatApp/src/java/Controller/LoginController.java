package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Repository.DatabaseConnection;
import javax.servlet.RequestDispatcher;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra tính hợp lệ của tài khoản từ cơ sở dữ liệu
        String storedPassword = getPassword(username);

        if (storedPassword != null && storedPassword.equals(password)) { // So sánh mật khẩu trực tiếp
            HttpSession session = request.getSession();
            session.setAttribute("username", username); // Lưu username vào session

            // Chuyển hướng đến trang home.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
            dispatcher.forward(request, response);
        } else {
            // Nếu tài khoản không hợp lệ, trả lại trang đăng nhập với thông báo lỗi
            request.setAttribute("errorMessage", "Tên người dùng hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Phương thức lấy mật khẩu từ cơ sở dữ liệu
    private String getPassword(String username) {
        String password = null;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    password = resultSet.getString("password"); // Lấy mật khẩu từ cơ sở dữ liệu
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }
}
