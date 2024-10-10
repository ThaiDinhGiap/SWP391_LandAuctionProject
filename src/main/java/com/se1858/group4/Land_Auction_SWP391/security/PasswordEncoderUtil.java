package com.se1858.group4.Land_Auction_SWP391.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123";  // Mật khẩu gốc
        String encodedPassword = passwordEncoder.encode(rawPassword);  // Mã hóa mật khẩu
        System.out.println(encodedPassword);
    }
}
