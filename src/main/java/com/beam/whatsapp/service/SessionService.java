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

import static com.beam.whatsapp.service.UserService.SESSION_USER;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public void addUserToSession(User user, String sessionId) {
        Optional<Session> optSession = sessionRepository.findById(sessionId);

        if (optSession.isPresent()) {
            Session session = optSession.get();

            session.getUsers().add(user);

            sessionRepository.save(session);
        }
    }

    public Session newSession(String id, User me) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            User realUser = optUser.get();
            Session newSession = new Session();
            newSession.setAvatar(realUser.getAvatar());
            newSession.setTitle(realUser.getName());
            newSession.getUsers().add(realUser);
            newSession.getUsers().add(me);
            sessionRepository.save(newSession);
            return newSession;
        } else {
            return null;
        }
    }

    public List<Session> list(User user) {
        List<Session> sessions = sessionRepository.findByUsersId(user.getId());
        List<Session> newSessions = new ArrayList<>();
        for (Session s : sessions) {
            s.getUsers().remove(user);
            s.setAvatar(s.getUsers().get(0).getAvatar());
            s.setTitle(s.getUsers().get(0).getName());
            newSessions.add(s);
        }

        return newSessions;
    }

    public Session openSession(String sessionId) {
        Optional<Session> optSession = sessionRepository.findById(sessionId);
        if (optSession.isPresent()) {
            Session session = optSession.get();
            return session;
        } else {
            return null;
        }
    }
}
