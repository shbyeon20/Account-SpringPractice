package com.example.account.controller;


import com.example.account.domain.Account;
import com.example.account.dto.AccountDto;
import com.example.account.dto.CreateAccount;
import com.example.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;



    @PostMapping("/account")
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
            ) {

        // service createAccount 결과로 Entity to DTO를 생성하고
        // 그 DTO를 reponse Dto로 변환.
        return CreateAccount.Response.from(
                accountService.createAccount(request.getUserId(),
                request.getInitialBalance()));
    }


    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id) {
        return accountService.getAccount(id);

    }


}
