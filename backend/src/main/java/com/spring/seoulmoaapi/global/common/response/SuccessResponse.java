package com.spring.seoulmoaapi.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String status; // SUCCESS or ERROR
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>("SUCCESS", message, data);
    }

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>("SUCCESS", "요청이 성공했습니다.", data);
    }

    public static <T> SuccessResponse<T> success() {
        return new SuccessResponse<>("SUCCESS", "요청이 성공했습니다.", null);
    }

    public static <T> SuccessResponse<T> error(String message) {
        return new SuccessResponse<>("ERROR", message, null);
    }
}
