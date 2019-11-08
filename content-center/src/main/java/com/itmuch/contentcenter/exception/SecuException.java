package com.itmuch.contentcenter.exception;

public class SecuException extends RuntimeException {

    private String msg;
    public SecuException(String s) {
        this.msg = s;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
}
