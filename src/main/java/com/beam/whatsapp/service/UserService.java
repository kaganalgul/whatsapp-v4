package com.beam.whatsapp.service;

import com.beam.whatsapp.dto.AuthenticationResponse;
import com.beam.whatsapp.model.User;
import com.beam.whatsapp.model.status.UserStatus;
import com.beam.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String SESSION_USER = "user";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate template;
    private final DiskService diskService;

    public AuthenticationResponse login(String number, String password) {

        Optional<User> optionalUser = userRepository.findByNumber(number);

        if (optionalUser.isEmpty()) {
            return new AuthenticationResponse()
                    .setCode(11);
        } else {
            User user = optionalUser.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setStatus(UserStatus.ONLINE);
                userRepository.save(user);
                template.convertAndSend("/whatsapp/login/" + user.getId(), user);
                return new AuthenticationResponse()
                        .setUser(user)
                        .setCode(0);

            } else {
                return new AuthenticationResponse()
                        .setCode(10);
            }
        }
    }

    public void register(User user, MultipartFile file) {
        String filename = null;

        try {
            filename = diskService.write(file.getBytes(), FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        if (!userRepository.findByNumber(user.getNumber()).isPresent()) {
            User newUser = new User()
                    .setName(user.getName())
                    .setSurname(user.getSurname())
                    .setNumber(user.getNumber())
                    .setPassword(passwordEncoder.encode(user.getPassword()))
                    .setAvatar(filename);

            userRepository.save(newUser);
        }
    }

    public void addFriend(String number, User user) {
        if (!user.getAddedNumbers().contains(number) && number != user.getNumber()) {
            user.getAddedNumbers().add(number);
            Optional<User> optUser = userRepository.findByNumber(number);
            if (optUser.isPresent()) {
                User addedUser = optUser.get();
                template.convertAndSend("/whatsapp/list-friends/" + user.getId(), addedUser);
            }
            userRepository.save(user);
        }
    }

    public List<User> listFriends(User user) {
        if (user.getAddedNumbers().size() >= 1) {
            List<User> users = new ArrayList<>();
            for (String n : user.getAddedNumbers()) {
                Optional<User> optUser = userRepository.findByNumber(n);
                if (optUser.isPresent()) {
                    User addedUser = optUser.get();
                    users.add(addedUser);
                }
            }
            return users;

        } else {
            return null;
        }

    }
}


