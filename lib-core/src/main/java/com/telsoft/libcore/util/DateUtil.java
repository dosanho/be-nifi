package com.telsoft.libcore.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtil {

    private static String PATTERN_ISO8601 = "^\\d{4}-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|Z)?$";

    public static java.sql.Date getSqlDate(String strDate) {
        return getSqlDate(getDate(strDate));
    }

    public static java.sql.Date getSqlDate(java.util.Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }
    public static Date getDate(String strDate) {
        if (isStandardDateValue(strDate)) {
            TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(strDate);
            Instant i = Instant.from(ta);
            return Date.from(i);
        }

        return null;
    }

    public static boolean isStandardDateValue(String input) {
        return Pattern.matches(PATTERN_ISO8601, input);
    }

}
