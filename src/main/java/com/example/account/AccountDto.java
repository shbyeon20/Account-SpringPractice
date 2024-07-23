package com.example.account;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@Slf4j
public class AccountDto {
    private String accountNumber;
    private String nickname;
    private LocalTime registeredAt;

    public void log(){
        log.error("error occured");
    }
}
