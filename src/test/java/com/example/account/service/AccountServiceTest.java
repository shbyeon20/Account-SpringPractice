package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountSuccess() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        AccountUser.builder()
                                .id(12L)
                                .name("Pobi")
                                .build()));

        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.of(Account.builder().accountNumber(
                                "1000000012")
                        .build()));

        given(accountRepository.save(any())).willReturn(
                Account.builder()
                        .accountUser(
                                AccountUser.builder()
                                        .id(12L)
                                        .name("Pobi")
                                        .build())
                        .accountNumber("1000000013")
                        .build());




        //when
        AccountDto accountDto = accountService.createAccount(1L, 100L);


        //then
        assertEquals(12, accountDto.getUserId());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals("1000000013", captor.getValue().getAccountNumber());


    }


}