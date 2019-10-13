package com.itmuch.contentcenter;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LombokTest {
    public static void main(String[] args) {
        LoginDto build = LoginDto.builder().email("ee@qq.com")
                .password("111")
                .mobile("22")
                .build();
        log.info("hahah = {}",build);
    }


}

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
class LoginDto {
    private String email;
    private String password;
    private String mobile;

}
