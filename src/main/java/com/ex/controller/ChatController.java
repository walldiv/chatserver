package com.ex.controller;

import com.ex.model.ChatMessage;
import com.ex.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ChatController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/chatconnect")
    public String getConnectedClient(Model model, HttpSession session) {
        User loggedUser = (User)session.getAttribute("SessionUser");
        if (loggedUser == null) {
            return "index";
        }
        logger.info("USER LOGGED IN -> {}", loggedUser);
        model.addAttribute("user", loggedUser);
        return "chatconnect";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        logger.info("ChatController::sendMessage()" + chatMessage.toString());
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        //Add username in web socket session
        logger.info("ChatController::addUser() -> ADDING CHAT USER");
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
