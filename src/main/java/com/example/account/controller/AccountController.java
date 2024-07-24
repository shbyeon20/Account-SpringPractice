package com.example.account.controller;


import com.example.account.dto.CreateAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    @PostMapping
    public String createAccount(
            @RequestBody @Valid CreateAccount.Request request){
        return "";
    }

}
