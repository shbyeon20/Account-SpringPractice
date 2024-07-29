package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
                .willReturn(Optional.of(
                        Account.builder()
                                .accountNumber("1000000012")
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


    @Test
    void createFirstAccountSuccess() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        AccountUser.builder()
                                .id(15L)
                                .name("Pobi")
                                .build()));

        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.empty());

        given(accountRepository.save(any())).willReturn(
                Account.builder()
                        .accountUser(
                                AccountUser.builder()
                                        .id(15L)
                                        .name("Pobi")
                                        .build())
                        .accountNumber("1000000013")
                        .build());

        //when
        AccountDto accountDto = accountService.createAccount(1L, 100L);


        //then
        assertEquals(15, accountDto.getUserId());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals("1000000000", captor.getValue().getAccountNumber());
    }

    @Test
    @DisplayName("계좌생성실패")
    void createAccountUserNotFound() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());


        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.createAccount(1L, 100L));


        //then

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 당 최대 계좌는 10개")
    void createAccount_MaxAccountIs10() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        AccountUser.builder()
                                .id(15L)
                                .name("Pobi")
                                .build()));

        given(accountRepository.countByAccountUser(any()))
                .willReturn(10);
        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.createAccount(1L, 100L));


        //then

        assertEquals(ErrorCode.MAX_ACCOUNT_PER_USER_10, exception.getErrorCode());
    }


    @Test
    void deleteAccountSuccess() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(
                        Account.builder()
                                .accountUser(pobi)
                                .accountNumber("1000000012")
                                .accountStatus(AccountStatus.IN_USE)
                                .balance(0L)
                                .build()));

        given(accountRepository.save(any())).willReturn(
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("1000000012")
                        .unregisteredAt(LocalDateTime.now())
                        .balance(0L)
                        .build());

        //when
        AccountDto accountDto = accountService.deleteAccount(1L, "1001234");


        //then
        assertEquals(12, accountDto.getUserId());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals("1000000012", captor.getValue().getAccountNumber());
        assertEquals(AccountStatus.UNREGISTERED, captor.getValue().getAccountStatus());
    }

    @Test
    @DisplayName("계좌생성실패")
    void deleteAccountUserNotFound() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.deleteAccount(1L, "1234567890"));

        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("해당계좌없음 - 계좌해지 실패")
    void deleteAccount_AccountNotFound() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.empty());


        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.deleteAccount(1L, "1234567890"));

        //then
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("계좌소유주다름")
    void deleteAccountFailed_userUnmatched() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();

        AccountUser harry = AccountUser.builder()
                .id(13L)
                .name("Harry")
                .build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(
                        Account.builder()
                                .accountUser(harry)
                                .accountNumber("1000000012")
                                .accountStatus(AccountStatus.IN_USE)
                                .balance(0L)
                                .build()));

        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.deleteAccount(1L, "1234567890"));

        //then
        assertEquals(ErrorCode.USER_ACCOUNT_UN_MATCH, exception.getErrorCode());
    }


    @Test
    @DisplayName("해지계좌는 잔액이 없어야한다")
    void deleteAccountFailed_balanceNotEmpty() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();


        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(
                        Account.builder()
                                .accountUser(pobi)
                                .accountNumber("1000000012")
                                .accountStatus(AccountStatus.IN_USE)
                                .balance(100L)
                                .build()));

        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.deleteAccount(1L, "1234567890"));

        //then
        assertEquals(ErrorCode.BALANCE_NOT_EMPTY, exception.getErrorCode());
    }

    @Test
    @DisplayName("해지계좌는 해지할 수 없다")
    void deleteAccountFailed_alreadyUnregistered() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();


        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(
                        Account.builder()
                                .accountUser(pobi)
                                .accountNumber("1000000012")
                                .accountStatus(AccountStatus.UNREGISTERED)
                                .balance(100L)
                                .build()));

        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.deleteAccount(1L, "1234567890"));

        //then
        assertEquals(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED, exception.getErrorCode());
    }

    @Test
    void successGetAccountsByUserId() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));

        List<Account> accounts = Arrays.asList(
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("1111111111")
                        .balance(1000L)
                        .build(),
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("2222222222")
                        .balance(2000L)
                        .build(),
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("3333333333")
                        .balance(3000L)
                        .build()
        );

        given(accountRepository.findByAccountUser(any()))
                .willReturn(accounts);

        //when
        List<AccountDto> accountDtos = accountService.getAccountsByUserId(1L);

        //then
        assertEquals(accountDtos.size(), 3);
        assertEquals(accountDtos.get(0).getAccountNumber(), "1111111111");
        assertEquals(accountDtos.get(0).getBalance(), 1000L);
        assertEquals(accountDtos.get(1).getAccountNumber(), "2222222222");
        assertEquals(accountDtos.get(1).getBalance(), 2000L);
        assertEquals(accountDtos.get(2).getAccountNumber(), "3333333333");
        assertEquals(accountDtos.get(2).getBalance(), 3000L);
    }

    @Test
    void failedToGetAccount() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        AccountException exception = assertThrows(AccountException.class
                , () -> accountService.getAccountsByUserId(1L));

        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}