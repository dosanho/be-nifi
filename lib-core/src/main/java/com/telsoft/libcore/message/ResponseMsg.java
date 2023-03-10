package com.telsoft.libcore.message;

import com.telsoft.libcore.util.Utils;
import com.telsoft.libcore.exception.CommandFailureException;
import com.telsoft.libcore.model.DetailError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import  com.telsoft.libcore.constant.FieldNames;
import  com.telsoft.libcore.constant.ResultCode;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;


public class ResponseMsg<T> extends ResponseEntity<ResponseMsg.WrapContent> {

    private static final String REGEX_DETAIL_ERROR = "interpolatedMessage='([a-zA-Z0-9_]+)'";
    private static final String REGEX_HIBERNATE_CONSTRAINT_ERROR = "\\sconstraint\\s\\[.+\\.(.+)\\]";//could not execute statement; SQL [n/a]; constraint [PCAT_OWNER_CBS.FK_EAV_ENTI_REFERENCE_EAV_ATT9]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
    private static final String REGEX_CONSTRAINT_ERROR = "ORA-(\\d+):(.*?)constraint\\s\\((.*?)\\)\\sviolated";//ORA-02292: integrity constraint (PCAT_OWNER_CBS.FK_EAV_ENTI_REFERENCE_EAV_ATT9) violated - child record found
    private static final String REGEX_JPA_ERROR = "messageTemplate='(.*?)'"; //ConstraintViolationImpl{interpolatedMessage='EAV_ATTRIBUTE_SET_ERR_1002', propertyPath=attributeSetName, rootBeanClass=class com.vn.telsoft.api.model.EavAttributeSet, messageTemplate='EAV_ATTRIBUTE_SET_ERR_1002'}

    protected ResponseMsg(HttpStatus status) {
        super(status);
    }

    protected ResponseMsg(@Nullable WrapContent body, HttpStatus status) {
        super(body, status);
    }

