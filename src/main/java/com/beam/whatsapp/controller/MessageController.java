package com.beam.whatsapp.controller;

import com.beam.whatsapp.dto.SendMessageRequest;
import com.beam.whatsapp.model.Message;
import com.beam.whatsapp.model.Session;
import com.beam.whatsapp.model.User;
import com.beam.whatsapp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.beam.whatsapp.service.UserService.SESSION_USER;

@RequiredArgsConstructor
@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("send")
    public void send(@RequestBody SendMessageRequest request, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        messageService.sendMessage(request.getSessionId(), request.getFriendId(), user, request.getMessage());
    }

    @GetMapping("list/{id}")
    public List<Message> list(@PathVariable String id) {
        return messageService.list(id);
    }
}
