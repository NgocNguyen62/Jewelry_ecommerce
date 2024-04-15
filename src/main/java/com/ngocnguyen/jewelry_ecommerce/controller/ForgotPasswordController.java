package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.service.PasswordResetService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "/client/forgot-password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try{
            passwordResetService.createToken(email, token);
            String siteUrl = request.getRequestURL().toString();
            String resetPasswordLink = siteUrl.replace(request.getServletPath(), "") + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "Link đổi mật khẩu đã được gửi về email.");

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "/client/forgot-password";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ngoc622512@gmail.com", "Jewelry Shop");
        helper.setTo(recipientEmail);

        String subject = "Link đặt lại mật khẩu.";

        String content = "<p>Hello,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
                + "<p>Vào link dưới đây để đặt lại mật khẩu của bạn:</p>"
                + "<p><a href=\"" + link + "\">Đổi mật khẩu</a></p>"
                + "<br>"
                + "<p>Bỏ qua email này nếu không còn nhu cầu.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = passwordResetService.getUser(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Không hợp lệ");
            return "/client/message";
        }

        return "/client/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = passwordResetService.getUser(token);
        if (user == null) {
            model.addAttribute("message", "Không tìm thấy tài khoản");
            return "/client/message";
        } else {
            userService.updatePassword(user, password);
            passwordResetService.deleteToken(token);
        }

        return "redirect:/login?passwordResetSuccess=true";
    }
}
