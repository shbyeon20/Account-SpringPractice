package com.example.account.controller;


import com.example.account.domain.Account;
import com.example.account.dto.AccountInfo;
import com.example.account.dto.CreateAccount;
import com.example.account.dto.DeleteAccount;
import com.example.account.dto.ErrorResponse;
import com.example.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


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

    @DeleteMapping("/account")
    public DeleteAccount.Response createAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {

        // service createAccount 결과로 Entity to DTO를 생성하고
        // 그 DTO를 reponse Dto로 변환.
        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()));
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountByUserId(
            @RequestParam("user_Id") Long userId) {

        return accountService.getAccountsByUserId(userId).stream()
                .map(accountDto ->
                        AccountInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance()).build())
                .collect(Collectors.toList());

    }


    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id) {
        return accountService.getAccount(id);

    }



}
