package com.itmuch.usercenter.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//校验授权的注解
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {
    String value();
}
