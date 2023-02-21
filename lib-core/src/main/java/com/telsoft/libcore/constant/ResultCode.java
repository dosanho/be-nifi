package com.telsoft.libcore.constant;

import org.springframework.http.HttpStatus;

public enum ResultCode {
    SUCCESS(Code.SUCCESS, "SUCCESS", HttpStatus.OK),
    INTERNAL_SERVER_ERROR(Code.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(Code.UNAUTHORIZED, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIAL(Code.BAD_CREDENTIAL, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    ACCOUNT_IS_LOCKED(Code.ACCOUNT_IS_LOCKED, "ACCOUNT IS LOCKED", HttpStatus.UNAUTHORIZED),
    ACCOUNT_IS_DISABLED(Code.ACCOUNT_IS_DISABLED, "ACCOUNT IS DISABLED", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED(Code.PERMISSION_DENIED, "ACCESS DENIED", HttpStatus.FORBIDDEN),
    OTHER_SERVLET_ERROR(Code.OTHER_SERVLET_ERROR, "OTHER SERVLET ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    TIMEOUT_ERROR(Code.TIMEOUT_ERROR, "TIMEOUT_ERROR", HttpStatus.REQUEST_TIMEOUT),
    LOST_CONNECTION_ERROR(Code.LOST_CONNECTION_ERROR, "LOST CONNECTION ERROR", HttpStatus.SERVICE_UNAVAILABLE),
    MISSING_PARAMETER(Code.MISSING_PARAMETER, "MISSING PARAMETER", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER(Code.INVALID_PARAMETER, "INVALID PARAMETER", HttpStatus.BAD_REQUEST),
    INVALID_DATA(Code.INVALID_DATA, "INVALID DATA", HttpStatus.BAD_REQUEST),
    NOT_FOUND(Code.NOT_FOUND, "API NOT FOUND", HttpStatus.NOT_FOUND),
    METHOD_ARGUMENT_TYPE_MISMATCH(Code.METHOD_ARGUMENT_TYPE_MISMATCH, "API NOT FOUND", HttpStatus.BAD_REQUEST),
    REQUEST_METHOD_NOT_SUPPORTED(Code.REQUEST_METHOD_NOT_SUPPORTED, "API NOT FOUND", HttpStatus.BAD_REQUEST),
    CONSTRAIN_VIOLATION(Code.CONSTRAIN_VIOLATION, "CONSTRAIN VIOLATION", HttpStatus.METHOD_NOT_ALLOWED),
    OBJECT_NOT_EXIST(Code.OBJECT_NOT_EXIST, "OBJECT NO EXIST", HttpStatus.NOT_FOUND),
    OBJECT_EXISTED(Code.OBJECT_EXISTED, "OBJECT EXISTED", HttpStatus.OK),
    NO_DATA_FOUND(Code.NO_DATA_FOUND, "NO DATA FOUND", HttpStatus.OK),
    ELEMENT_ALREADY_DELETED(Code.ELEMENT_ALREADY_DELETED, "ELEMENT ALREADY DELETED", HttpStatus.METHOD_NOT_ALLOWED),
    ELEMENT_ALREADY_SAVED(Code.ELEMENT_ALREADY_SAVED, "ELEMENT ALREADY SAVED", HttpStatus.METHOD_NOT_ALLOWED),
    API_NOT_EXISTED(Code.API_NOT_EXISTED, "API NOT FOUND", HttpStatus.BAD_REQUEST),
    INVALID_FILE(Code.INVALID_FILE, "INVALID FILE", HttpStatus.OK);

    private final String code;
    private final String desc;
    private final HttpStatus httpStatus;

    ResultCode(String code, String desc, HttpStatus httpStatus) {
        this.code = code;
        this.desc = desc;
        this.httpStatus = httpStatus;
    }

    public static final String fromDesc(String code) {
        for (ResultCode e : values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public static final ResultCode fromCode(String code) {
        for (ResultCode e : values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        return ResultCode.INTERNAL_SERVER_ERROR;
    }

    public static final ResultCode fromExCode(int code) {
        for (ResultCode e : values()) {
            if (e.getHttpStatus().value() == code) {
                return e;
            }
        }
        return null;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public interface Code {
        String SUCCESS = "0";
        String ERR_OTHER = "ERR_OTHER";
        String TIMEOUT_ERROR = "TIMEOUT_ERROR";
        String LOST_CONNECTION_ERROR = "LOST_CONNECTION_ERROR";
        String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
        String UNAUTHORIZED = "UNAUTHORIZED";
        String PERMISSION_DENIED = "PERMISSION_DENIED";
        String OTHER_SERVLET_ERROR = "OTHER_SERVLET_ERROR";
        String MISSING_PARAMETER = "MISSING_PARAMETER";
        String INVALID_PARAMETER = "INVALID_PARAMETER";
        String INVALID_DATA = "INVALID_DATA";
        String NOT_FOUND = "NOT_FOUND";
        String CONSTRAIN_VIOLATION = "CONSTRAIN_VIOLATION";
        String METHOD_ARGUMENT_TYPE_MISMATCH = "METHOD_ARGUMENT_TYPE_MISMATCH";
        String REQUEST_METHOD_NOT_SUPPORTED = "REQUEST_METHOD_NOT_SUPPORTED";
        String OBJECT_EXISTED = "OBJECT_EXISTED";
        String OBJECT_NOT_EXIST = "OBJECT_NOT_EXIST";
        String NO_DATA_FOUND = "NO_DATA_FOUND";
        String BAD_CREDENTIAL = "BAD_CREDENTIAL";
        String ACCOUNT_IS_LOCKED = "ACCOUNT_IS_LOCKED";
        String ACCOUNT_IS_DISABLED = "ACCOUNT_IS_DISABLED";
        String ELEMENT_ALREADY_DELETED = "ELEMENT_ALREADY_DELETED";
        String ELEMENT_ALREADY_SAVED = "ELEMENT_ALREADY_SAVED";
        String API_NOT_EXISTED = "API_NOT_EXISTED";
        String INVALID_FILE = "INVALID_FILE";
        String CANNOT_BE_EMPTY = "CANNOT_BE_EMPTY";
        String REQUIRED_SUFFIX = "_REQUIRED_";

        String FILTER_ERR_1001 = "FILTER_ERR_1001";
        String FILTER_ERR_1002 = "FILTER_ERR_1002";
        String FILTER_ERR_1003 = "FILTER_ERR_1003";
        String FILTER_ERR_1004 = "FILTER_ERR_1004";
        String FILTER_ERR_1005 = "FILTER_ERR_1005";
        String FILTER_ERR_1006 = "FILTER_ERR_1006";
        String FILTER_ERR_1007 = "FILTER_ERR_1007";
        String FILTER_ERR_1008 = "FILTER_ERR_1008";
        String FILTER_ERR_1009 = "FILTER_ERR_1009";

        String EAV_ENTITY_SEARCH_ERR_1001 = "EAV_ENTITY_SEARCH_ERR_1001";
        String EAV_ENTITY_SEARCH_ERR_1002 = "EAV_ENTITY_SEARCH_ERR_1002";
        String EAV_ENTITY_SEARCH_ERR_1003 = "EAV_ENTITY_SEARCH_ERR_1003";
        String EAV_ENTITY_SEARCH_ERR_1004 = "EAV_ENTITY_SEARCH_ERR_1004";
        String EAV_ENTITY_SEARCH_ERR_1005 = "EAV_ENTITY_SEARCH_ERR_1005";
    }

}
