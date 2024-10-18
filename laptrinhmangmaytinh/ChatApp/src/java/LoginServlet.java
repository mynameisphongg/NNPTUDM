import model.User;
import Repository.UserRepository;
import Repository.UserRepositoryImpl;
import Repository.DatabaseConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        try {
            // Khởi tạo kết nối cơ sở dữ liệu và UserRepository
            Connection connection = DatabaseConnection.getConnection();
            userRepository = new UserRepositoryImpl(connection);
        } catch (Exception e) {
            throw new ServletException("Không thể kết nối cơ sở dữ liệu", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra username và password có bị null hoặc trống
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không được để trống.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        User user = userRepository.findUserByUsername(username);

        // Kiểm tra nếu mật khẩu đúng bằng cách sử dụng bcrypt để so sánh
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            // Hủy session cũ nếu tồn tại và tạo session mới
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            session = request.getSession(true);
            session.setAttribute("currentUser", user);

            // Chuyển hướng về trang home.jsp
            response.sendRedirect("home.jsp");
        } else {
            // Nếu đăng nhập thất bại, hiển thị thông báo lỗi
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
