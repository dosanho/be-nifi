package com.telsoft.libcore.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static Map<String, Object> splitStringToMap(String input, String sperator) {
        Map<String, Object> result = new HashMap<>();

        if (!isNullOrEmpty(input)) {
            String[] values = StringUtils.splitByWholeSeparator(input, sperator);
            for (String value : values) {
                result.put(value, null);
            }
        }

        return result;
    }

//    public static Gson getGsonIgnoreNull() {
//        return new GsonBuilder().setDateFormat(Const.DFT.DATETIME_FORMAT).create();
//    }
//
//    public static Gson getGsonUtcIgnoreNull() {
//        return new GsonBuilder().registerTypeAdapter(Date.class, new GsonUtcDateAdapter()).create();
//    }
//
//    public static Gson getGson() {
//        return new GsonBuilder().serializeNulls().setDateFormat(Const.DFT.DATETIME_FORMAT).create();
//    }
//
//    public static Gson getGsonUtc() {
//        return new GsonBuilder().serializeNulls().registerTypeAdapter(Date.class, new GsonUtcDateAdapter()).create();
//    }
//
//    public static String getToken(HttpServletRequest request) throws Exception {
//        return fixNull(request.getHeader("Authorization"));
//    }
//
//    public static <T> T getCache(HazelcastInstance hazelcastInstance, String cacheKey, String dataKey, Class clazz) {
//        IMap<String, String> map = hazelcastInstance.getMap(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat(Const.DFT.DATETIME_FORMAT).create();
//        return (T) gson.fromJson(map.get(dataKey), clazz);
//    }

    public static String fixNull(String input) {
        if (input != null) {
            return input;

        } else {
            return "";
        }
    }

//    public static boolean isJson(String text) {
//        try {
//            new JSONObject(text);
//
//        } catch (Exception e) {
//            return false;
//        }
//
//        return true;
//    }
//
//    public static boolean isJsonArray(String text) {
//        try {
//            new JSONArray(text);
//
//        } catch (Exception e) {
//            return false;
//        }
//
//        return true;
//    }

    public static boolean isNullOrEmpty(Object... objs) {
        for (Object o : objs) {
            if (o == null) {
                return true;
            }

            if (o instanceof String && o.toString().trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String stringCut(String text, int length) {
        return text == null ? null : text.substring(0, text.length() > length ? length : text.length());
    }

    public static String stringTrimEnd(String text, int length) {
        return text == null ? null : text.substring(0, text.length() - length);
    }

    public static String getVarName(String columnName) {
        columnName = WordUtils.capitalizeFully(columnName.replaceAll("_", " "));
        columnName = columnName.replaceAll(" ", "");
        return WordUtils.uncapitalize(columnName);
    }

    public static Locale getLocale() {
        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Locale locale = curRequest.getLocale();
        if (StringUtils.isEmpty(locale.getLanguage())) {
            //locale = LocaleContextHolder.getLocale();
            locale = Locale.US;
        }

        return locale;
    }

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    public static boolean validateRegularExpressions(String strValue, String strPattern) {
        Pattern usrNamePtrn = Pattern.compile(strPattern);
        Matcher mtch = usrNamePtrn.matcher(strValue);
        return mtch.matches();
    }

    public static String replaceSpecialCharacterToSearch(String input) {
        String temp = null;
        try {
            temp = input.replaceAll("_", "__").replaceAll("%", "_%");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
