/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Acer
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class UserController {

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Trả về trang đăng ký
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        // Ghi mã đăng ký người dùng vào cơ sở dữ liệu
        // ...
        return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
    }

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Trả về trang đăng nhập
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Xác thực thông tin đăng nhập
        // ...
        return "redirect:/chat"; // Chuyển hướng đến trang chat nếu đăng nhập thành công
    }
}
