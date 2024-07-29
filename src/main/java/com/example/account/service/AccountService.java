package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.account.type.AccountStatus.*;
import static com.example.account.type.AccountStatus.IN_USE;
import static com.example.account.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;


    /**
     * // 사용자가 잇는지 조회
     * //  계좌번호를 생성 후
     * // 계좌번호를 저장
     */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance) {

        AccountUser accountUser =
                accountUserRepository.findById(userId).orElseThrow(() ->
                        new AccountException(USER_NOT_FOUND));

        validateCreateAccount(accountUser);


        String accountNumber = accountRepository.findFirstByOrderByIdDesc().
                map(Account -> Integer.parseInt(Account.getAccountNumber()) + 1 + "")
                .orElse("1000000000");


        return AccountDto.fromEntity(
                accountRepository.save(Account.builder().
                        accountUser(accountUser).
                        accountNumber(accountNumber).
                        accountStatus(IN_USE).
                        balance(initialBalance).
                        registeredAt(LocalDateTime.now())
                        .build()));


    }

    private void validateCreateAccount(AccountUser accountUser) {
        if (accountRepository.countByAccountUser(accountUser) >= 10) {
            throw new AccountException(MAX_ACCOUNT_PER_USER_10);
        }
    }

    @Transactional
    public Account getAccount(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));


    }

    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {

        AccountUser accountUser =
                accountUserRepository.findById(userId).
                        orElseThrow(() -> new AccountException(USER_NOT_FOUND));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));

        validateDeleteAccount(accountUser, account);

        account.setAccountStatus(UNREGISTERED);
        account.setUnregisteredAt(LocalDateTime.now());

        account = accountRepository.save(account);


        return AccountDto.fromEntity(account);

    }

    private void validateDeleteAccount(AccountUser accountUser, Account account) throws AccountException {
        if (!Objects.equals(accountUser.getId(), account.getAccountUser().getId())) {
            throw new AccountException(USER_ACCOUNT_UN_MATCH);
        }
        if (account.getAccountStatus() == UNREGISTERED) {
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }

        if (account.getBalance() > 0) {
            throw new AccountException(BALANCE_NOT_EMPTY);
        }
    }

    @Transactional
    public List<AccountDto> getAccountsByUserId(Long userId) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(()->new AccountException(USER_NOT_FOUND));

        List<Account> accounts =
                accountRepository.findByAccountUser(accountUser);

        return accounts.stream().map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }
}



