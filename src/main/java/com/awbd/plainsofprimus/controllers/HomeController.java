package com.awbd.plainsofprimus.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    @GetMapping("/login")
    public String showLogInForm(){ return "login"; }

    @GetMapping("/")
    public String showHome(){
        return "index";
    }

    @GetMapping("/login-error")
    public String loginError() {
        return "error_login";
    }

    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }
}
