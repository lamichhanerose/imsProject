package com.example.imsmanagemnt.forgetPassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.imsmanagemnt.user.User;
import com.example.imsmanagemnt.user.UserRepository;

@Controller
public class ForgetPasswordController {

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @Autowired
    private UserRepository userRepository;


    public ForgetPasswordController(ForgetPasswordService forgetPasswordService) {
        this.forgetPasswordService = forgetPasswordService;
    }

    @GetMapping("/passwordReset")
    public String showForgetPasswordPage() {
        return "password";
    }

    @PostMapping("/passwordReset")
    public String processForgetPassword(@RequestParam("email") String email, Model model) {
        try {
            boolean emailValid = forgetPasswordService.processForgetPassword(email);
            if (emailValid) {
                return "redirect:/token";
            } else {
                model.addAttribute("error", "Invalid email address.");
                return "password";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "password";
        }
    }

    @GetMapping("/token")
    public String showTokenPage() {
        return "token";
    }

    @PostMapping("/token")
    public String resetToken(@RequestParam("code") String code, Model model) {
        try {
            boolean tokenValid = forgetPasswordService.resetToken(code);
            if (tokenValid) {
                return "redirect:/changePassword";
            } else {
                model.addAttribute("error", "Invalid token.");
                return "token";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "token";
        }
    }

    @GetMapping("/changePassword")
    public String showChangePasswordPage() {
        return "reset";
    }

    @PostMapping("/changePassword")
    public String changePassword(String email,
        @RequestParam("password") String newPassword,
        @RequestParam("confirmPassword") String confirmPassword,
        Model model) {
        if (newPassword.equals(confirmPassword)) {
            try {
                forgetPasswordService.changePassword(email , newPassword, confirmPassword);
                model.addAttribute("success", "Password changed successfully!");
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("error", "Passwords do not match.");
        }
        return "login";
    }


}


