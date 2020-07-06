package com.ex.service;

import com.ex.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    //GetALlUsers
    public List<User> findAll();

    //GetUserByID
    public Optional<User> getUserById(User user);

    //GetUserByUsername
    public User getUserByUsername(User user);
}
