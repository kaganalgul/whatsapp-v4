package com.beam.whatsapp.controller;

import com.beam.whatsapp.model.User;
import com.beam.whatsapp.service.DiskService;
import com.beam.whatsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.beam.whatsapp.service.UserService.SESSION_USER;

@RequiredArgsConstructor
@Controller
@Log4j2
public class PageController {
    private final DiskService diskService;

    @GetMapping(value = {"/", "/home", "/session"}, produces = {MediaType.TEXT_HTML_VALUE})
    public String home(HttpSession session, Model model) {
        if (session.getAttribute(SESSION_USER) == null) {
            return "redirect:/login";
        } else {
            User user = (User) session.getAttribute(SESSION_USER);
            model.addAttribute("current_user", user);
            return "index";
        }
    }

    @GetMapping(value = {"/login", "/register", "/logout"}, produces = {MediaType.TEXT_HTML_VALUE})
    public String login() {
        return "index";
    }

    @GetMapping("new-person")
    public String newPerson() {
        return "new-person";
    }

    @PostMapping("new-person")
    public String newPerson(User user, @RequestPart MultipartFile file) {

        //log.info("Person added", firstName, lastName);

        //userService.save(user, file);

        log.info(user);

        return "redirect:/new-person";
    }

    @GetMapping("/img/{filename}")
    public ResponseEntity<byte[]> image(@PathVariable String filename) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity(diskService.read(filename), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.of(null);
        }
    }
}
