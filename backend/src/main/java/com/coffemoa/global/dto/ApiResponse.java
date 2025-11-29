package com.coffemoa.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

  private int code;        // SUCCESS_CODE = 200
  private String message;  // "OK"
  private T data;

  public static <T> ApiResponse<T> ok(T data) {
    return ApiResponse.<T>builder().code(HttpStatus.SC_OK).message("OK").data(data).build();
  }

  public static ApiResponse<Void> ok() {
    return ApiResponse.<Void>builder().code(HttpStatus.SC_OK).message("OK").build();
  }

  public static <T> ApiResponse<T> error(int code, String message) {
    return ApiResponse.<T>builder().code(code).message(message).data(null).build();
  }
}