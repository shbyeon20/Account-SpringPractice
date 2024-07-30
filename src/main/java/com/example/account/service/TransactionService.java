package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.TrasnactionDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;




    /**
     * 사용자가 없는 경우, 사용자 아이디와 계좌소유주가 다른 경우,
     * 계좌가 이미 해지되어 있는 경우, 거래금액 잔액보다 큰 경우
     * 거래금액이 너무 작거나 큰 경우 실패응답
     *
     */
    @Transactional
    public TrasnactionDto useBalance(Long userId, String accountNumber,
                                     Long amount){
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() ->new AccountException(ErrorCode.USER_NOT_FOUND));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateUseBalance(account, accountUser, amount);



    }

    private static void validateUseBalance(Account account,
                                           AccountUser accountUser, Long amount) {
        if(!Objects.equals(accountUser.getId(), account.getAccountUser().getId())){
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH);
        }
        if(account.getAccountStatus()!= AccountStatus.IN_USE){
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }
        if(account.getBalance()<amount){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }


    }


}
