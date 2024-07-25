package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.type.AccountStatus;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository ;



    /**
     *
     // 사용자가 잇는지 조회
     //  계좌번호를 생성 후
     // 계좌번호를 저장
     */
    @Transactional
    public void createAccount(Long userId, Long InitialBalance) {

        accountUserRepository.findById(userId).orElseThrow(() -> new AccountNotFoundException(userId));

        Account account = Account.builder()
                .accountNumber("40000")
                .accountStatus(AccountStatus.IN_USE)
                .build();
         accountRepository.save(account);
    }

    @Transactional
    public Account getAccount(Long id) {
        return accountRepository.findById(id).get();


    }
}



