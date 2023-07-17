package com.example.imsmanagemnt.forgetPassword;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.imsmanagemnt.user.User;
import com.example.imsmanagemnt.user.UserRepository;

@Service
public class ForgetPasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public boolean processForgetPassword(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String resetCode = generateResetCode();
            user.setResetCode(resetCode);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public void changePassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    private String generateResetCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }

    public boolean resetToken(String code) {
        User user = userRepository.findByResetCode(code);
        if (user != null && user.getResetCode().equals(code)) {
            return true;
        } else {
            return false;
        }
    }

}
