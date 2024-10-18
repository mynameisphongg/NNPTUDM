import Repository.DatabaseConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8"); // Thiết lập mã hóa cho response
        try (PrintWriter out = response.getWriter()) {
            // Lấy thông tin từ form đăng ký
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Mã hóa mật khẩu bằng BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Kết nối đến cơ sở dữ liệu và thêm thông tin người dùng
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, hashedPassword); // Lưu mật khẩu đã mã hóa
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        out.println("<html><head><meta charset='UTF-8'></head><body>");
                        out.println("<h1>Đăng Ký Thành Công!</h1>");
                        out.println("<a href='login.jsp'>Đăng Nhập</a>");
                        out.println("</body></html>");
                    } else {
                        out.println("<html><head><meta charset='UTF-8'></head><body>");
                        out.println("<h1>Đăng Ký Thất Bại!</h1>");
                        out.println("<a href='register.jsp'>Thử lại</a>");
                        out.println("</body></html>");
                    }
                }
            } catch (SQLException e) {
                out.println("<html><head><meta charset='UTF-8'></head><body>");
                out.println("<h1>Lỗi cơ sở dữ liệu: " + e.getMessage() + "</h1>");
                out.println("<a href='register.jsp'>Thử lại</a>");
                out.println("</body></html>");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet để xử lý đăng ký người dùng";
    }
}
