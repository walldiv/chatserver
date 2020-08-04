package com.ex.controller;

import com.ex.event.SubscriberRepo;
import com.ex.model.ChatMessage;
import com.ex.model.User;
import com.ex.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpSubscriptionMatcher;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ChatController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired private SubscriberRepo subscriberRepo;
    @Autowired private UserRepository userRepository;




    @GetMapping("/chatconnect")
    public String getConnectedClient(Model model, HttpSession session) {
        User loggedUser = (User)session.getAttribute("SessionUser");
        if (loggedUser == null) {
            return "index";
        }
        logger.info("ChatController::getConnectedClient()  USER LOGGED IN -> {}", loggedUser);
        model.addAttribute("user", loggedUser);
        return "chatconnect";
    }

    @MessageMapping("/chat.sendMessage.{dest}")
//    @SendTo("/{dest}")
    public void sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String dest) {
        logger.info("ChatController::sendMessage() => {}, TO DESTINATION:{}", chatMessage.toString(), dest);
        simpMessagingTemplate.convertAndSend("/topic/chat." + dest, chatMessage);
    }

    @MessageMapping("/chat.addUser.{dest}")
//    @SendTo("/topic/public")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String dest) {
        //Add username in web socket session
        logger.info("ChatController::addUser() -> ADDING CHAT USER TO CHANNEL => {}", dest);
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        simpMessagingTemplate.convertAndSend("/topic/chat." + dest, chatMessage);
    }

    /**
     * This function allows for a requesting-client-only return upon a subscription.  The subscription ONLY picks up
     * on "/app/chat.{dest}".... Because we are using "/topic/chat.{dest} to send messages to - the connected clients
     * are never sent messages about this.
     * @param dest
     * @param username - @Header will allow custom headers to be mapped in via JS from client.  NEED TO DO - try mapping objects!
     * @return - any type/literal that you want to pass back to the client.
     */
    @SubscribeMapping("/chat.{dest}")
    public Collection<String> SubscribeCustom(@DestinationVariable String dest, @Header("username") String username) {
        Collection<String> users = new ArrayList<>();
        User thisUser = userRepository.findByUsername(username);
        if(thisUser != null) {
            subscriberRepo.add(dest, thisUser);
            Collection<User> tempusers = subscriberRepo.getActiveSessions().getValues(dest);
            for (User u : tempusers) {
                users.add(u.getUsername());
            }
            logger.info("ChatController::SubscribeCustom() USERS IN CHANNEL: /app/chat.{} => {}", dest, users);
        }
//        logger.info("HEADER PASSED => {}      PASSED USERNAME => {}", dest, username);
        return(users);
    }
}
