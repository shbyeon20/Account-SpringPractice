package com.example.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class DeleteAccount {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request{
        @NotNull
        @Min(1)
        private Long userId;
        @NotBlank
        @Size(min = 10, max = 10)
        private String AccountNumber;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Long userId;
        private String accountNumber;
        private LocalDateTime unRegisteredAt;

        public static Response from (AccountDto accountDto) {
            return Response.builder().
                    accountNumber(accountDto.getAccountNumber())
                    .userId(accountDto.getUserId())
                    .unRegisteredAt(accountDto.getUnregisteredAt())
                    .build();

        }
    }
}
