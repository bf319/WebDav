package com.webdav.configurations;

import com.webdav.model.UserInfo;
import io.milton.http.Auth;
import io.milton.http.Request;
import io.milton.http.SecurityManager;
import io.milton.http.http11.auth.DigestResponse;
import io.milton.resource.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class CustomSecurityManager implements SecurityManager {
    private final static String loginBaseURI = "http://localhost:8081/api/login/";
    private static final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Object authenticate(DigestResponse digestResponse) {
        return null;
    }

    @Override
    public Object authenticate(String s, String s1) {
        try {
            URI uri = new URI(loginBaseURI);
            ResponseEntity<Boolean> response = restTemplate.postForEntity(uri, new UserInfo(s, s1), Boolean.class);

            if (Boolean.TRUE.equals(response.getBody())) {
                return s;
            } else {
                return null;
            }

        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public boolean authorise(Request request, Request.Method method, Auth auth, Resource resource) {
        // Same implementation as the SimpleSecurityManager from Milton
        if (auth == null) {
            return false;
        } else return auth.getTag() != null;
    }

    @Override
    public String getRealm(String s) {
        return "mock-demo-realm";
    }

    @Override
    public boolean isDigestAllowed() {
        return false;
    }
}
