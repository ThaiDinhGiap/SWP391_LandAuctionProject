package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AccountRepository accountRepository;

    private String storedOtp; // for simplicity, store OTP temporarily in memory or use a database.

    public void registerUser(String username, String password, String email) {
        // Registration logic here
        storedOtp = generateOTP(); // Generate OTP
        sendOtpEmail(email, storedOtp); // Send OTP email
    }

    public boolean verifyOtp(String otp) {
        return otp.equals(storedOtp); // Validate OTP
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP Verification");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        return String.format("%06d", new Random().nextInt(999999));
    }


    public Object findAllStaffsByRole(String roleAuctioneer) {
    }
}
