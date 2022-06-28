package com.testAPI.controllers;

import com.webdav.model.UserInfo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private static final Map<String, String> passwords = new HashMap<>();

    static {
        passwords.put("user-1", "pass-1");
        passwords.put("user-2", "pass-2");
    }

    @PostMapping
    public Boolean authenticateUser(@RequestBody UserInfo userInfo) {
        if (!passwords.containsKey(userInfo.getUserName())) {
            return false;
        }

        return userInfo.getPassword().equals(passwords.get(userInfo.getUserName()));
    }
}
