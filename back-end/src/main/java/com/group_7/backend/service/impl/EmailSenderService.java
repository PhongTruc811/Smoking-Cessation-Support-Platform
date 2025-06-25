package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserDto;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.ISenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailSenderService implements ISenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    //Gửi message bằng email cơ bản
    @Override
    public void sendMessage(String receiver, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        //Content của message
        message.setFrom("${spring.mail.username}");
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    //Gửi message bằng email kèm file
    @Override
    public void sendMessage(String receiver, String subject, String body, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(body);
        //Đính file vào nội dung gửi trong email
        FileSystemResource file = new FileSystemResource(new File(fileName));
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }
    //Gửi email cho tất cả user (có thể lỗi nhưng nhanh)
    @Override
    public void sendMessageToAll(String subject, String body, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String[] userList = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .map(UserDto::getEmail)
                .toArray(String[]::new);

        helper.setTo(userList);
        helper.setSubject(subject);
        helper.setText(body);
        //Đính file vào nội dung gửi trong email
        FileSystemResource file = new FileSystemResource(new File(fileName));
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }

    //Gửi email cho tất cả user nhưng xử lí từng người một (tránh lỗi nhưng chậm)
    public void sendMessageToAllIndividually(String subject, String body, String fileName) {
        String[] userList = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .map(UserDto::getEmail)
                .toArray(String[]::new);

        FileSystemResource fileResource = new FileSystemResource(new File(fileName));

        // Gửi tới từng người một
        for (String email : userList) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(email);
                helper.setSubject(subject);
                helper.setText(body);

                if (fileResource != null) {
                    // File cho việc đính kèm
                    helper.addAttachment(fileResource.getFilename(), fileResource);
                }

                mailSender.send(message);
                System.out.println("Email sent successfully to: " + email);
            } catch (MessagingException e) {
                System.err.println("Failed to send email to " + email + ": " + e.getMessage());
            }
        }
    }


}
