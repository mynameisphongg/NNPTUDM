<%-- 
    Document   : register
    Created on : Oct 10, 2024, 3:58:45 PM
    Author     : Acer
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"> <!-- Thêm thẻ meta cho mã hóa -->
    <title>Đăng Ký</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h1>Đăng Ký</h1>
    <form action="RegisterServlet" method="POST" accept-charset="UTF-8"> <!-- Thêm accept-charset -->
        <input type="text" name="username" placeholder="Tên người dùng" required>
        <input type="password" name="password" placeholder="Mật khẩu" required>
        <input type="submit" value="Đăng Ký">
    </form>
    <p>Đã có tài khoản? <a href="login.jsp">Đăng Nhập</a></p>
</body>
</html>

