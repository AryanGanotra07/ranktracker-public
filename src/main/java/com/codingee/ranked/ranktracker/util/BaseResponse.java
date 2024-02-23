package com.codingee.ranked.ranktracker.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;

@ToString
@Setter
@Getter
public class BaseResponse<T> {
    private HttpStatus status;
    private T data;
    private String error;
    private String userMessage;
    private String message;
    private Boolean success;

    private Map<String, Object> metadata = new HashMap<>();

    public BaseResponse(T data, HttpStatus status, String error, Boolean success, String userMessage) {
        this.status = status;
        this.data = data;
        this.error = error;
        this.success = success;
        this.userMessage = userMessage;
    }

    public static <G>BaseResponse<G> success(G data){
        return new BaseResponse<>(data, HttpStatus.OK, null, Boolean.TRUE, null);
    }

    public static <G>BaseResponse<G> successWithMsg(G data, String msg){
        return new BaseResponse<>(data, HttpStatus.OK, null, Boolean.TRUE, msg);
    }

    public static <G>BaseResponse<G> created(G data){
        return new BaseResponse<>(data, HttpStatus.CREATED, null, Boolean.TRUE, null);
    }

    public static <G>BaseResponse<G> createdWithMsg(G data, String msg){
        return new BaseResponse<>(data, HttpStatus.CREATED, null, Boolean.TRUE, msg);
    }

    public static BaseResponse<String> badRequest(){
        return new BaseResponse<> (null, HttpStatus.BAD_REQUEST, null, Boolean.FALSE, null);
    }

    public static BaseResponse<String> badRequest(String errorMessage){
        return new BaseResponse<> (null, HttpStatus.BAD_REQUEST, errorMessage, Boolean.FALSE, null);
    }

    public static BaseResponse<String> notFound(){
        return new BaseResponse<> (null, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), Boolean.FALSE, null);
    }

    public static BaseResponse<String> notFound(String msg){
        return new BaseResponse<> (null, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), Boolean.FALSE, msg);
    }

    public static BaseResponse<String> conflict(){
        return new BaseResponse<> (null, HttpStatus.CONFLICT, HttpStatus.CONFLICT.name(), Boolean.FALSE, null);
    }

    public static BaseResponse<String> conflict(String msg){
        return new BaseResponse<> (null, HttpStatus.CONFLICT, HttpStatus.CONFLICT.name(), Boolean.FALSE, msg);
    }

    public static BaseResponse<String> internalServerError(String errorMessage){
        return new BaseResponse<> (null, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, Boolean.FALSE, null);
    }

    public static BaseResponse<Null> unauthorized(String msg) {
        return new BaseResponse<>(null, HttpStatus.UNAUTHORIZED, msg, Boolean.FALSE, "Please login again to access this");
    }

    public static BaseResponse<Null> forbidden(String msg) {
        return new BaseResponse<>(null, HttpStatus.FORBIDDEN, msg, Boolean.FALSE, "You are not allowed to access this feature");
    }

}
