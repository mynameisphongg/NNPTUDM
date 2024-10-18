<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng Nhập</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
        }
        form {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            background: white;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            background-color: #28a745;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }
        input[type="submit"]:hover {
            background-color: #218838;
        }
        .error {
            color: red;
            text-align: center;
        }
    </style>
</head>
<body>
    <h1>Đăng Nhập</h1>
    
    <!-- Form với action trỏ đến LoginServlet và phương thức POST -->
    <form action="<%= request.getContextPath() %>/LoginServlet" method="POST" accept-charset="UTF-8">
        <input type="text" name="username" placeholder="Tên người dùng" value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" required>
        <input type="password" name="password" placeholder="Mật khẩu" required>
        <input type="submit" value="Đăng Nhập">
    </form>
    
    <!-- Hiển thị thông báo lỗi nếu có -->
    <p>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <span class="error">
                <%= request.getAttribute("errorMessage") %>
            </span>
        <% } %>
    </p>

    <p style="text-align: center;">
        Chưa có tài khoản? <a href="register.jsp">Đăng ký</a>
    </p>
</body>
</html>
