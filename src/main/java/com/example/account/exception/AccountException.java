package com.example.account.exception;

import com.example.account.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountException extends RuntimeException {
   private ErrorCode errorCode;
   private String Errormessage;

   public AccountException(ErrorCode errorCode) {
      this.errorCode = errorCode;
      this.Errormessage = errorCode.getDescription();
   }
}
