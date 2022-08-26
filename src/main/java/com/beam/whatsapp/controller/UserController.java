package com.beam.whatsapp.controller;

import com.beam.whatsapp.dto.AuthenticationRequest;
import com.beam.whatsapp.dto.AuthenticationResponse;
import com.beam.whatsapp.model.User;
import com.beam.whatsapp.model.status.UserStatus;
import com.beam.whatsapp.repository.UserRepository;
import com.beam.whatsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

import static com.beam.whatsapp.service.UserService.SESSION_USER;

@Controller
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final SimpMessagingTemplate template;

    @ResponseBody
    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request, HttpSession session) {
        AuthenticationResponse response = userService.login(request.getNumber(), request.getPassword());

        if (response.getCode() == 0) {
            session.setAttribute(SESSION_USER, response.getUser());
        }

        return response;
    }

    @GetMapping("logout")
    public String logout(HttpSession session)
    {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null){
            user.setStatus(UserStatus.OFFLINE);
            user.setLastSeen(new Date());
            userRepository.save(user);
            template.convertAndSend("/whatsapp/logout/" + user.getId(), user);
        }
        session.removeAttribute(SESSION_USER);
        return "redirect:/login";
    }

    @ResponseBody
    @PostMapping("register")
    public void register(User user, @RequestPart MultipartFile file) {
        userService.register(user, file);
    }

    @ResponseBody
    @GetMapping("list-friends")
    public List<User> listFriends(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        return userService.listFriends(user);
    }

    @ResponseBody
    @PostMapping("add-friend")
    public void addFriend(@RequestBody String number, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        userService.addFriend(number, user);
    }
}
