package com.mytel.digital_lucky_draw.utils;


/**
 * @author : Nguyen Ba Hung
 * @since : 21/09/2021, Sun
 * Purpose:
 **/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class I18n {
    private static Logger log = LoggerFactory.getLogger(I18n.class);

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    public I18n(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String t(String keyword, Object... params) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            log.info("locale: " + locale.getLanguage());
            return messageSource.getMessage(keyword, params, locale);
        } catch (Exception e) {
            log.error("Error: ", e);
            return keyword;
        }
    }

    public static String t(String key, String language, Object... params) {
        String locate = "my";
        if (("en".equalsIgnoreCase(language) || "my".equalsIgnoreCase(language))) {
            locate = language;
        }
        ResourceBundle rb = ResourceBundle.getBundle("messages_" + locate.toLowerCase());
        try {
            if (rb.containsKey(key)) {
                return String.format(rb.getString(key), params);
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return key;
    }
}


