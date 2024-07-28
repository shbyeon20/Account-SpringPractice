package com.example.account.dto;

import com.example.account.domain.Account;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class CreateAccount {

    @Getter
    @Setter

    public static class Request{
        @NotNull
        @Min(1)
        private Long userId;
        @NotNull
        @Min(100)
        private Long initialBalance;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Long userId;
        private String accountNumber;
        private LocalDateTime registeredAt;

        public static Response from (AccountDto accountDto) {
            return Response.builder().
                    accountNumber(accountDto.getAccountNumber())
                    .userId(accountDto.getUserId())
                    .registeredAt(accountDto.getRegisteredAt())
                    .build();

        }
    }
}
