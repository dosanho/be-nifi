package com.telsoft.libcore.exception;

import com.telsoft.libcore.constant.ResultCode;
import org.springframework.http.HttpStatus;

public class CommandFailureException extends Exception{
    private static final long serialVersionUID = 3485644902954087531L;

    private String code;
    private String desc;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public CommandFailureException(String code, String desc, HttpStatus httpStatus) {
        this.code = code;
        this.desc = desc;
        this.httpStatus = httpStatus;
    }

    public CommandFailureException(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public CommandFailureException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.desc = resultCode.getDesc();
        this.httpStatus = resultCode.getHttpStatus();

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getDesc() {

        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
