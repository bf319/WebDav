package com.testAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class APILauncher {

    public static void main(String[] args) {
        SpringApplication testAPI = new SpringApplication(APILauncher.class);
        testAPI.setDefaultProperties(Collections.singletonMap("server.port", "8081")); // Port 8080 is used by the WebDav server
        testAPI.run(args);
    }

}