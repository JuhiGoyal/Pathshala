package com.pathshala.service.interfaces;

import com.pathshala.entities.User;

public interface UserService {

    boolean isValidUser(User user);
    boolean isUserAlreadyExists(String email);

    User getUserByEmail(String email);

    User save(User user);

    void deleteById(Integer id);
}
