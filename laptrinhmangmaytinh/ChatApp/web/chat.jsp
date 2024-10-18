<%@page import="java.util.List"%>
<%@page import="model.Message"%>
<%@page import="model.User"%> <!-- Nhập lớp User -->
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="javax.servlet.http.HttpSession" %>

<%
    request.setCharacterEncoding("UTF-8"); // Thiết lập mã hóa UTF-8 cho request
    response.setCharacterEncoding("UTF-8"); // Thiết lập mã hóa UTF-8 cho response

    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("currentUser") == null) {
        response.sendRedirect("login.jsp"); // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
        return; // Kết thúc trang JSP
    }

    // Lấy userId từ session
    User currentUser = (User) currentSession.getAttribute("currentUser"); // Lấy User từ session
    Integer senderId = currentUser.getId(); // Lấy ID của người gửi từ User
    String senderUserName = currentUser.getUsername(); // Lấy tên người gửi từ User
    String receiverIdStr = request.getParameter("receiverId"); // Lấy ID của người nhận từ URL

    // Kiểm tra nếu receiverIdStr không null và không rỗng
    if (receiverIdStr == null || receiverIdStr.isEmpty()) {
        response.sendRedirect("home.jsp"); // Nếu không có receiverId, chuyển về trang home
        return; // Kết thúc trang JSP
    }

    Integer receiverUserId = Integer.parseInt(receiverIdStr); // Chuyển đổi receiverId sang Integer

    // Lấy danh sách tin nhắn và tên người nhận từ request
    List<Message> messages = (List<Message>) request.getAttribute("messages"); // Lấy danh sách tin nhắn từ request
    String receiverUserName = (String) request.getAttribute("receiverUserName"); // Lấy tên người nhận từ request

    if (receiverUserName == null) {
        receiverUserName = "Người dùng không xác định"; // Nếu không có tên người nhận, hiển thị thông báo
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chat App</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css"> <!-- Đường dẫn CSS -->
</head>
<body>
    <h2>Chat với người dùng: <%= receiverUserName %></h2> <!-- Hiển thị tên người dùng -->

    <form action="chat" method="post">
        <input type="hidden" name="senderId" value="<%= senderId %>"/> <!-- Gán senderId từ session -->
        <input type="hidden" name="receiverId" value="<%= receiverUserId %>"/> <!-- Gán receiverId -->
        <textarea name="content" required placeholder="Nhập tin nhắn..."></textarea>
        <button type="submit">Gửi</button>
    </form>

    <div>
        <h3>Tin nhắn</h3>
        <c:if test="${not empty messages}">
            <c:forEach items="${messages}" var="message">
                <p>${message.senderId}: ${message.content}</p>
            </c:forEach>
        </c:if>
        <c:if test="${empty messages}">
            <p>Chưa có tin nhắn nào.</p>
        </c:if>
    </div>

    <a href="home.jsp">Quay lại trang chủ</a> <!-- Nút để quay lại trang chủ -->
</body>
</html>
