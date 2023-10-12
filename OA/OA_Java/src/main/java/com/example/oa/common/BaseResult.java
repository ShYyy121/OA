package com.example.oa.common;

import org.springframework.http.ResponseEntity;

public class BaseResult<T> {
    private int code;
    private String msg;
    private T data;

    public BaseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <D> ResponseEntity<BaseResult<D>> ok(D data) {
        return ResponseEntity.ok(new BaseResult<>(200, "成功", data));
    }

    public static <D> ResponseEntity<BaseResult<D>> ok(String msg, D data) {
        return ResponseEntity.ok(new BaseResult<>(200, msg, data));
    }

    public static <D> ResponseEntity<BaseResult<D>> ok(String msg) {
        return ResponseEntity.ok(new BaseResult<>(200, msg, null));
    }

    public static <D> ResponseEntity<BaseResult<D>> error(String msg) {
        return ResponseEntity.ok(new BaseResult<>(201, msg, null));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
