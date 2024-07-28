package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountUserRepository accountUserRepository;


        @Test
        void testCreateAccount () {
            accountUserRepository.save(AccountUser.builder().id(1L).name(
                    "proro").build());

            AccountDto accountdto = accountService.createAccount(1L, 1000L);

            assertEquals("1000000000",accountdto.getAccountNumber());
            assertEquals(1000,accountdto.getAccountNumber());




        }

    }