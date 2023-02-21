package com.telsoft.libcore.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class Message {

    private ResourceBundleMessageSource messageSource;

    public Message() {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
    }

    /**
     * Add Request Header
     * Key              value
     * Accept-Language  vi-VN
     */
    public String getMessage(String key) {
//        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        Locale locale = curRequest.getLocale();
//        if (StringUtils.isEmpty(locale.getLanguage())) {
//            //locale = LocaleContextHolder.getLocale();
//            locale = Locale.US;
//        }
        Locale locale  = Locale.US;

        return messageSource.getMessage(key, null, locale);
    }

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessageUS(String key) {
        return messageSource.getMessage(key, null, Locale.US);
    }

    public String getMessageVN(String key) {
        return messageSource.getMessage(key, null, new Locale("vi", "VN"));
    }
}
