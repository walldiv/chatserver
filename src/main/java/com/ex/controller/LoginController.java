package com.ex.controller;

import com.ex.model.User;
import com.ex.repository.UserRepository;
import com.ex.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String loadPage(Model model, HttpSession session) {
        System.out.println("GET FIRED");
        model.addAttribute("user", new User());
        User thisUser = (User)session.getAttribute("SessionUser");
        logger.info("SESSION VARIABLE {}", thisUser);
        return "index.html";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute User user, HttpSession session) {
        logger.info("PASSED USER FROM LOGIN FORM {}", user.toString());
        //Hash the incoming password immediatelly
        String hashedPass = userService.hashPassword(user.getPassword());
        user.setPassword(hashedPass);

        //Get the username from database & match password
        User storedUser = userService.getUserByUsername(user);
        if(storedUser != null){
            session.setAttribute("SessionUser", storedUser);
            user = storedUser;
        }
        logger.info("STORED USER IS -> {}", storedUser);
        return "redirect:/chatconnect";
    }

}
