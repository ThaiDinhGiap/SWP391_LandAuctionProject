package com.se1858.group4.Land_Auction_SWP391.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Xác minh email";
        String text = "Mã xác minh của bạn là: " + verificationCode;
        sendEmail(toEmail, subject, text);
    }

    public void sendContactConfirmationEmail(String toEmail) {
        String subject = "Chúng tôi sẽ liên hệ bạn sau";
        String text = "Cảm ơn bạn đã liên hệ với chúng tôi. Chúng tôi sẽ phản hồi sớm nhất.";
        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    public void sendContactFormDetails(String toEmail, String name, String email, String phone, String job, String message) {
        String subject = "Thông tin đăng ký liên hệ mới";
        String text = "Thông tin đăng ký liên hệ:\n\n" +
                "Họ tên: " + name + "\n" +
                "Email: " + email + "\n" +
                "Số điện thoại: " + phone + "\n" +
                "Nơi làm việc: " + job + "\n" +
                "Tin nhắn: " + message;
        sendEmail(toEmail, subject, text);
    }

}

