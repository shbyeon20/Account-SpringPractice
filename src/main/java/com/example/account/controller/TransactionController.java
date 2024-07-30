package com.example.account.controller;

import com.example.account.dto.UseBalance;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 잔액관련 컨트롤로
 * 1. 잔액사용
 * 2. 잔액사용취소
 * 3. 거래확인
 */

@Slf4j
@RestController
@RequiredArgsConstructor

public class TransactionController {

    @PostMapping("/transaction/use")
    public UseBalance.Response useBalance(
            @Valid @RequestBody UseBalance.Request request
    ){



    }
}
