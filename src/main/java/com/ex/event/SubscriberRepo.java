package com.ex.event;


import com.ex.model.User;
import com.ex.types.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubscriberRepo {

    private MultiValueMap<String, User> activeSessions = new MultiValueMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void add(String sessionId, User user) {
        logger.info("STORING SESSIONID/SUBSCRIBER => {} : {} ", sessionId, user.getUsername());
        activeSessions.putValue(sessionId, user);
    }

    public void remove(String sessionId, User user) {
        logger.info("REMOVING SESSIONID/SUBSCRIBER => {} : {} ", sessionId, user.getUsername());
        activeSessions.putValue(sessionId, user);
    }

    public MultiValueMap<String, User> getActiveSessions() {
        return activeSessions;
    }
}
