package com.itmuch.usercenter.auth;

import com.itmuch.usercenter.security.MySecurityException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(MySecurityException.class)
    public ResponseEntity error(MySecurityException e) {
        log.info("发生 MySecurityException", e);
        ResponseEntity<ErrorBody> response = new ResponseEntity<>(
                ErrorBody.builder()
                        .msg("token不合法或失效 用户不允许访问")
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .build()
                ,
                HttpStatus.UNAUTHORIZED
        );
        return response;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ErrorBody {
    private String msg;
    private int code;
}
