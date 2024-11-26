package com.arcfit.murasaki.model;

public class BaseResponse<T> {
    public boolean success;
    public String message;
    public T payload;

    public T getData() {
        return payload;
    }
}
