<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    HttpSession currentSession = request.getSession(false);
    User currentUser = null;

    if (currentSession != null) {
        currentUser = (User) currentSession.getAttribute("currentUser");
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang Chủ</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h1>Chào mừng đến với ứng dụng nhắn tin tức thời</h1>

    <h2>
        <c:choose>
            <c:when test="${currentUser != null}">
                Xin chào, ${currentUser.username}!
            </c:when>
            <c:otherwise>
                Người dùng không được xác thực.
            </c:otherwise>
        </c:choose>
    </h2>
    
    <p>Đây là trang chính của bạn!</p>
    <a href="LogoutServlet">Đăng xuất</a>

    <div>
        <h3>Bắt đầu trò chuyện</h3>
        <form action="ChatServlet" method="get">
            <label for="receiverId">Chọn người nhận:</label>
            <select name="receiverId" id="receiverId">
                <option value="1">Người dùng 1</option>
                <option value="2">Người dùng 2</option>
                <!-- Thêm nhiều tùy chọn khác với các ID người nhận thực tế -->
            </select>
            <button type="submit">Chat ngay!</button>
        </form>
    </div>
</body>
</html>
