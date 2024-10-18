import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        // Kiểm tra nếu người dùng đã đăng nhập
        if (currentUser != null) {
            String receiverIdStr = request.getParameter("receiverId"); // Lấy ID người nhận từ request

            if (receiverIdStr != null && !receiverIdStr.isEmpty()) {
                request.setAttribute("receiverId", receiverIdStr); // Gán receiverId vào request
                request.getRequestDispatcher("chat.jsp").forward(request, response); // Chuyển tiếp đến chat.jsp
            } else {
                response.sendRedirect("home.jsp"); // Nếu không có receiverId, chuyển về trang home
            }
        } else {
            response.sendRedirect("login.jsp"); // Nếu chưa đăng nhập, chuyển đến trang login
        }
    }
}