    protected ResponseMsg(@Nullable WrapContent body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    protected ResponseMsg(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public T getDataContent() throws Exception {
        if (getBody() != null) {
            if (getBody().get("data") instanceof PageImpl) {
                return (T) ((PageImpl) getBody().get("data")).getContent();
            } else {
                return (T) getBody().get("data");
            }
        }

        return null;
    }

    // Exception Response
    public static ResponseMsg newExceptionResponse(String logId, ResultCode resultCode, String dataName, Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, dataName, body);
        wrapContent.put(FieldNames.LOG_ID, logId);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newExceptionResponse(String logId, ResultCode resultCode) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode);
        wrapContent.put(FieldNames.LOG_ID, logId);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newExceptionResponse(String logId, ResultCode resultCode, String exDescription) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode.getCode(), exDescription);
        wrapContent.put(FieldNames.LOG_ID, logId);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newExceptionResponse(String logId, HttpStatus httpStatus, String resultCode, String message) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, message);
        wrapContent.put(FieldNames.LOG_ID, logId);
        return new ResponseMsg<>(wrapContent, httpStatus);
    }

    public static ResponseMsg newExceptionResponse(String logId, HttpStatus httpStatus, String message) {
        WrapContent<Object> wrapContent = new WrapContent<>(message);
        wrapContent.put(FieldNames.LOG_ID, logId);
        return new ResponseMsg<>(wrapContent, httpStatus);
    }
    // End Exception Response

    public static ResponseMsg newDtlErrResponse(ResultCode resultCode, Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, FieldNames.DETAIL_ERROR, new Object[]{body});
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newResponse(ResultCode resultCode, String dataName, Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, dataName, body);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newResponseRequired(ResultCode resultCode, String dataName, Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, dataName, body);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newResponse(ResultCode resultCode, String dataName, Exception ex) {
        String strEx = ex.getMessage();
        String strExStackTrace = ExceptionUtils.getStackTrace(ex);
        List<String> body = new ArrayList<>();

        if (strEx != null && strEx.contains("Could not commit JPA transaction")) {
            Pattern pattern = Pattern.compile(REGEX_JPA_ERROR);
            Matcher matcher = pattern.matcher(strExStackTrace);

            while (matcher.find()) {
                body.add(matcher.group(1));
            }

        } else if (strEx != null && strEx.contains("Validation failed for classes")) {
            Pattern pattern = Pattern.compile(REGEX_DETAIL_ERROR);
            Matcher matcher = pattern.matcher(strEx);

            while (matcher.find()) {
                body.add(matcher.group(1));
            }

        } else if (strExStackTrace.contains("ConstraintViolationException")) {
            String constraintName = null;
            Pattern pattern = Pattern.compile(REGEX_CONSTRAINT_ERROR);
            Matcher matcher = pattern.matcher(strExStackTrace);

            if (matcher.find()) {
                constraintName = matcher.group(3).toUpperCase();
                constraintName = constraintName.substring(constraintName.indexOf(".") + 1);
            }

            if (constraintName != null) {
                if (strExStackTrace.contains("child record found")) {
                    body.add("CHILD_" + constraintName);
                } else if (strExStackTrace.contains("parent record found")) {
                    body.add("PARENT_" + constraintName);
                } else {
                    body.add(constraintName);
                }

            } else {
                pattern = Pattern.compile(REGEX_HIBERNATE_CONSTRAINT_ERROR);
                matcher = pattern.matcher(strExStackTrace);

                while (matcher.find()) {
                    body.add(matcher.group(1).toUpperCase());
                }
            }

        } else if (strExStackTrace.contains("SqlExceptionHelper") && strExStackTrace.contains("TLS_")) {
            strExStackTrace = strExStackTrace.substring(strExStackTrace.indexOf("TLS_"));
            strExStackTrace = strExStackTrace.substring(0, strExStackTrace.indexOf(" "));
            body.add(strExStackTrace);

        } else if (strEx != null && strEx.contains("PropertyValueException")) {
            body.add(strEx);
        }

        if (ex instanceof CommandFailureException) {
            body.add(((CommandFailureException) ex).getDesc());
        }

        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, dataName, body);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newResponse(ResultCode resultCode) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newResponse(HttpStatus httpStatus, String message) {
        WrapContent<Object> wrapContent = new WrapContent<>(message);
        return new ResponseMsg<>(wrapContent, httpStatus);
    }

    public static ResponseMsg newResponse(ResultCode resultCode, Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, body);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg newResponse(HttpStatus httpStatus, String resultCode, String message) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode, message);
        return new ResponseMsg<>(wrapContent, httpStatus);
    }

    public static ResponseMsg newResponse(ResultCode resultCode, String exDescription) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode.getCode(), exDescription);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg<Object> newOKResponse() {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.SUCCESS);
        return new ResponseMsg<>(wrapContent, ResultCode.SUCCESS.getHttpStatus());
    }

    public static ResponseMsg<Object> newOKResponse(Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.SUCCESS, body);
        return new ResponseMsg<>(wrapContent, ResultCode.SUCCESS.getHttpStatus());
    }

    public static ResponseMsg<Object> newOKResponse(String dataName, Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.SUCCESS, body);
        return new ResponseMsg<>(wrapContent, ResultCode.SUCCESS.getHttpStatus());
    }

    public static ResponseMsg<Object> new500ErrorResponse() {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.INTERNAL_SERVER_ERROR);
        return new ResponseMsg<>(wrapContent, ResultCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    protected static class WrapContent<T> extends HashMap<String, Object> {
        private Message messages = new Message();

        public WrapContent(ResultCode resultCode) {
            put(FieldNames.RESULT_CODE, resultCode.getCode());
            put(FieldNames.RESULT_MSG, messages.getMessage(resultCode.getCode()));
        }

        public WrapContent(String code, String message) {
            put(FieldNames.RESULT_CODE, code);
            put(FieldNames.RESULT_MSG, messages.getMessage(message));
        }

        public WrapContent(String message) {
            put(FieldNames.RESULT_CODE, ResultCode.OTHER_SERVLET_ERROR.getCode());
            put(FieldNames.RESULT_MSG, messages.getMessage(message));
        }

        public WrapContent(ResultCode resultCode, T content) {
            this(resultCode);
            String name = FieldNames.DATA;
            put(name, content);
        }

        public WrapContent(ResultCode resultCode, String fieldNames, T content) {
            this(resultCode);

            Map<String, String> mapContent = new HashMap<>();
            Map<String, String> errorDetail = new HashMap<>();

            if (content instanceof DetailError) {
                put(fieldNames, ((DetailError) content).getDetailError());
                return;

            } else if (content instanceof Iterable) {
                ((Iterable) content).forEach(item -> {
                    mapContent.put((String) item, null);
                });

            } else {
                mapContent.putAll((Map<? extends String, ? extends String>) content);
            }

            for (String key : mapContent.keySet()) {
                String translated = "", des = mapContent.get(key);

                if (key.endsWith(ResultCode.Code.REQUIRED_SUFFIX)) {
                    String field = key.replace(ResultCode.Code.REQUIRED_SUFFIX, "");
                    translated = field + " " + messages.getMessage(ResultCode.Code.CANNOT_BE_EMPTY);

                    key = key.replace(ResultCode.Code.REQUIRED_SUFFIX, ResultCode.Code.REQUIRED_SUFFIX.toLowerCase());
                    key = Utils.stringTrimEnd(key, 1);

                } else {
                    translated = messages.getMessage(key);
                }

                if (translated.equals(key)) {
                    translated = messages.getMessage(ResultCode.Code.ERR_OTHER) + " [" + key + "]";
                }

                if (!Utils.isNullOrEmpty(des)) {
                    translated += " => " + des;
                }

                errorDetail.put(key, translated);
            }

            put(fieldNames, errorDetail.entrySet());
        }
    }
}
