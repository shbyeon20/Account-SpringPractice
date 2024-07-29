package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.type.AccountStatus;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

import java.time.LocalDateTime;

import static com.example.account.type.AccountStatus.IN_USE;

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
                        new AccountException(ErrorCode.USER_NOT_FOUND));
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

    @Transactional
    public Account getAccount(Long id) {

        return  accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));


    }
}



