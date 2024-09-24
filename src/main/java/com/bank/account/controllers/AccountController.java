package com.bank.account.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @GetMapping("/")
    public String create() {
        return "Welcome to the Home Page!";
    }
}
