package com.ex;

import com.ex.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan({"com.ex.controller", "com.ex.model"})
//@EnableJpaRepositories("com.ex.repository")
public class ChatServerApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepository dao;

	public static void main(String[] args) {
		SpringApplication.run(ChatServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("USERS -> {}", dao.findAll());
	}
}
