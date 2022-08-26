package com.beam.whatsapp.service;

import com.beam.whatsapp.dto.SendMessageRequest;
import com.beam.whatsapp.model.Message;
import com.beam.whatsapp.model.Session;
import com.beam.whatsapp.model.User;
import com.beam.whatsapp.model.status.MessageStatus;
import com.beam.whatsapp.repository.MessageRepository;
import com.beam.whatsapp.repository.SessionRepository;
import com.beam.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final SimpMessagingTemplate template;

    private final SessionRepository sessionRepository;

    public List<Message> list(String id) {
        Optional<Session> optSession = sessionRepository.findById(id);
        if (optSession.isPresent()) {
            Session session = optSession.get();
            List<Message> messages = session.getMessages();
            return messages;
        }
        return null;
    }

    public void sendMessage(String sessionId, String userId, User sender, String message) {
        Optional<Session> optSession = sessionRepository.findById(sessionId);
        Optional<User> optUser = null;
        if (userId != null) {
            optUser = userRepository.findById(userId);
        }
        if (optSession.isPresent()) {
            Session session = optSession.get();
            User receiver;
            if (optUser != null) {
                receiver = optUser.get();
            } else {
                receiver = session.getUsers().get(0);
            }
            Message newMessage = new Message()
                    .setContent(message)
                    .setSender(sender)
                    .setSentAt(new Date())
                    .setStatus(MessageStatus.RECEIVED);

            newMessage.getReceiverNumbers().add(receiver.getNumber());
            session.setLastMessage(newMessage.getContent());
            session.setLastMessageTime(newMessage.getSentAt());
            session.getMessages().add(newMessage);

            sessionRepository.save(session);

            template.convertAndSend("/whatsapp/send-message/" + sessionId, newMessage);

        }
    }

    public void readMessage(Message message, User user) {
        message.getSentUserIdList().remove(user.getId());
        messageRepository.save(message);
        message.setReadAt(new Date())
                .getReadUserIdList().add(user.getId());
        if (message.getReadUserIdList().size() == message.getReadUserIdList().size() + message.getSentUserIdList().size()) {
            message.setStatus(MessageStatus.READ);
        } else if (message.getSentUserIdList().size() >= 1) {
            message.setStatus(MessageStatus.RECEIVED);
        }
        messageRepository.save(message);
    }
}
