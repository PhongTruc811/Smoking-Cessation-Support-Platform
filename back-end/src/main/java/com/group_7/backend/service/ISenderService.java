package com.group_7.backend.service;

import jakarta.mail.MessagingException;

public interface ISenderService {
    void sendMessage(String receiver, String subject,String body);
    void sendMessage(String receiver, String subject, String body, String fileName) throws MessagingException;
    void sendMessageToAll( String subject, String body, String fileName) throws MessagingException;
}
