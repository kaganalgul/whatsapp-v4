package com.beam.whatsapp.controller;

import com.beam.whatsapp.model.Session;
import com.beam.whatsapp.model.User;
import com.beam.whatsapp.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.beam.whatsapp.service.UserService.SESSION_USER;

@RestController
@RequestMapping("session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("new-session")
    public Session newSession(@RequestBody String id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(SESSION_USER);
        return sessionService.newSession(id, user);
    }

    @PostMapping("open-exist-session")
    public Session openSession(@RequestBody String sessionId) {
        return sessionService.openSession(sessionId);
    }

    @GetMapping("list-sessions")
    public List<Session> list(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(SESSION_USER);
        return sessionService.list(user);
    }
}
