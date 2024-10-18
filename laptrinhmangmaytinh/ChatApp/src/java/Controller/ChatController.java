package Controller;

import Repository.MessageRepository;
import Repository.MessageRepositoryImpl;
import Repository.UserRepository;
import Repository.UserRepositoryImpl;
import model.Message;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/chat")
public class ChatController extends HttpServlet {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ChatApp", "root", "admin123");

            // Khởi tạo MessageRepositoryImpl với kết nối
            messageRepository = new MessageRepositoryImpl(connection);
            userRepository = new UserRepositoryImpl(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("Không tìm thấy driver JDBC", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Không thể kết nối đến cơ sở dữ liệu", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để vào trang chat.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("chat.jsp").forward(request, response);
    }

  @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    User currentUser = (User) session.getAttribute("currentUser");

    if (currentUser == null) {
        request.setAttribute("errorMessage", "Bạn cần đăng nhập để gửi tin nhắn.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
        return;
    }

    try {
        String receiverIdStr = request.getParameter("receiverId");
        String content = request.getParameter("content");

        // Kiểm tra nếu receiverId và content hợp lệ
        if (receiverIdStr != null && !receiverIdStr.isEmpty() && content != null && !content.isEmpty()) {
            int senderId = currentUser.getId(); 
            int receiverId = Integer.parseInt(receiverIdStr);

            // Tạo và lưu tin nhắn
            Message message = new Message();
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setContent(content);
            message.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
            messageRepository.save(message);

            // Lấy danh sách tin nhắn giữa người gửi và người nhận
            List<Message> messages = messageRepository.getMessages(senderId, receiverId);
            request.setAttribute("messages", messages); 

            // Lấy tên người nhận
            User receiverUser = userRepository.findUserById(receiverId); 
            if (receiverUser != null) {
                request.setAttribute("receiverUserName", receiverUser.getUsername()); 
            } else {
                request.setAttribute("errorMessage", "Người nhận không tồn tại.");
            }
        } else {
            request.setAttribute("errorMessage", "Vui lòng điền tất cả các trường.");
            request.getRequestDispatcher("chat.jsp").forward(request, response);
            return;
        }
    } catch (NumberFormatException e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "ID người nhận không hợp lệ.");
        request.getRequestDispatcher("chat.jsp").forward(request, response);
        return;
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Đã xảy ra lỗi khi gửi tin nhắn. Vui lòng thử lại.");
        request.getRequestDispatcher("chat.jsp").forward(request, response);
        return;
    }

    // Chuyển hướng trở lại trang chat sau khi gửi tin nhắn thành công
    request.getRequestDispatcher("chat.jsp").forward(request, response);
}


    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
