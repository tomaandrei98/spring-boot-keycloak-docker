package com.example.keycloak.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/resource")
public class DemoController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('client_user')")
    public String userEndpoint(Principal principal) {
        return "Hello " + principal.getName() + ", you have USER access!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('client_admin')")
    public String adminEndpoint(Principal principal) {
        return "Hello " + principal.getName() + ", you have ADMIN access!";
    }
}

