package com.ex.service;

import com.ex.model.User;
import com.ex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    public String hashPassword(String password) {
        StringBuilder hash = new StringBuilder();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for(int itr = 0; itr < hashedBytes.length; itr++) {
                byte b = hashedBytes[itr];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    @Override
    public List<User> findAll() {
        List<User> users = (List<User>)userRepository.findAll();
        return users;
    }

    public Optional<User> getUserById(User user){
        return userRepository.findById(user.getId());
    }

    public User getUserByUsername(User user) {
        System.out.println(user.toString());
        return userRepository.findByUserPass(user.getUsername(), user.getPassword());
    }
}
