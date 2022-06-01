package com.kaellum.walkmydog.authentication.services;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kaellum.walkmydog.authentication.collections.User;


public interface UserService {
    User saveUser(User user);
    User getUser(String username);
    List<User>getUsers();
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
