package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserDto;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.ISenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Random;

@Service
public class EmailSenderService implements ISenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String mailSenderUsername;

    // Trả về một thông điệp HTML động viên ngẫu nhiên
    public String getRandomMotivationalMessage() {
        List<String> messages = List.of(
                "<h3>🚭 You're stronger than your cravings!</h3><p>Every cigarette you avoid is a victory. Stay strong and remember why you started this journey.</p>",
                "<h3>💪 One step at a time</h3><p>Every smoke-free minute adds up. You’re doing amazing. Keep pushing forward!</p>",
                "<h3>🎯 Stay focused, stay free</h3><p>Imagine how proud you'll be tomorrow. Your future self thanks you for staying smoke-free today!</p>",
                "<h3>🌿 Breathe in strength, breathe out smoke</h3><p>Fresh air feels better than smoke. Keep choosing health every moment.</p>",
                "<h3>🔥 Crush the craving!</h3><p>Cravings pass. Health lasts. You're in control now, not nicotine.</p>"
        );

        return messages.get(new Random().nextInt(messages.size()));
    }

    public String getFormattedMotivationalEmail() {
        String message = getRandomMotivationalMessage();

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    background-color: #f4f6f8;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #ffffff;
                    border-radius: 10px;
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                    padding: 30px;
                    text-align: center;
                }
                .header {
                    font-size: 22px;
                    color: #333;
                    margin-bottom: 15px;
                }
                .content {
                    font-size: 16px;
                    color: #444;
                    line-height: 1.6;
                }
                .footer {
                    margin-top: 30px;
                    font-size: 14px;
                    color: #888;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">✨ Daily Motivation for Your Smoke-Free Journey</div>
                <div class="content">
                    %s
                </div>
                <div class="footer">
                    You're receiving this message because you're on a journey to quit smoking.<br>
                    Keep it up – one step at a time!
                </div>
            </div>
        </body>
        </html>
        """.formatted(message);
    }

    // Gửi email đơn cho 1 người — HTML, không file
    @Override
    public void sendMessage(String receiver, String subject, String body) {
        try {
            sendEmail(receiver, subject, body, null);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // Gửi email đơn cho 1 người — có thể có file
    @Override
    public void sendMessage(String receiver, String subject, String body, String fileName) throws MessagingException {
        sendEmail(receiver, subject, body, fileName);
    }

    // Gửi email tới tất cả người dùng cùng lúc (không khuyến nghị nếu số lượng lớn)
    @Override
    public void sendMessageToAll(String subject, String body, String fileName) throws MessagingException {
        String[] userList = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .map(UserDto::getEmail)
                .toArray(String[]::new);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailSenderUsername);
        helper.setTo(userList);
        helper.setSubject(subject);
        helper.setText(body, true); // HTML

        if (fileName != null && !fileName.isBlank()) {
            File file = new File(fileName);
            if (file.exists() && file.isFile()) {
                FileSystemResource fileResource = new FileSystemResource(file);
                helper.addAttachment(fileResource.getFilename(), fileResource);
            }
        }

        mailSender.send(message);
    }

    // Gửi email tới từng người một (tốt hơn cho số lượng lớn)
    @Override
    public void sendMessageToAllIndividually(String subject, String body, String fileName) {
        String[] userList = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .map(UserDto::getEmail)
                .toArray(String[]::new);

        int failed = 0, total = 0;

        for (String email : userList) {
            try {
                sendEmail(email, subject, body, fileName);
            } catch (MessagingException e) {
                failed++;
            }
            total++;
        }

        System.out.println("Total emails sent: " + total);
        System.out.println("Failed emails sent: " + failed);
    }

    // Hàm dùng chung để gửi email HTML, có thể có file hoặc không
    private void sendEmail(String to, String subject, String body, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailSenderUsername);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body != null ? body : getFormattedMotivationalEmail(), true);

        if (fileName != null && !fileName.isBlank()) {
            File file = new File(fileName);
            if (file.exists() && file.isFile()) {
                FileSystemResource fileResource = new FileSystemResource(file);
                helper.addAttachment(fileResource.getFilename(), fileResource);
            } else {
                System.out.println("File not found or inaccessible: " + fileName);
            }
        }

        mailSender.send(message);
        System.out.println("Email sent to: " + to);
    }
}
